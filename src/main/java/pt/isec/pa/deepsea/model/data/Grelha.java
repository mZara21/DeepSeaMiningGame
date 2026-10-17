package pt.isec.pa.deepsea.model.data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Random;

/**
 * Classe abstrata base para todas as matrizes bidimensionais do jogo.
 * <p>
 * Gere a estrutura fundamental de coordenadas (linhas e colunas), a alocação de memória
 * para as células e fornece os algoritmos comuns para manipulação espacial, como a inserção
 * de elementos, procura de espaços vazios e limpeza da matriz.
 * </p>
 *
 * @author Guilherme Eça, Luana Rabello e Miguel Zara
 */

abstract public class Grelha implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected int linhas, colunas;
    protected Celula [][]grelha;

    /**
     * Construtor base para inicialização das dimensões da matriz.
     *
     * @param lin Número total de linhas.
     * @param col Número total de colunas.
     */
    Grelha(int lin, int col){
        linhas = lin;
        colunas = col;
    }

    /**
     * Verifica se a célula na coordenada especificada está livre de elementos físicos.
     *
     * @param linha Coordenada da linha.
     * @param coluna Coordenada da coluna.
     * @return true se o conteúdo da célula for nulo; false se estiver ocupada.
     */
    public boolean celulaVazia(int linha, int coluna){
        return grelha[linha][coluna].getElemento() == null;
    }

    /**
     * Obtém a referência da célula para uma posição específica.
     *
     * @param l Coordenada da linha.
     * @param c Coordenada da coluna.
     * @return A instância da Célula nessa posição.
     * @throws IndexOutOfBoundsException Se as coordenadas estiverem fora das fronteiras da matriz.
     */
    public Celula getCelula(int l, int c) {
        if(l < 0 || l >= linhas) throw new IndexOutOfBoundsException("Posicao invalida.");
        if(c < 0 || c >= colunas) throw new IndexOutOfBoundsException("Posicao inválida");

        return grelha[l][c];
    }

    public int getLinhas() {return linhas;}
    public int getColunas() {return colunas;}

    /**
     * Insere um elemento na matriz utilizando as coordenadas internas do próprio objeto.
     *
     * @param e O elemento a posicionar na grelha.
     */
    void addElemento(Elemento e){
        grelha[e.getLin()][e.getCol()].setElemento(e);
    }

    /**
     * Procura aleatoriamente por uma célula vazia dentro de um perímetro delimitado.
     * <p>
     * Possui um limite de tentativas baseado na área otal definida,
     * prevenindo loops infinitos caso a zona esteja lotada.
     * </p>
     *
     * @param minL Limite mínimo da linha (inclusivo).
     * @param maxL Limite máximo da linha (exclusivo).
     * @param minC Limite mínimo da coluna (inclusivo).
     * @param maxC Limite máximo da coluna (exclusivo).
     * @return Array de inteiros com as coordenadas [linha, coluna] encontradas, ou null se não houver espaço disponível.
     */
    protected int[] encontrarEspacoVazio(int minL, int maxL, int minC, int maxC) {
        if (maxC <= minC || minL >= maxL) return null;
        int l, c, tentativas= 0;
        int maxTentativas = (maxC-minC) * (maxL-minL);

        Random rand = new Random();
        do {
            l = rand.nextInt(minL, maxL);
            c = rand.nextInt(minC, maxC);
            if (tentativas++ > maxTentativas) return null;
        } while (!celulaVazia(l, c));
        return new int[]{l, c};
    }

    /**
     * Efetua a limpeza seletiva da grelha.
     * <p>
     * Remove exclusivamente as entidades como 'Obstaculo' (monstros, correntes),
     * preservando a localização de recursos passivos (minérios, artefactos, rochas).
     * </p>
     */
    protected void limpar() {
        for (int i = 0; i < getLinhas(); i++) {
            for (int j = 0; j < getColunas(); j++) {
                if (getCelula(i, j).getElemento() instanceof Obstaculo) {
                    grelha[i][j].setElemento(null);
                }
            }
        }
    }

}
