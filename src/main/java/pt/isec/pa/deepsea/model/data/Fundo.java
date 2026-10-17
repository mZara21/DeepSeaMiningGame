package pt.isec.pa.deepsea.model.data;

import static pt.isec.pa.deepsea.model.data.Constantes.*;

import java.io.Serial;
import java.util.List;
import java.util.Random;

/**
 * Representa a grelha bidimensional do Fundo.
 * <p>
 * Gere o estado de visibilidade das células e a geração procedimental de entidades estáticas
 * (minérios e artefactos) e dinâmicas (monstros). Executa a lógica de dispersão e reposicionamento
 * de carga (loot) quando um drone é perdido em missão.
 * </p>
 *
 * @author Guilherme Eça, Luana Rabello e Miguel Zara
 */

public class Fundo extends Grelha {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Construtor da grelha do Fundo.
     * Inicializa a matriz com instâncias de CelulaFundo.
     */
    Fundo(){
        super(FUNDO_LINHAS, FUNDO_COLUNAS);
        grelha = new CelulaFundo[linhas][colunas];
        for(int i=0; i<linhas; i++){
            for(int j=0; j<colunas; j++)
                grelha[i][j] = new CelulaFundo(i,j);
        }
    }

    /**
     * Identificadores de segurança para os tipos de elementos que podem ocupar uma célula do Fundo.
     * Utilizado para a UI consultar a grelha sem aceder às instâncias físicas dos objetos.
     */
    public enum FundoTipoElemento{
        VAZIO, MONSTRO, MINERIO, ARTEFACTO
    }

    public CelulaFundo getCelula(int l, int c){
        return (CelulaFundo) super.getCelula(l,c);
    }

    private boolean isCelulaRevelada(int l, int c){
        return getCelula(l, c).isRevelada();
    }

    private void revelarTodas() {
        int i, j;
        for(i = 0; i<getLinhas(); i++)
            for(j = 0; j<getColunas(); j++)
                getCelula(i,j).setRevelada(true);
    }

    private void esconderTodas(){
        for (int i = 0; i < getLinhas(); i++){
            for (int j = 0; j < getColunas(); j++){
                getCelula(i,j).setRevelada(false);
            }
        }
    }

    /**
     * Configura o estado inicial estático do Fundo na criação do jogo.
     * Distribui aleatoriamente as jazidas de minérios pelo mapa.
     */
    void prepara(){
        gerarMinerios();
    }

