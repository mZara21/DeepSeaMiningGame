package pt.isec.pa.deepsea.model.state.states;

import pt.isec.pa.deepsea.model.data.DeepSeaData;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.state.DeepSeaStateAdapter;

import java.io.Serial;

/**
 * Representa o estado de resolução do minijogo.
 * <p>
 * É ativado quando o drone tenta extrair um artefacto no fundo do mar. O jogador
 * tem de ordenar as peças numa grelha deslizante num número limite de movimentos.
 * A vitória garante o artefacto, já a derrota força o drone a regressar ao Fosso
 * </p>
 * @author Miguel Zara
 */

public class PuzzleState extends DeepSeaStateAdapter {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Construtor do estado do Puzzle.
     *
     * @param data Referência para os dados globais do jogo.
     * @param context Referência para o contexto da máquina de estados.
     */
    public PuzzleState(DeepSeaData data, DeepSeaContext context){ super(data,context); }

    /**
     * Tenta mover uma peça do puzzle para o espaço vazio adjacente.
     * <p>
     * Após um movimento válido, verifica imediatamente as condições de fim do minijogo:
     * se o puzzle ficou resolvido, aciona a vitória, se esgotou os movimentos, aciona a derrota.
     * </p>
     * @param lin A linha da peça que o jogador pretende mover.
     * @param col A coluna da peça que o jogador pretende mover.
     * @return true se o movimento era válido e foi executado, false se o movimento era inválido.
     */
    @Override
    public boolean moverPeca(int lin, int col){

        if (!data.moverPeca(lin, col)) return false;
        if (data.puzzleResolvido()) return ganharPuzzle();
        if (!data.puzzlTemMov()) return perderPuzzle();

        return true;
    }

    /**
     * Processa a vitória no minijogo.
     * <p>
     * Recolhe o artefacto para o porão do drone e devolve o controlo da expedição
     * para o estado de exploração do Fundo.
     * </p>
     * @return true indicando que a transição de estado ocorreu com sucesso,
     * false caso ocorra algum erro interno na perda.
     */
    private boolean ganharPuzzle() {
        if (!data.recolherArtefactoDoFundo()) return false;
        changeState(DeepSeaState.FUNDO);
        return true;
    }

    /**
     * Processa a derrota no minijogo por esgotamento de movimentos permitidos.
     * <p>
     * A recolha falha e o drone é forçado a voltar ao Fosso.
     * </p>
     * @return true se a penalização foi aplicada e o estado transitou para FOSSO,
     * false caso ocorra algum erro interno na perda.
     */
    private boolean perderPuzzle(){
        if (!data.perderPuzzle()) return false;
        changeState(DeepSeaState.FOSSO);
        return true;
    }

    /**
     * Devolve a identificação do estado atual.
     * @return O enum DeepSeaState.PUZZLE correspondente a este estado.
     */
    @Override
    public DeepSeaState getState(){
        return DeepSeaState.PUZZLE;
    }

}
