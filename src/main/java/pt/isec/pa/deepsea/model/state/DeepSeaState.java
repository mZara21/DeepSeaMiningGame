package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.data.DeepSeaData;
import pt.isec.pa.deepsea.model.state.states.*;

/**
 * Enumeracao dos estados possiveis do jogo.
 * @author Guilherme Eça, Miguel Zara e Luana Rabello
 */
public enum DeepSeaState {
    SUPERFICIE, OFICINA, FOSSO, FUNDO, PUZZLE, FIM_DE_JOGO;

    /** @return Instancia do estado correspondente. */
    IDeepSeaState getInstance(DeepSeaData data, DeepSeaContext context) {
        return switch (this) {
            case SUPERFICIE  -> new SuperficieState(data, context);
            case OFICINA     -> new OficinaState(data, context);
            case FOSSO       -> new FossoState(data, context);
            case FUNDO       -> new FundoState(data, context);
            case PUZZLE      -> new PuzzleState(data, context);
            case FIM_DE_JOGO -> new FimDeJogoState(data, context);
        };
    }
}