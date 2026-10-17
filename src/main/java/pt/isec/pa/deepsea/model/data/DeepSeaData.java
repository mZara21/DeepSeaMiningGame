package pt.isec.pa.deepsea.model.data;

import pt.isec.pa.deepsea.model.DeepSeaLog;
import pt.isec.pa.deepsea.model.TipoUpgrade;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Set;

import static pt.isec.pa.deepsea.model.data.Constantes.*;

/**
 * Motor de dados (Data Model) e lógica central do jogo.
 * <p>
 * Armazena as instâncias dos mapas (Superfície, Fosso, Fundo, Puzzle) e das entidades (Navio, Drones).
 * É a classe que executa as regras físicas de negócio, processa alterações de coordenadas, calcula consumos
 * de combustível, aplica danos de colisão ao casco e efetua as transações de carga (minérios e artefactos).
 * </p>
 * @author Guilherme Eça, Luana Rabello e Miguel Zara
 */

public class DeepSeaData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Navio navio;
    private final Superficie superficie;
    private Puzzle puzzle;

    public DeepSeaData() {
        this.navio = new Navio(SUPERFICIE_LINHAS / 2, SUPERFICIE_COLUNAS / 2);
        this.superficie = new Superficie();

        Gerador g = new Gerador();
        g.preparaAmbiente(superficie);
    }

    // Navio

    /** Move o navio e regista nos logs
     * @param direcao Direcao do movimento (CIMA,DIR,BAIXO,ESQ) */
    public boolean moveNavio(String direcao){
        boolean res = navio.move(direcao,superficie);
        if(res)
            DeepSeaLog.getInstance().log(String.format("Navio moveu-se para (%d,%d)",navio.getLin(),navio.getCol()));
        return res;
    }

    /** Transfere os artefactos e minerios do drone selecionado para o navio */
    public void recolheRecursos(){
        Drone d = navio.getDroneReal();
        if (d != null) {
            navio.recolherRecursos(d);
            navio.desselecionarDrone();
            DeepSeaLog.getInstance().log("Drone descarregou recursos");
        }
    }

    /** * Prepara o ambiente para mergulho.
     * @return true se o drone selecionado estiver apto a mergulhar.
     */
    public boolean mergulhar(){
        Drone d = navio.getDroneReal();

        if(d == null){
            DeepSeaLog.getInstance().log("Falha ao mergulhar. Nao existe drone selecionado.");
            return false;
        }

        if(d.getCombustivel() == 0 || d.isPerdido()){
            DeepSeaLog.getInstance().log("Falha ao mergulhar. O drone selecionado nao esta apto para mergulhar.");
            return false;
        }

        int l = navio.getLin();
        int c = navio.getCol();

        d.prepararDescida();

        superficie.iniciaFosso(l,c);

        DeepSeaLog.getInstance().log(String.format("Drone - %s mergulhou.",d.getIdentificacao()));

        return true;
    }


    //Drone
    /**
     * Gere o movimento do drone, consumos de combustível e visibilidade.
     * @param direcao Direcao do movimento (CIMA,DIR,BAIXO,ESQ)
     * @param isFundo Define se a colisão é verificada na grelha do Fundo ou do Fosso.
     */
    public boolean moverDrone(String direcao, boolean isFundo) {
        Drone d = navio.getDroneReal();
        if (d == null || d.isPerdido()) return false;

        int[] pos = proxPos(d.getLin(), d.getCol(), direcao);
        int l = pos[0];
        int c = pos[1];

        if (isFundo) {
            if (l < 0 || l >= Constantes.FUNDO_LINHAS || c < 0 || c >= Constantes.FUNDO_COLUNAS) {
                DeepSeaLog.getInstance().log("Movimento bloqueado: Limite do Fundo.");
                return false;
            }
        } else {
            if (l < 0 || l >= Constantes.FOSSO_LINHAS || c < 0 || c >= Constantes.FOSSO_COLUNAS) {
                DeepSeaLog.getInstance().log("Movimento bloqueado: Limite do Fosso.");
                return false;
            }
        }

        CelulaSuperficie cSup = superficie.getCelulaSuperficie(navio.getLin(), navio.getCol());
        Celula celulaDestino = isFundo ? cSup.getCelulaFundo(l, c) : cSup.getCelulaFosso(l, c);
        Elemento e = celulaDestino.getElemento();



        if (isFundo) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int nl = l + i;
                    int nc = c + j;
                    if (nl >= 0 && nl < Constantes.FUNDO_LINHAS && nc >= 0 && nc < Constantes.FUNDO_COLUNAS) {
                        CelulaFundo cf = cSup.getCelulaFundo(nl, nc);
                        cf.setRevelada(true);
                    }
                }
            }


            if(e instanceof Minerio m){
                d.recolherMinerio(m.getQuantidade());
                celulaDestino.setElemento(null);
                DeepSeaLog.getInstance().log(m.getQuantidade() + " minérios encontrados!");
                e = null;
            }
        }

        double consumo = CONSUMO_MOVIMENTO_DRONE;
        if (e instanceof CorrenteAgua corrente) consumo *= corrente.getMultiplicador();

        if (!registaColisao(d, e)) {
            DeepSeaLog.getInstance().log("Movimento bloqueado para " + direcao + ".");
            return false;
        }

        d.avanca(l, c, consumo);
        DeepSeaLog.getInstance().log("Drone moveu-se para " + direcao + ".");
        return true;
    }

   /**
    * Abastece o drone
    * @param id Identificação do drone
    * */
    public void abasteceDrone(String id){
        Drone d = navio.getDronePorId(id);
        if (d != null) navio.abasteceDrone(d);
    }

    /**
     * Aplica melhorias no drone ( 1- Melhora Tanque, 2 - Melhora Casco)
     * @param id Identificação do drone
     * @param tipoMelhoria Tipo da melhoria
     * @return true se foi possível melhorar o drone
     *  */
    public boolean melhorarDrone(String id, TipoUpgrade tipoMelhoria){
        Drone d = navio.getDronePorId(id);
        if(d != null) {
            return navio.melhoraDrone(d, tipoMelhoria);
        }
        return false;
    }

    /**
     * Utiliza combustivel para reparar o drone
     * @param id Identificacao do drone
     * @return true se a reparacao foi possivel
     * */
    public boolean reparaDrone(String id){
        return navio.reparaDrone(id);
    }

    /**
     * Seleciona um drone atraves do id
     * @param id Identificacao do drone
     * @return true se foi encontrado um drone com o ID fornecido
     * */
    public boolean selecionaDrone(String id){
        boolean res = navio.selecionarDrone(id);

        if(res)
            DeepSeaLog.getInstance().log(String.format("Drone -  %s foi selecionado",navio.getDroneSelecionado().getIdentificacao()));
        else
            DeepSeaLog.getInstance().log("Falha ao selecionar drone.");

        return res;
    }

    /** Desseleciona o drone selecionado */
    public void desselecionarDrone(){
        navio.desselecionarDrone();
        DeepSeaLog.getInstance().log("Drone desselecionado.");
    }

    /** Redistribuidos artefactos e minerios de um drone perdido */
    public void processaPerdaDrone(){
        Drone d = navio.getDroneReal();
        if (d == null) return;

        List<Artefacto> aPerdidos = d.descarregarArtefactos();
        int mPerdidos = d.descarregarMinerios();

        if (!aPerdidos.isEmpty() || mPerdidos != 0) {
            superficie.getCelulaSuperficie(navio.getLin(), navio.getCol()).reposicionarRecursos(aPerdidos, mPerdidos);
            // reposicionar recursos ja deixa o fundo escuro de novo
            DeepSeaLog.getInstance().log(String.format(
                    "Recursos perdidos no fundo do mar: %d artefacto(s) e %d minério(s).",
                    aPerdidos.size(), mPerdidos
            ));
        }

        navio.desselecionarDrone();
    }


    /** Prepara o drone para a descida ate ao fundo, inicia tambem o ambiente do fundo */
    public void descerDrone(){
        navio.getDroneReal().prepararFundo();
        superficie.iniciaFundo(navio.getLin(), navio.getCol());
        DeepSeaLog.getInstance().log("Drone entrou no fundo");
    }

    /** Prepara o drone para a subida para o ofsso, apos estar no fundo.
     * Tambem e iniciado o fosso novamente.  */
    public void subirDrone(){
        navio.getDroneReal().prepararSubida();
        superficie.iniciaFosso(navio.getLin(), navio.getCol());
        DeepSeaLog.getInstance().log("Drone voltou ao fosso");
    }


    public boolean isDroneNaSaidaFosso() {
      if (isDronePerdido()) return false;
      return navio.getDroneReal().getLin() == 0;
    }

    public boolean isDroneNoFundoDoFosso() {
        if (isDronePerdido()) return false;
        return navio.getDroneReal().getLin() == FOSSO_LINHAS - 1;
    }

    public boolean isDronePerdido() {
        Drone d = navio.getDroneReal();
        return d == null || d.isPerdido();
    }

    public boolean isDronePerdido(String id) {
        for (Drone d : navio.getDrones()) {
            if (d.getIdentificacao().equals(id)) {
                return d.isPerdido();
            }
        }
        return false;
    }

    /** Verifica se o jogo terminou, com os seguintes critérios:
     *  <ul>
     *      <li> Não ha drones com integridade, nao da para recolher artefactos. </li>
     *      <li> O navio nao tem combustivel para se mexer nem para abastecer drones, e nenhum drone tem combustivel </li>
     *      <li> Não ha drones com integridade, nao da para recolher artefactos. </li>
     *  </ul>
     * @return true se o jogo terminou
     * */
    public boolean terminou() {
        if (getNumeroArtefactosRecolhidos() >= Constantes.N_ARTEFACTOS) {
            return true;
        }

        // Se não ha drones com integridade, nao da para recolher artefactos
        Set<Drone> frota = navio.getDrones();
        boolean existeDroneVivo = false;
        for (Drone d : frota) {
            // Se nao esta perdido E tem combustivel proprio OU o navio tem combustivel para o abastecer
            if (!d.isPerdido() && (d.getCombustivel() > 0 || navio.getCombustivel() > 0)) {
                existeDroneVivo = true;
                break;
            }
        }
        if (!existeDroneVivo) return true;


        // O navio está a zero, nao tem combustivel para se mexer nem para abastecer drones
        // E nenhum drone tem combustivel
        if (navio.getCombustivel() <= 0) {
            boolean algumDroneTemEnergia = false;
            for (Drone d : frota) {
                if (d.getCombustivel() > 0 && !d.isPerdido()) {
                    algumDroneTemEnergia = true;
                    break;
                }
            }

            // Se o navio parou e nenhum drone consegue se mexer, tambem acaba o jogo
            return !algumDroneTemEnergia;
        }

        return false;
    }

    /** Realiza a logica de fim de jogo
     * @return true se o jogador ganhou */
    // True - ganhou, False - perdeu
    public boolean fimDeJogo(){
        if (getNumeroArtefactosRecolhidos() >= Constantes.N_ARTEFACTOS) {
            DeepSeaLog.getInstance().log("Foram colectados todos os artefactos.");
            return true;
        }

        boolean algumOperacional = false;
        for (Drone d : navio.getDrones()) {
            if (!d.isPerdido()) {
                algumOperacional = true;
                break;
            }
        }

        if (!algumOperacional && navio.getCombustivel() <= 0) {
            DeepSeaLog.getInstance().log("Nao existem mais drones operacionais.");
        } else {
            DeepSeaLog.getInstance().log("O jogo foi terminado.");
        }
            return false;
    }


    // Puzzle
    /** Logica de iniciar o puzzle
     * @return true se foi iniciado com sucesso */
    public boolean iniciarPuzzle() {
        CelulaFundo cF = getCelulaFundoAtual();

        if (cF != null && cF.getElemento() instanceof Artefacto) {
            this.puzzle = new Puzzle();
            DeepSeaLog.getInstance().log("Minijogo iniciado! Resolva o puzzle para extrair o Artefacto.");
            return true;
        }

        DeepSeaLog.getInstance().log("Não há nenhum Artefacto nesta posição para abrir.");
        return false;
    }

    /** Realiza logica para mover peca no puzzle
     * @param col coluna
     * @param lin linha
     * @return true se o movimento de certa peca e permitido */
    public boolean moverPeca(int lin, int col){
        if (puzzle == null) return false;

        if (puzzle.moverPeca(lin, col)) {
            DeepSeaLog.getInstance().log(String.format("Peça movida para a posição (%d, %d).", lin, col));
            return true;
        }

        DeepSeaLog.getInstance().log("Não foi possível mover a peça.");
        return false;
    }

    /** @return true se o puzzle esta resolvido */
    public boolean puzzleResolvido(){
        if (puzzle == null) return false;

        if (puzzle.isResolvido()) {
            DeepSeaLog.getInstance().log("Puzzle resolvido!");
            return true;
        }
        return false;
    }

    /** Retorna se o puzzle tem movimentos
     * @return true se o puzzle tem movimentos */
    public boolean puzzlTemMov(){
        if (puzzle == null) return false;

        if (puzzle.temMovimentos()) {
            return true;
        }

        DeepSeaLog.getInstance().log("Perdeu o Puzzle!");
        return false;
    }

    /** Lógica para perder o puzzle
     * @return true se operacao foi bem sucedida */
    public boolean perderPuzzle(){
        if (isDronePerdido()) return false;
        Drone d = navio.getDroneReal();
        d.prepararSubida();
        superficie.iniciaFosso(navio.getLin(), navio.getCol());
        DeepSeaLog.getInstance().log("Perdeu puzzle desmoronou!");
        limparPuzzle();
        return true;
    }
    /** Lógica para recolher artefacto
     * @return true se a operacao for bem sucedida */
    public boolean recolherArtefactoDoFundo() {
        CelulaFundo cF = getCelulaFundoAtual();

        if (cF != null && cF.getElemento() instanceof Artefacto a) {
            navio.getDroneReal().recolherArtefacto(a);
            cF.setElemento(null);
            DeepSeaLog.getInstance().log("Artefacto guardado no drone com sucesso.");
            limparPuzzle();
            return true;
        }
        return false;
    }

    public List<Drone> getDronesOrdenadosPorCombustivel() {
        List<Drone> lista = new ArrayList<>(navio.getDrones());
        Collections.sort(lista); // usa o compareTo do drone, que ja compara pelo combustivel
        return lista;
    }
    public List<Drone> getDronesOrdenadosPorIntegridade() {
        List<Drone> lista = new ArrayList<>(navio.getDrones());
        lista.sort( (d1,d2) ->
                Double.compare(d2.getIntegridade(), d1.getIntegridade()) ); // queria comparator com lambda function
        return lista;
    }



    //auxiliares
    /** @param direcao Direcao do movimento (CIMA,DIR,BAIXO,ESQ)
     * @return nova posicao tendo em conta a direcao enviada */
    private int[] proxPos(int lin, int col, String direcao){
        switch (direcao.toUpperCase()){
            case "DIR" -> col++;
            case "ESQ" -> col--;
            case "CIMA" -> lin--;
            case "BAIXO" -> lin++;
        }
        return new int[]{lin, col};
    }

    /** Logica para colisao com elementos
     * @return true se o objeto for passavel  */
    private boolean registaColisao(Drone d, Elemento e){
        switch (e) {
            case null -> {
                return true;
            }
            case Rocha rocha -> d.perderIntegridade(d.getProximoPrimo());
            case Obstaculo o -> d.perderIntegridade(o.getDano());
            default -> {
            }
        }

        return e.isPassavel();
    }

    private void limparPuzzle() {
        this.puzzle = null;
        DeepSeaLog.getInstance().log("Puzzle limpo.");
    }


    private CelulaFundo getCelulaFundoAtual() {
        Drone d = navio.getDroneReal();
        if (d == null || d.isPerdido()) return null;

        CelulaSuperficie cSup = superficie.getCelulaSuperficie(navio.getLin(), navio.getCol());
        return cSup.getCelulaFundo(d.getLin(), d.getCol());
    }


    // getters
    public int getSuperficieLin(){ return superficie.getLinhas();}
    public int getSuperficieCol(){ return superficie.getColunas();}
    public int getFossoLin(){ return FOSSO_LINHAS;  }
    public int getFossoCol(){ return FOSSO_COLUNAS; }
    public int getFundoLin(){ return FUNDO_LINHAS;  }
    public int getFundoCol(){ return FUNDO_COLUNAS; }
    public boolean temArtefacto(int l, int c){ return superficie.temArtefacto(l,c); }
    public Drone getDroneSelecionado(){
        return navio.getDroneSelecionado();
    }
    public Superficie getSuperficie() { return superficie; }
    public Navio getNavio() { return navio; }
    public int getNumeroArtefactosRecolhidos(){ return navio.getArtefactos().size(); }

    public Fosso.FossoTipoElemento getFossoElemento(int l, int c){
        CelulaSuperficie cel = superficie.getCelulaSuperficie(navio.getLin(), navio.getCol());
        return cel.getFosso().getTipoElemento(l,c);
    }
    public Fundo.FundoTipoElemento getFundoElemento(int l, int c){
        CelulaSuperficie cel = superficie.getCelulaSuperficie(navio.getLin(), navio.getCol());
        return cel.getFundo().getTipoElemento(l,c);
    }
    public boolean isFundoRevelado(int l, int c){
        CelulaSuperficie cel = superficie.getCelulaSuperficie(navio.getLin(), navio.getCol());
        return cel.getFundo().getCelula(l,c).isRevelada();
    }

    public int[][] getPuzzleGrelha() {
        if (puzzle == null) return null;
        return puzzle.getGrelha();
    }

    public int getMovRestantesPuzzle(){
        if (puzzle == null) return 0;
        return puzzle.getMovimentosRestantes();
    }
    public boolean isDroneEmCimaArtefacto(){
        Drone d = navio.getDroneSelecionado();
        if(d == null || d.isPerdido() ) return false;

        int l = d.getLin();
        int c = d.getCol();

        CelulaFundo cel = superficie.getCelulaSuperficie(navio.getLin(), navio.getCol()).getCelulaFundo(l,c);
        return cel.getElemento() instanceof Artefacto;
    }
}