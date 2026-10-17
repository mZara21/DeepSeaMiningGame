package pt.isec.pa.deepsea.model.data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Random;

import static pt.isec.pa.deepsea.model.data.Constantes.*;

/**
 * Implementa a lógica e o estado interno do minijogo de extração de artefactos.
 * <p>
 * Gere a grelha numérica de peças, efetua o baralhamento inicial garantindo solução possível,
 * valida as regras de movimento (apenas peças adjacentes ao espaço vazio) e controla
 * as condições de vitória (ordenação correta) e derrota (limite de movimentos).
 * </p>
 *
 * @author Guilherme Eça e Miguel Zara
 */

public class Puzzle implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int[][] grelha;
    private int cont;

    private int vazioLinha;
    private int vazioColuna;

    /**
     * Construtor padrão do Puzzle.
     * Inicializa a grelha e executa o baralhamento automático.
     */
    Puzzle(){
        this.grelha = new int[DIMENSAO_PUZZLE][DIMENSAO_PUZZLE];
        inicializar();
    }

    /**
     * Configura o estado inicial do minijogo.
     * <p>
     * Preenche a grelha com os números ordenados (1 a 15) e o zero no fim (representando o buraco vazio).
     * De seguida, efetua um número predefinido de movimentos aleatórios válidos (SHUFFLES_PUZZLE)
     * para baralhar a grelha, garantindo que o puzzle gerado tem sempre resolução possível.
     * Reinicia o contador de jogadas para zero no final.
     * </p>
     */
    void inicializar() {
        Random rand = new Random();
        int k = 1;
        for (int i = 0; i < DIMENSAO_PUZZLE; i++){
            for (int j = 0; j < DIMENSAO_PUZZLE; j++){
                grelha[i][j] = k++;
            }
        }
        grelha[DIMENSAO_PUZZLE - 1][DIMENSAO_PUZZLE - 1] = 0;
        vazioLinha = DIMENSAO_PUZZLE - 1;
        vazioColuna = DIMENSAO_PUZZLE - 1;

        int movimentosRealizados = 0;
        while (movimentosRealizados < SHUFFLES_PUZZLE) {
            int dir = rand.nextInt(4); // 0 - Cima, 1 - Baixo, 2 - Esquerda, 3 - Direita
            int novaLinha = vazioLinha;
            int novaColuna = vazioColuna;

            switch (dir) {
                case 0 -> novaLinha--;
                case 1 -> novaLinha++;
                case 2 -> novaColuna--;
                case 3 -> novaColuna++;
            }

            if (moverPeca(novaLinha, novaColuna)) {
                movimentosRealizados++;
            }
        }

        this.cont = 0;
    }

    /**
     * Executa a tentativa de deslocamento de uma peça para o espaço vazio.
     * <p>
     * Verifica se a coordenada fornecida está dentro dos limites e se é estritamente
     * adjacente ao espaço vazio atual.
     * Se for válido, troca os valores na grelha e incrementa o contador de movimentos.
     * </p>
     *
     * @param lin A linha da peça a mover.
     * @param col A coluna da peça a mover.
     * @return true se a deslocação foi efetuada, false se o movimento é ilegal.
     */
    boolean moverPeca(int lin, int col){
        if (lin < 0  || lin >= DIMENSAO_PUZZLE || col < 0 || col >= DIMENSAO_PUZZLE)
            return false;

        int distL = Math.abs(lin - vazioLinha);
        int distC = Math.abs(col - vazioColuna);

        if (distC + distL == 1){
            grelha[vazioLinha][vazioColuna] = grelha[lin][col];
            grelha[lin][col] = 0;

            vazioColuna = col;
            vazioLinha = lin;

            cont++;
            return true;
        }
        return false;
    }

    /**
     * Verifica se o jogador ainda não excedeu o limite energético do artefacto.
     *
     * @return true se o número de movimentos efetuados for menor que MAX_MOVIMENTOS_PUZZLE.
     */
    public boolean temMovimentos() {
        return cont < MAX_MOVIMENTOS_PUZZLE;
    }


    /**
     * Analisa o estado atual da grelha para verificar a condição de vitória.
     * <p>
     * Percorre as células para confirmar se estão estritamente ordenadas de 1 a 15,
     * sendo o espaço vazio (0) obrigatoriamente a última célula do canto inferior direito.
     * </p>
     *
     * @return true se o puzzle está totalmente resolvido, false se houver alguma peça fora do sítio.
     */
    public boolean isResolvido() {
        int esperado = 1;
        for (int i = 0; i < DIMENSAO_PUZZLE; i++) {
            for (int j = 0; j < DIMENSAO_PUZZLE; j++) {
                if (i == DIMENSAO_PUZZLE - 1 && j == DIMENSAO_PUZZLE - 1)
                    return grelha[i][j] == 0;
                if (grelha[i][j] != esperado)
                    return false;
                esperado++;
            }
        }
        return true;
    }

    /**
     * Obtém a matriz de representação atual do puzzle.
     *
     * @return Array 2D de inteiros correspondendo ao estado atual do tabuleiro.
     */
    public int[][] getGrelha() {
        return grelha != null ? grelha : new int[0][0];
    }

    /**
     * Calcula a quantidade de movimentos válidos que o jogador ainda pode executar.
     *
     * @return Número de movimentos restantes.
     */
    int getMovimentosRestantes(){
        return MAX_MOVIMENTOS_PUZZLE - cont;
    }
}
