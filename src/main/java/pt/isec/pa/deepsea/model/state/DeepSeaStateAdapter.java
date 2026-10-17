package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.data.DeepSeaData;
import java.io.Serial;
import java.io.Serializable;

/**
 * Implementacao base  para os estados do jogo.
 * @author Guilherme Eça, Miguel Zara e Luana Rabello
 */
abstract public class DeepSeaStateAdapter implements IDeepSeaState, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected final DeepSeaData data;
    protected final DeepSeaContext context;

    /** @param data Dados do jogo
     * @param context Contexto da FSM
     * */
    protected DeepSeaStateAdapter(DeepSeaData data, DeepSeaContext context){
        this.data = data;
        this.context = context;
    }

    /** @param newState Novo estado para transicao */
    protected void changeState(DeepSeaState newState){
        context.changeState(newState.getInstance(data, context));
    }
}