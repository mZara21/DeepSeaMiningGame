package pt.isec.pa.deepsea.model;

import pt.isec.pa.deepsea.model.data.*;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static pt.isec.pa.deepsea.model.data.Constantes.*;

/**
 * A classe DeepSeaManager atua como a Facade do sistema.
 * <p>
 * Centraliza a comunicação entre a Interface Gráfica (UI) e a lógica (FSM e Dados).
 * Garante que a UI apenas interage com o modelo através de comandos de alto nível e
 * recebe dados de forma segura e imutável (através de Records e tipos primitivos).
 * </p>
 *
 * @author Guilherme Eça, Luana Rabello e Miguel Zara
 */

public class DeepSeaManager  {

    private DeepSeaContext context;
    private PropertyChangeSupport pcs;
    private final List<String> recentFiles = new ArrayList<>();

    public static final String PROP_MOVE = "_move_";
    public static final String PROP_DRONE = "_drone_";
    public static final String PROP_STATE = "_state_";
    public static final String PROP_LOG = "_log_";
    public static final String PROP_FILES = "_files_";
    public static final String PROP_EXPLOSAO = "_explosao_";
    public static final String PROP_COLETA = "_coleta_";

    /** Inicializa o gestor criando um novo contexto de jogo e PropertyChangeSupport. */
    public DeepSeaManager(){
        pcs = new PropertyChangeSupport(this);
        carregarFicheirosRecentes();
        novoJogo();
    }

    public void novoJogo(){
        this.context = new DeepSeaContext();

        DeepSeaLog.getInstance().clear();

        pcs.firePropertyChange(PROP_MOVE,null,null);
        pcs.firePropertyChange(PROP_STATE, null, null);
        pcs.firePropertyChange(PROP_DRONE, null, null);
        pcs.firePropertyChange(PROP_LOG, null, null);
    }

    private void novoJogo(DeepSeaContext context){
        this.context = context;
        DeepSeaLog.getInstance().clear();

        pcs.firePropertyChange(PROP_MOVE,null,null);
        pcs.firePropertyChange(PROP_STATE, null, null);
        pcs.firePropertyChange(PROP_DRONE, null, null);
        pcs.firePropertyChange(PROP_LOG, null, null);
    }

    /** @param direcao Direção do movimento (CIMA, BAIXO, ESQ, DIR)
     * @return Sucesso da operação. */
    public boolean mover(String direcao) {
        DeepSeaState old = context.getState();

        DroneRec antes = context.getDroneSelecionado();

        String el = null;
        if (antes != null) {
            int nl = antes.lin(), nc = antes.col();
            switch(direcao) {
                case "CIMA" -> nl--; case "BAIXO" -> nl++;
                case "ESQ" -> nc--; case "DIR" -> nc++;
            }
            try { el = getFundoElemento(nl, nc); } catch (Exception ignored) {}
        }

        boolean res =  context.mover(direcao);

        if (antes != null && !antes.isPerdido() && context.isDronePerdido(antes.id()))
            pcs.firePropertyChange(PROP_EXPLOSAO, null, null);

        if(res) {
            pcs.firePropertyChange(PROP_MOVE, null, null);
            if (el != null) {
                pcs.firePropertyChange(PROP_COLETA, null, el);
            }
        }
        pcs.firePropertyChange(PROP_STATE, old, context.getState());
        pcs.firePropertyChange(PROP_DRONE, null, null);
        pcs.firePropertyChange(PROP_LOG,null,null);

        return res;
    }

    public boolean subir() {
        DeepSeaState old = context.getState();
        boolean res = context.subir();

        pcs.firePropertyChange(PROP_STATE, old, context.getState());

        return res;
    }

