package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.DeepSeaLog;
import pt.isec.pa.deepsea.model.DroneRec;
import pt.isec.pa.deepsea.model.TipoUpgrade;
import pt.isec.pa.deepsea.model.data.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * O DeepSeaContext gere a Máquina de Estados Finita (FSM) do jogo.
 * <p>
 * Mantém a referência do estado atual da simulação e atua como intermediário
 * para os pedidos do exterior, delegando as ações para a classe de Estado ativa.
 * </p>
 *
 * @author Guilherme Eça, Luana Rabello e Miguel Zara
 */
public class DeepSeaContext implements Serializable {
    private final DeepSeaData data;
    private IDeepSeaState currentState;
    private int contadorIds;

    @Serial
    private static final long serialVersionUID = 1L;

    /** Cria uma nova instancia do 'Data' e defini o primeiro estado como superficie */
    public DeepSeaContext(){
        data = new DeepSeaData();
        currentState = DeepSeaState.SUPERFICIE.getInstance(data, this);
    }

    /** @return Estado atual da FSM. */
    public DeepSeaState getState(){
        return currentState.getState();
    }

    /**
     * Efetua a transição para um novo estado.
     * @param newState A nova instância de estado a ativar.
     */
    void changeState(IDeepSeaState newState){
        currentState = newState;
    }

    public boolean mover(String direcao) {
        return currentState.mover(direcao);
    }

    public boolean subir() {
        return currentState.subir();
    }

    public boolean mergulhar() {
        return currentState.mergulhar();
    }

    public boolean selecionaDrone(String droneId) {
        return currentState.selecionaDrone(droneId);
    }

    public boolean abastecer() {
        return currentState.abastecer();
    }

    public boolean reparar() {
        return currentState.reparar();
    }

    public boolean melhorar(TipoUpgrade melhoria) {
        return currentState.melhorar(melhoria);
    }

    public boolean moverPeca(int lin, int col) {
        return currentState.moverPeca(lin, col);
    }

    public boolean abrir() { return currentState.abrir(); }
    public boolean fechar() { return currentState.fechar(); }
    public void desistir() { currentState.desistir(); }

    public double getCombustivelNavio() {
        return data.getNavio().getCombustivel();
    }

    public int getMineriosNavio() {
        return data.getNavio().getMinerios();
    }

    public int getNumeroArtefactosRecolhidos() {
        return data.getNumeroArtefactosRecolhidos();
    }

    /** @return Uma cópia segura do drone selecionado para evitar alteração direta de referências. */
    public DroneRec getDroneSelecionado() {
        Drone d = data.getDroneSelecionado();
        if(d == null)
            return null;

        return new DroneRec(
                d.getIdentificacao(),
                d.getCombustivel(),
                d.getCombustivelMaximo(),
                d.getIntegridade(),
                d.getIntegridadeMaxima(),
                d.getNivelCasco(),
                d.getNivelCombustivel(),
                d.isPerdido(),
                d.getNumArtefactos(),
                d.getMinerios(),
                d.getLin(),
                d.getCol()
        );
    }

    /** * Converte a frota interna em objetos imutáveis para transporte de dados.
     * @return Lista de records imutáveis (DroneRec).
     */
    public List<DroneRec> getFrotaInfo() {
        List<DroneRec> list = new ArrayList<>();
        for(Drone d : data.getDronesOrdenadosPorCombustivel()){
            list.add(new DroneRec(
                    d.getIdentificacao(),
                    d.getCombustivel(),
                    d.getCombustivelMaximo(),
                    d.getIntegridade(),
                    d.getIntegridadeMaxima(),
                    d.getNivelCasco(),
                    d.getNivelCombustivel(),
                    d.isPerdido(),
                    d.getNumArtefactos(),
                    d.getMinerios(),
                    d.getLin(),
                    d.getCol()
            ));
        }
        return list;
    }

    public int getLinNavio() { return data.getNavio().getLin(); }
    public int getColNavio() { return data.getNavio().getCol(); }
    public int getSuperficieLin(){ return data.getSuperficieLin(); }
    public int getSuperficieCol(){ return data.getSuperficieCol(); }
    public boolean temArtefacto(int l, int c){ return data.temArtefacto(l,c); }
    public int getFossoLin(){ return data.getFossoLin(); }
    public int getFossoCol(){ return data.getFossoCol(); }
    public int getFundoLin(){ return data.getFundoLin(); }
    public int getFundoCol(){ return data.getFundoCol(); }
    public Fosso.FossoTipoElemento getFossoElemento(int l, int c){ return data.getFossoElemento(l,c); }
    public Fundo.FundoTipoElemento getFundoElemento(int l , int c) { return data.getFundoElemento(l,c); }
    public boolean isFundoRevelado(int l, int c){ return data.isFundoRevelado(l,c); }
    public boolean isDronePerdido(String id) {
        return data.isDronePerdido(id);
    }

    /** @return Uma cópia  da grelha do puzzle para proteger o estado interno. */
    public int[][] getPuzzleGrelha(){
        int[][] original = data.getPuzzleGrelha();
        if (original == null) return null;
        int [][] copia = new int[original.length][];

        for(int i = 0; i < original.length; i++){
            copia[i] = original[i].clone();
        }

        return copia;
    }
    public int getPuzzleMovimentosRestantes(){ return data.getMovRestantesPuzzle(); }

    /**
     * Grava o estado total do jogo num ficheiro binário.
     * <p>
     * Salva a instância do Manager (e por arrasto a FSM e Dados) e o contador
     * estático de IDs para garantir a consistência no carregamento.
     * </p>
     * @param file Ficheiro
     * @return true se gravado com sucesso.
     */
    public boolean saveGame(File file) {
        contadorIds = Obstaculo.getContadorAtual();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(this);
            DeepSeaLog.getInstance().log("Jogo gravado com sucesso: " + file.getName());
            return true;
        } catch (NotSerializableException e) {
            DeepSeaLog.getInstance().log("ERRO: Classe não serializável -> " + e.getMessage());
            return false;
        }catch (IOException e) {
            DeepSeaLog.getInstance().log("Erro ao gravar o jogo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Recupera um estado de jogo previamente gravado.
     * @param file Ficheiro binário
     * @return Instância carregada do DeepSeaContext.
     */
    public static DeepSeaContext loadGame(File file) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            DeepSeaContext dc = (DeepSeaContext) ois.readObject();
            Obstaculo.setContador(dc.contadorIds);
            DeepSeaLog.getInstance().log("Jogo carregado com sucesso: " + file.getName());
            return dc;
        } catch (IOException | ClassNotFoundException e) {
            DeepSeaLog.getInstance().log("Erro ao carregar o jogo: " + e.getMessage());
            return null;
        }
    }
}
