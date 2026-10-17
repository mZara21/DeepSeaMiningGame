package pt.isec.pa.deepsea.model.data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static pt.isec.pa.deepsea.model.data.Constantes.*;

/**
 * Representa um Drone de exploração aquática.
 * <p>
 * Esta classe gere o estado físico (integridade e combustível), a localização
 * nas diversas grelhas do jogo e a gestão de carga (minérios e artefactos).
 * </p>
 *
 * @author Guilherme Eça, Luana Rabello e Miguel Zara
 * @version 1.0
 */
public class Drone implements Comparable<Drone>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String identificacao;
    private double integridadeMaxima = INTEGRIDADE_MAXIMA_DRONE;
    private double combustivelMaximo = COMBUSTIVEL_MAXIMO_DRONE;
    private int nivelCombustivel = 1;
    private int nivelCasco = 1;

    private double integridade = INTEGRIDADE_MAXIMA_DRONE;
    private double combustivel = COMBUSTIVEL_INICIAL_DRONE;
    private int colisoesRochaAtual = 1;

    private int minerios = 0;
    private final List<Artefacto> artefactos;

    private int lin;
    private int col;
    private boolean destruido = false;

    /**
     * Construtor base para criação de um novo drone.
     * @param id Identificador único atribuído ao drone.
     */
    Drone(String id) {
        this.identificacao = id;
        this.artefactos = new ArrayList<>();
    }

    /**
     * Construtor de cópia utilizado para persistência ou clonagem de estados.
     * @param outro Instância de drone a ser replicada.
     */
    Drone(Drone outro) {
        this.identificacao = outro.identificacao;
        this.artefactos = new ArrayList<>(outro.artefactos);
        this.integridadeMaxima = outro.integridadeMaxima;
        this.combustivelMaximo = outro.combustivelMaximo;
        this.integridade = outro.integridade;
        this.combustivel = outro.combustivel;
        this.colisoesRochaAtual = outro.colisoesRochaAtual;
        this.minerios = outro.minerios;
        this.lin = outro.lin;
        this.col = outro.col;
        this.destruido = outro.destruido;
        this.nivelCombustivel = outro.nivelCombustivel;
        this.nivelCasco = outro.nivelCasco;
    }

    /**
     * Movimenta o drone e abate o custo de combustível.
     * @param lin Coordenada da linha de destino.
     * @param col Coordenada da coluna de destino.
     * @param qtd Valor de combustível a consumir.
     */
    void avanca(int lin, int col, double qtd) {
        this.lin = lin;
        this.col = col;
        this.combustivel -= qtd;
        if (this.combustivel <= 0) ficarPerdido();
    }

    /**
     * Adiciona combustível ao drone, respeitando a capacidade máxima.
     * @param qtd Valor a abastecer.
     * @return Valor efetivamente adicionado ao tanque.
     */
    double abastecer(double qtd) {
        if (combustivel + qtd > combustivelMaximo) {
            double realAdded = combustivelMaximo - combustivel;
            combustivel = combustivelMaximo;
            return realAdded;
        }
        combustivel += qtd;
        return qtd;
    }

    /** Restaura a integridade do drone para o valor máximo atual. */
    void repara() { this.integridade = integridadeMaxima; }

    /** Melhora a capacidade máxima de armazenamento de combustível. */
    boolean melhoraTanque() {
        if (nivelCombustivel >= 3)
            return false;
        this.combustivelMaximo += VALOR_UPGRADE_TANQUE;
        nivelCombustivel++;
        return true;
    }

    /** Melhora a resistência máxima do casco do drone. */
    boolean melhoraCasco() {
        if (nivelCasco >= 3)
            return false;
        this.integridadeMaxima += VALOR_UPGRADE_CASCO;
        nivelCasco++;
        return true;
    }

    /**
     * Aplica dano à integridade do drone.
     * @param qtd Quantidade de dano sofrido.
     */
    void perderIntegridade(double qtd) {
        integridade -= qtd;
        if (integridade <= 0) ficarPerdido();
    }

    /** Inutiliza o drone e remove-o das coordenadas ativas do mapa. */
    void ficarPerdido() {
        this.combustivel = 0;
        this.integridade = 0;
        this.lin = -1;
        this.col = -1;
        this.destruido = true;
    }

    /** Posiciona o drone na entrada do Fosso. */
    void prepararDescida() {
        this.lin = FOSSO_DRONE_INICIO_LINHAS;
        this.col = FOSSO_DRONE_INICIO_COLUNAS;
        resetPrimos();
    }

    /** Posiciona o drone na saída do Fosso para a Superfície. */
    void prepararSubida() {
        this.lin = FOSSO_LINHAS - 1;
        this.col = FOSSO_DRONE_INICIO_COLUNAS;
    }

    /** Posiciona o drone no centro do Leito Marinho. */
    void prepararFundo() {
        this.lin = FUNDO_LINHAS / 2;
        this.col = FUNDO_COLUNAS / 2;
    }

    /** @param qtd Quantidade de minério a adicionar à carga. */
    void recolherMinerio(int qtd) { if (qtd > 0) minerios += qtd; }

    /** @return Total de minério descarregado, limpando o porão do drone. */
    int descarregarMinerios() {
        int aux = minerios;
        minerios = 0;
        return aux;
    }

    /** @param a Artefacto a ser armazenado no drone. */
    void recolherArtefacto(Artefacto a) { if (a != null) artefactos.add(a); }

    /** @return Lista de artefactos descarregados, limpando o inventário. */
    List<Artefacto> descarregarArtefactos() {
        List<Artefacto> a = new ArrayList<>(artefactos);
        artefactos.clear();
        return a;
    }

    /**
     * Gera o próximo número primo para o cálculo de danos por colisão.
     * @return O valor do contador de colisões antes do incremento.
     */
    int getProximoPrimo() {
        int valorApoio = colisoesRochaAtual;
        while (true) {
            colisoesRochaAtual++;
            boolean ePrimo = true;
            for (int i = 2; i < colisoesRochaAtual; i++) {
                if (colisoesRochaAtual % i == 0) {
                    ePrimo = false;
                    break;
                }
            }
            if (ePrimo) return valorApoio;
        }
    }

    /** Reinicia o histórico de colisões com rochas. */
    public void resetPrimos() { this.colisoesRochaAtual = 1; }

    public double getCombustivel() { return combustivel; }
    public double getIntegridade() { return integridade; }
    public String getIdentificacao() { return identificacao; }
    public int getCol() { return col; }
    public int getLin() { return lin; }
    public boolean isPerdido() { return destruido; }
    public int getMinerios() { return minerios; }
    public int getNumArtefactos() { return artefactos.size(); }
    public int getNivelCasco() { return nivelCasco; }
    public int getNivelCombustivel() { return nivelCombustivel; }
    public double getIntegridadeMaxima() {return integridadeMaxima;}
    public double getCombustivelMaximo() {return combustivelMaximo;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Drone d)) return false;
        return d.identificacao.equals(identificacao);
    }

    @Override
    public int hashCode() { return Objects.hashCode(identificacao); }

    /** Compara drones com base no nível de combustível atual. */
    @Override
    public int compareTo(Drone outro) {
        return Double.compare(outro.combustivel, this.combustivel);
    }
}