    public boolean mergulhar() {
        DeepSeaState old = context.getState();
        boolean res = context.mergulhar();
        if (res)
            pcs.firePropertyChange(PROP_STATE, old, context.getState());
        pcs.firePropertyChange(PROP_LOG,null,null);

        return res;
    }
    /** @param droneId ID do drone a operar
     *  @return true se o drone foi selecionado com sucesso. */
    public boolean selecionaDrone(String droneId) {
        DroneRec old = context.getDroneSelecionado();

        boolean res = context.selecionaDrone(droneId);

        pcs.firePropertyChange(PROP_DRONE , old, context.getDroneSelecionado());
        pcs.firePropertyChange(PROP_LOG,null,null);

        return res;
    }
    public boolean abastecer() {
        boolean res = context.abastecer();
        if (res)
            pcs.firePropertyChange(PROP_DRONE, null, null);
        pcs.firePropertyChange(PROP_LOG,null,null);

        return res;
    }
    public boolean reparar() {
        boolean res = context.reparar();
        if (res)
            pcs.firePropertyChange(PROP_DRONE, null, null);
        pcs.firePropertyChange(PROP_LOG,null,null);

        return res;
    }
    public boolean melhorar(String melhoriaStr) {
        TipoUpgrade melhoria;
        try {
            melhoria = TipoUpgrade.valueOf(melhoriaStr);
        } catch (IllegalArgumentException e) {
            return false;
        }

        boolean res = context.melhorar(melhoria);
        if (res)
            pcs.firePropertyChange(PROP_DRONE, null, null);
        pcs.firePropertyChange(PROP_LOG,null,null);

        return res;
    }

    /** Gere o movimento das peças no minijogo do artefacto. */
    public boolean moverPeca(int lin, int col) {
        DeepSeaState estadoAntigo = context.getState();
        boolean res = context.moverPeca(lin, col);
        if (res) {
            pcs.firePropertyChange(PROP_MOVE, null, null);
        }
        // se ganho vou pro fundo, se perco vou pro fosso
        if (estadoAntigo != context.getState()) {
            pcs.firePropertyChange(PROP_STATE, estadoAntigo, context.getState());
            pcs.firePropertyChange(PROP_DRONE, null, null); // atualiza o painel lateral
        }
        return res;
    }

    public boolean abrir() {
        DeepSeaState old = context.getState();
        boolean res = context.abrir();
        if (res) {
            pcs.firePropertyChange(PROP_STATE, old, context.getState());
            pcs.firePropertyChange(PROP_DRONE, null, null);
        }
        pcs.firePropertyChange(PROP_LOG,null,null);

        return res;
    }
    public boolean fechar() {
        DeepSeaState old = context.getState();
        boolean res = context.fechar();
        if (res) {
            pcs.firePropertyChange(PROP_STATE, old, context.getState());
            pcs.firePropertyChange(PROP_DRONE, null, null);
        }
        pcs.firePropertyChange(PROP_LOG,null,null);

        return res;
    }

    public void desistir() {
        context.desistir();
        pcs.firePropertyChange(PROP_STATE,null,null);
    }

    /** @return Lista imutável de todos os eventos registados. */
    public List<String> getLogsJogo() {
        return DeepSeaLog.getInstance().getLogs();
    }

    /** Exporta o histórico para um ficheiro de texto definido nas constantes. */
    public void exportarLogTexto(File file) {
        DeepSeaLog.getInstance().saveToTextFile(file);
    }
    public void limparLogTexto() {
        DeepSeaLog.getInstance().clear();
        pcs.firePropertyChange(PROP_LOG, null,null);
    }

    // getters
    /** @return Estado atual da FSM */
    public DeepSeaState getState() { return context.getState(); }

    /** Obtém um snapshot imutável do drone selecionado.
     * @return DroneRec contendo os dados atuais ou null se nenhum drone estiver ativo.
     */
    public DroneRec getInfoDroneSelecionado(){
        return context.getDroneSelecionado();
    }

    // para representar superficie
    public int getSuperficieLin(){ return context.getSuperficieLin(); }
    public int getSuperficieCol(){ return context.getSuperficieCol(); }
    public boolean temArtefacto(int l, int c){ return context.temArtefacto(l,c); }