    /**
     * Prepara o Fundo para o início de uma expedição de mergulho.
     * Limpa os obstáculos antigos e efetua o spawn de novos monstros em células ocultas.
     */
    void inicia() {
        limpar();
        gerarMonstrosFundo();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int nl = FUNDO_LINHAS / 2 + i;
                int nc = FUNDO_COLUNAS / 2 + j;

                CelulaFundo cf = getCelula(nl,nc);
                cf.setRevelada(true);
            }
        }
    }

    /**
     * Posiciona um artefacto num espaço vazio aleatório da grelha.
     *
     * @param id O identificador único do artefacto.
     */
    void colocarArtefacto(int id){
        int[] pos = encontrarEspacoVazio(0, FUNDO_LINHAS, 0, FUNDO_COLUNAS);
        if (pos != null)
            addElemento(new Artefacto(pos[0], pos[1], id));
    }

    private void gerarMonstrosFundo() {

        Random rand = new Random();
        int nMonstros = rand.nextInt(MAX_MONSTROS_FUNDO) + 1;
        for (int i = 0; i < nMonstros; i++) {
            int[] pos = encontrarEspacoEscuro();
            if (pos != null) {
                addElemento(new Monstro(pos[0], pos[1]));
            }
        }
    }


    private void gerarMinerios(){

        Random rand = new Random();
        int numMinerios = rand.nextInt(MAX_MINERIOS - MIN_MINERIOS + 1) + MIN_MINERIOS;
        for (int i = 0; i < numMinerios; i++) {
            int[] pos = encontrarEspacoVazio(0, FUNDO_LINHAS, 0, FUNDO_COLUNAS);
            if (pos == null) break;
            int qtd = rand.nextInt(10) + 1;
            addElemento(new Minerio(pos[0], pos[1], qtd));
        }
    }

    /**
     * Reposiciona no fundo do mar a carga de um drone destruído.
     * <p>
     * Garante que os artefactos não são perdidos permanentemente e dispersa os minérios
     * aleatoriamente em pequenas quantidades pelas células vazias.
     * Executa um reset à visibilidade do mapa (esconde tudo).
     * </p>
     *
     * @param aPerdidos Lista de artefactos a reposicionar.
     * @param mPerdidos Quantidade total de minérios a dispersar.
     */
    void reposicionarRecursosPerdidos(List<Artefacto> aPerdidos, int mPerdidos) {
        if(aPerdidos == null && mPerdidos == 0) return;

        if (aPerdidos != null) {
            for (Artefacto a : aPerdidos) {
                int[] pos = encontrarEspacoVazio(0, FUNDO_LINHAS, 0, FUNDO_COLUNAS);

                if (pos != null) {
                    a.setLin(pos[0]);
                    a.setCol(pos[1]);

                    addElemento(a);
                }
            }
        }

        Random rand = new Random();
        while (mPerdidos > 0) {
            int qtdNaCelula = rand.nextInt(Math.min(3, mPerdidos)) + 1; // podem ficar de 1 a 3 minerios em cada celula
            int[] pos = encontrarEspacoVazio(0, FUNDO_LINHAS, 0, FUNDO_COLUNAS);
            if(pos != null){
                addElemento(new Minerio(pos[0], pos[1], qtdNaCelula));
                mPerdidos -= qtdNaCelula;
            }
            else{
                break;
            }
        }
        esconderTodas();
    }

    /**
     * Mete numas posições fixas de minérios necessárias para o modo de defesa/testes.
     */
    void preparaDefesa(){
        revelarTodas();

        int linCentro = FUNDO_LINHAS / 2;
        int colCentro = FUNDO_COLUNAS / 2;

        //Minerio 1 no 1 quadrante
        addElemento(new Minerio(linCentro / 2, colCentro / 2, 1));

        // Minerio 2 noutro canto longe do Minério 1
        int linBaixo = Math.min(linCentro + 2, FUNDO_LINHAS - 1);
        int colDireita = Math.min(colCentro + 2, FUNDO_COLUNAS - 1);
        addElemento(new Minerio(linBaixo, colDireita, MAX_MINERIOS));
    }

    /**
     * Mete numa posição fixa do monstro necessária para o modo de defesa/testes.
     */
    void iniciaDefesa(){
        int linCentro = FUNDO_LINHAS / 2;
        int colCentro = FUNDO_COLUNAS / 2;

        addElemento(new Monstro(linCentro, colCentro));
    }

    /**
     * Mete o artefacto num local previsível para facilitar o modo de defesa/testes.
     *
     * @param id O identificador do artefacto.
     */
    void colocarArtefactoDefesa(int id){

        Random rand = new Random();
        boolean lado = rand.nextBoolean();
        if (lado){
            int[] pos = encontrarEspacoVazio(0, FUNDO_LINHAS, 0, FUNDO_COLUNAS / 4);
            if (pos == null) return;
            addElemento(new Artefacto(pos[0], pos[1], id));
        }else{
            int[] pos = encontrarEspacoVazio(0, FUNDO_LINHAS,
                    FUNDO_COLUNAS - FUNDO_COLUNAS / 4, FUNDO_COLUNAS);
            if (pos == null) return;
            addElemento(new Artefacto(pos[0], pos[1], id));
        }

    }

    /**
     * Efetua a procura de uma coordenada segura e escondida do jogador.
     *
     * @return Array com as coordenadas [linha, coluna] da célula escolhida ou null caso atinja o limite de tentativas.
     */
    private int[] encontrarEspacoEscuro() {
        int l, c, tentativas = 0;
        int maxTentativas = FUNDO_COLUNAS * FUNDO_LINHAS;

        Random rand = new Random();
        do {
            l = rand.nextInt(FUNDO_LINHAS);
            c = rand.nextInt(FUNDO_COLUNAS);
            if (tentativas++ > maxTentativas) return null; // Prevenir loop infinito
        } while (!celulaVazia(l, c) || isCelulaRevelada(l, c));
        return new int[]{l, c};
    }

    /**
     * Verifica a existência de artefactos não recolhidos no leito marinho.
     *
     * @return true se existir pelo menos um artefacto na grelha.
     */
    public boolean temArtefacto() {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                if (getCelula(i, j).getElemento() instanceof Artefacto) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Identifica o tipo primitivo/enum de um elemento para exposição segura na arquitetura UI.
     *
     * @param l Linha a consultar.
     * @param c Coluna a consultar.
     * @return O num FundoTipoElemento que corresponde ao objeto alojado na célula.
     */
    FundoTipoElemento getTipoElemento(int l, int c){
        CelulaFundo cel = getCelula(l,c);
        Elemento el = cel.getElemento();
        return switch (el) {
            case Monstro m -> FundoTipoElemento.MONSTRO;
            case Artefacto a -> FundoTipoElemento.ARTEFACTO;
            case Minerio m -> FundoTipoElemento.MINERIO;
            case null, default -> FundoTipoElemento.VAZIO;
        };
    }
}