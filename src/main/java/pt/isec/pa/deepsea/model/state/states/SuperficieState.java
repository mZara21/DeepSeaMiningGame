package pt.isec.pa.deepsea.model.state.states;

import pt.isec.pa.deepsea.model.DeepSeaLog;
import pt.isec.pa.deepsea.model.data.DeepSeaData;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.state.DeepSeaStateAdapter;

import java.io.Serial;

/**
 * Representa o estado de Superfície do jogo.
 * <p>
 * Neste estado, o jogador controla o navio,aceder à oficina ou
 * selecionar um drone e mergulhar no fosso para iniciar a exploração.
 * </p>
 *
 * @author Guilherme
 *
 */
public class SuperficieState extends DeepSeaStateAdapter {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Construtor do estado de Superfície.
     * @param data Referência para os dados do jogo.
     * @param context Referência para o contexto da FSM
     */
    public SuperficieState(DeepSeaData data, DeepSeaContext context) {
        super(data, context);
    }

    /**
     * Move o navio na superfície.
     * Após o movimento, verifica se as condições de fim de jogo foram atingidas.
     * Se sim, transita para o estado 'FIM DE JOGO'
     * @param direcao String que indica a direção (CIMA, BAIXO, ESQ, DIR).
     * @return true se o movimento foi bem-sucedido.
     */
    @Override
    public boolean mover(String direcao) {
        boolean res = data.moveNavio(direcao);
        if (data.terminou()) {
            changeState(DeepSeaState.FIM_DE_JOGO);
            return false;
        }
        return res;
    }

    /**
     * Faz a transicao para o estado de Oficina.
     * Garante que nenhum drone está selecionado antes de entrar na oficina.
     */
    @Override
    public boolean abrir() {
        data.desselecionarDrone();
        changeState(DeepSeaState.OFICINA);
        return true;
    }

    /**
     * Tenta iniciar o mergulho de um drone no fosso.
     * @return true se o drone conseguir mergulhar com sucesso.
     */
    @Override
    public boolean mergulhar() {
        if (data.mergulhar()) {
            changeState(DeepSeaState.FOSSO);
            return true;
        }
        return false;
    }

    /**
     * Seleciona um drone específico da frota para mergulhar.
     * @param droneId Identificador único do drone.
     * @return true se o drone existir.
     */
    @Override
    public boolean selecionaDrone(String droneId) {
        return data.selecionaDrone(droneId);
    }

    /**
     * Transita diretamente para o estado 'FIM DE JOGO'.
     */
    @Override
    public void desistir() {
        changeState(DeepSeaState.FIM_DE_JOGO);
    }

    /** @return O identificador do estado SUPERFICIE. */
    @Override
    public DeepSeaState getState() {
        return DeepSeaState.SUPERFICIE;
    }
}