    public int getNavioLin(){ return context.getLinNavio(); }
    public int getNavioCol(){ return context.getColNavio(); }
    public double getNavioCombustivel(){ return context.getCombustivelNavio(); }
    public int getMinerios(){ return context.getMineriosNavio(); }
    public int getArtefactosRecolhidosCount(){ return context.getNumeroArtefactosRecolhidos(); }

    // representar fosso
    public int getFossoLin(){ return context.getFossoLin(); }
    public int getFossoCol(){ return context.getFossoCol(); }
    public String getFossoElemento(int l, int c){
        Fosso.FossoTipoElemento el = context.getFossoElemento(l,c);
        if (el == null) return "VAZIO";
        return el.name();
    }
    // representar fundo
    public int getFundoLin(){ return context.getFundoLin(); }
    public int getFundoCol(){ return context.getFundoCol(); }
    public String getFundoElemento(int l, int c){
        Fundo.FundoTipoElemento el = context.getFundoElemento(l,c);
        if (el == null) return "VAZIO";
        return el.name();
    }
    public boolean isFundoRevelado(int l, int c){ return context.isFundoRevelado(l,c); }

    /** @return Lista de snapshots (DroneRec) de todos os drones da frota. */
    public List<DroneRec> getFrotaInfo(){
        return context.getFrotaInfo();
    }

    public double getCustoMelhoria() {
        return CUSTO_MELHORIA_MINERIOS;
    }


    // representar puzzle
    public int[][] getPuzzleGrelha() {
        return context.getPuzzleGrelha();
    }

    public int getPuzzleMovimentosRestantes() {
        return context.getPuzzleMovimentosRestantes();
    }

    public int getDimensaoPuzzle() {
        return DIMENSAO_PUZZLE;
    }

    // Gravar/Carregar
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
        boolean res = context.saveGame(file);
        if(res)
            addRecentFile(file.getAbsolutePath());
        return res;
    }

    /**
     * Recupera um estado de jogo previamente gravado.
     * @param file Ficheiro binário
     * @return Instância carregada do DeepSeaContext.
     */
    public boolean loadGame(File file) {
        DeepSeaContext novoContexto = DeepSeaContext.loadGame(file);

        if (novoContexto == null)  return false;

        addRecentFile(file.getAbsolutePath());
        this.novoJogo(novoContexto);
        return true;
    }

    public List<String> getRecentFiles() {
        return Collections.unmodifiableList(recentFiles);
    }

    public void addRecentFile(String path) {
        if (path == null || path.isEmpty()) return;

        // Remover se já existir (para passar para o topo)
        recentFiles.remove(path);

        // Adicionar no início
        recentFiles.add(0, path);

        // Manter apenas os últimos 5
        if (recentFiles.size() > 5) {
            recentFiles.remove(5);
        }

        // guardar no ficherio de texto
        guardarFicheirosRecentes();

        pcs.firePropertyChange(PROP_FILES, null, recentFiles);
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property,listener);
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    private void carregarFicheirosRecentes(){
        File file = new File(NOME_FICHEIRO_GAME_FILES);
        if(!file.exists())
            return;

        try(BufferedReader br = new BufferedReader( new FileReader(file))){
            String linha;
            recentFiles.clear();
             while((linha = br.readLine()) != null){
                 linha = linha.trim();
                 if(!linha.isEmpty() && recentFiles.size() < 5){
                     recentFiles.add(linha);
                 }
             }
        }catch (Exception e){
            DeepSeaLog.getInstance().getLogs().add("Erro ao carregar ficheiros recentes: " + e.getMessage());
        }
    }

    private void guardarFicheirosRecentes(){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(new File(NOME_FICHEIRO_GAME_FILES)))){ // limpa o ficheiro automaticamente
            for(String linha : recentFiles){
                bw.write(linha);
                bw.newLine();
            }
        }
        catch (Exception e){
            DeepSeaLog.getInstance().getLogs().add("Erro ao escrever ficheiros recentes: " + e.getMessage());
        }
    }

}
