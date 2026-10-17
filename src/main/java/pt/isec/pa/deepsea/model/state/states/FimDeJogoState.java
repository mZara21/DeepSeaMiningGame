package pt.isec.pa.deepsea.model.state.states;

import pt.isec.pa.deepsea.model.DeepSeaLog;
import pt.isec.pa.deepsea.model.data.DeepSeaData;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.state.DeepSeaStateAdapter;

import java.io.Serial;

/**
 * Representa o estado final do jogo.
 * <p>
 * Este estado é atingido quando as condições de vitória ou derrota
 * são cumpridas. Responsavel por processar e exibir o resultado final.
 * </p>
 * @author Guilherme Eça
 * @version 1.0
 */
public class FimDeJogoState extends DeepSeaStateAdapter {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Construtor do estado Fim de Jogo.
     * Executa automaticamente o processamento do resultado ao ser instanciado.
     * @param data Referência para os dados do jogo.
     * @param context Referência para o contexto da máquina de estados.
     */
    public FimDeJogoState(DeepSeaData data, DeepSeaContext context) {
        super(data, context);
        processaResultado();
    }

    /**
     * Analisa os dados finais e regista a mensagem de vitória ou derrota
     * no sistema de logs do jogo.
     */
    private void processaResultado() {
        if (data.fimDeJogo()) {
            DeepSeaLog.getInstance().log("Parabéns! Missão cumprida com sucesso.");
        } else {
            DeepSeaLog.getInstance().log("O jogo terminou. A frota ou o navio ficaram sem recursos.");
        }
    }

    /** @return O identificador do estado FIM DE JOGO. */
    @Override
    public DeepSeaState getState() {
        return DeepSeaState.FIM_DE_JOGO;
    }
}