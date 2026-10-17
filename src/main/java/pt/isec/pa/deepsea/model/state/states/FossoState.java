package pt.isec.pa.deepsea.model.state.states;

import pt.isec.pa.deepsea.model.data.DeepSeaData;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.state.DeepSeaStateAdapter;

import java.io.Serial;

/**
 * Representa o estado de descida e subida através do Fosso marinho.
 * <p>
 * Durante este estado, o drone navega verticalmente para chegar ao fundo do mar
 * ou para regressar à superfície. O jogador deve evitar rochas e correntes, e
 * suportar possíveis danos provocados por animais marinhos.
 * </p>
 *
 * @author Miguel Zara
 *
 */

public class FossoState extends DeepSeaStateAdapter {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Construtor do estado do Fosso.
     *
     * @param data Referência para os dados globais do jogo.
     * @param context Referência para o contexto da máquina de estados.
     */
    public FossoState(DeepSeaData data, DeepSeaContext context){ super(data,context); }


    /**
     * Tenta movimentar o drone dentro do Fosso.
     * <p>
     * Se o drone estiver na saída do fosso (linha 0) e a direção for "CIMA",
     * o movimento é intercetado e aciona a transição para a Superfície.
     * Se estiver no fundo do fosso (última linha) e a direção for "BAIXO",
     * aciona a transição para o Fundo.
     * Após qualquer movimento, verifica se o drone foi destruído.
     * </p>
     * @param dir A direção pretendida ("CIMA", "BAIXO", "ESQ", "DIR").
     * @return true se o drone se moveu ou transitou de estado com sucesso, false se o movimento foi bloqueado.
     */
    @Override
    public boolean mover(String dir) {
        if (dir.equalsIgnoreCase("CIMA") && data.isDroneNaSaidaFosso()) return subirParaSuperficie();
        if (dir.equalsIgnoreCase("BAIXO") && data.isDroneNoFundoDoFosso()) return descerParaFundo();

        boolean moveu = data.moverDrone(dir, false);
        if (data.isDronePerdido()) return perderDrone();
        return moveu;
    }

    /**
     * Efetua a transição do Fosso para o Fundo.
     * @return true se o drone estava na posição correta (fundo do fosso) e transitou com sucesso, false caso contrário.
     */
    private boolean descerParaFundo(){
        if (!data.isDroneNoFundoDoFosso()) return false;
        data.descerDrone();
        changeState(DeepSeaState.FUNDO);
        return true;
    }

    /**
     * Efetua a transição do Fosso para a Superfície.
     * <p>
     * Ao subir com sucesso, todos os recursos (minérios e artefactos) recolhidos
     * durante a expedição são descarregados no Navio.
     * </p>
     * @return true se a subida foi bem sucedida.
     */
    private boolean subirParaSuperficie(){
        if (!data.isDroneNaSaidaFosso()) return false;

        data.recolheRecursos();
        // Ao descarregar os recursos, completámos a missão?
        if (data.terminou()) {
            changeState(DeepSeaState.FIM_DE_JOGO);
        } else {
            changeState(DeepSeaState.SUPERFICIE);
        }
        return true;
    }

    /**
     * Processa a perda do drone por falta de combustível ou destruição do casco.
     * <p>
     * Rregressa imediatamente à Superfície, assinalando o drone atual como perdido e
     * forçando o abandono da carga no fundo do mar.
     * </p>
     * @return true indicando que o processo de perda foi executado e o estado foi alterado.
     */
    private boolean perderDrone() {
        data.processaPerdaDrone();
        if (data.terminou()) {
            changeState(DeepSeaState.FIM_DE_JOGO);
        } else {
            changeState(DeepSeaState.SUPERFICIE);
        }
        return true;
    }

    /**
     * Transita diretamente para o estado 'FIM DE JOGO'.
     */
    @Override
    public void desistir() {
        changeState(DeepSeaState.FIM_DE_JOGO);
    }

    /**
     * Devolve a identificação do estado atual.
     *
     * @return O enum DeepSeaState.FOSSO correspondente a este estado.
     */
    @Override
    public DeepSeaState getState(){
        return DeepSeaState.FOSSO;
    }
}
