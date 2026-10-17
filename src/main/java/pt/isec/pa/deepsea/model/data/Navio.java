package pt.isec.pa.deepsea.model.data;

import pt.isec.pa.deepsea.model.DeepSeaLog;
import pt.isec.pa.deepsea.model.TipoUpgrade;

import static pt.isec.pa.deepsea.model.data.Constantes.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Representa o Navio principal na superfície do oceano.
 * <p>
 * Atua como a base de operações, gerindo a frota de drones, o combustível global,
 * a oficina de melhorias e o armazenamento de recursos (minérios e artefactos).
 * </p>
 * @author Guilherme Eça, Luana Rabello e Miguel Zara
 * @version 1.0
 */
public class Navio implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private double combustivel = COMBUSTIVEL_NAVIO;
    private int minerios = 0;
    private final List<Artefacto> artefactos;

    private final Set<Drone> frota;
    private Drone droneSelecionado = null;

    private int lin;
    private int col;

    /**
     * Inicializa o navio numa posição específica e gera a frota inicial de drones.
     * @param l Linha inicial.
     * @param c Coluna inicial.
     */
    Navio(int l, int c) {
        this.lin = l;
        this.col = c;
        this.artefactos = new ArrayList<>();
        this.frota = new HashSet<>();

        for (int i = 0; i < N_DRONES; i++) {
            String nome = String.format("Drone %d", i + 1);
            this.frota.add(new Drone(nome));
        }
    }

    /**
     * Seleciona um drone da frota para operações.
     * @param id Identificador do drone.
     * @return true se o drone foi encontrado e selecionado.
     */
    boolean selecionarDrone(String id) {
        droneSelecionado = getDronePorId(id);
        return droneSelecionado != null;
    }

    /** Remove a seleção do drone atual. */
    void desselecionarDrone() {
        droneSelecionado = null;
    }

    /**
     * Abastece o drone indicado utilizando o combustível do navio.
     * @param d O drone a abastecer.
     */
    void abasteceDrone(Drone d) {
        if (d == null) return;

        double diferenca = d.getCombustivelMaximo() - d.getCombustivel();

        double qtd = diferenca > combustivel ? combustivel : diferenca;

        combustivel -= d.abastecer(qtd);
    }

    /**
     * Repara a integridade de um drone na oficina.
     * @param id ID do drone a reparar.
     * @return true se a reparação foi concluída com sucesso.
     */
    boolean reparaDrone(String id) {
        Drone d = getDronePorId(id);
        if (d == null || !frota.contains(d)) return false;
        if (combustivel - CONSUMO_REPARACAO_CASCO < 0) return false;

        d.repara();
        combustivel -= CONSUMO_REPARACAO_CASCO;
        return true;
    }

    /**
     * Aplica melhorias tecnológicas ao drone selecionado.
     * @param d Drone a melhorar.
     * @param melhoria Tipo de melhoria (1 para Tanque, 2 para Casco).
     * @return true se a melhoria foi aplicada.
     */
    boolean melhoraDrone(Drone d, TipoUpgrade melhoria) {
        if (d == null || !frota.contains(d) || d.isPerdido() || minerios < CUSTO_MELHORIA_MINERIOS)
            return false;

        boolean res;

        switch (melhoria) {
            case TipoUpgrade.COMBUSTIVEL -> res = d.melhoraTanque();
            case TipoUpgrade.CASCO -> res = d.melhoraCasco();
            default -> { return false; }
        }

        if (!res) return false;

        minerios -= CUSTO_MELHORIA_MINERIOS;
        return true;
    }

    /**
     * Transfere minérios e artefactos do drone para o porão do navio.
     * @param d Drone que está a descarregar.
     */
    void recolherRecursos(Drone d) {
        if (d == null || !frota.contains(d)) return;

        minerios += d.descarregarMinerios();
        artefactos.addAll(d.descarregarArtefactos());
    }

    /**
     * Movimenta o navio na superfície consumindo combustível.
     * @param direcao Direção do movimento.
     * @param s Referência à superfície para validar limites.
     * @return true se o movimento foi realizado.
     */
    boolean move(String direcao, Superficie s) {
        if (combustivel - CONSUMO_MOVIMENTO_NAVIO < 0) {
            DeepSeaLog.getInstance().log("Falha: Combustível insuficiente para mover o navio.");
            return false;
        }

        int proximaLinha = lin;
        int proximaColuna = col;

        switch (direcao.toUpperCase()) {
            case "CIMA" -> proximaLinha--;
            case "BAIXO" -> proximaLinha++;
            case "ESQ" -> proximaColuna--;
            case "DIR" -> proximaColuna++;
            default -> { return false; }
        }

        try {
            // Valida se a célula existe antes de mover
            s.getCelula(proximaLinha, proximaColuna);
            lin = proximaLinha;
            col = proximaColuna;
            combustivel -= CONSUMO_MOVIMENTO_NAVIO;
            return true;
        } catch (Exception e) {
            DeepSeaLog.getInstance().log("Movimento bloqueado: Limite da superfície atingido.");
            return false;
        }
    }

    /** @return A referência real do drone selecionado (uso interno do model). */
    Drone getDroneReal() {
        return droneSelecionado;
    }

    /** @return Uma cópia segura do drone selecionado para a UI. */
    public Drone getDroneSelecionado() {
        return (droneSelecionado == null) ? null : new Drone(droneSelecionado);
    }

    public int getLin() { return lin; }
    public int getCol() { return col; }
    public int getMinerios() { return minerios; }
    public double getCombustivel() { return combustivel; }

    /** @return Lista imutável dos artefactos recolhidos. */
    public List<Artefacto> getArtefactos() {
        return List.copyOf(artefactos);
    }

    /** @return Conjunto imutável da frota de drones. */
    public Set<Drone> getDrones() {
        return Set.copyOf(frota);
    }

    /**
     * Procura um drone na frota pelo seu ID.
     * @param id Identificação do drone.
     * @return Referência do drone ou null se não encontrado.
     */
    public Drone getDronePorId(String id) {
        for (Drone d : frota) {
            if (d.getIdentificacao().equals(id))
                return d;
        }
        return null;
    }
}