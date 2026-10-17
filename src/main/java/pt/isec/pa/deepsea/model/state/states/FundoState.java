package pt.isec.pa.deepsea.model.state.states;

import pt.isec.pa.deepsea.model.DeepSeaLog;
import pt.isec.pa.deepsea.model.data.DeepSeaData;
import pt.isec.pa.deepsea.model.data.Drone;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.state.DeepSeaStateAdapter;

import java.io.Serial;

/**
 * Representa o estado de Fundo do jogo.
 * <p>
 *     Neste estado, o jogador explora o Fundo do marcorrespondente
 *     a uma célula e pode recolher artefactos e minérios.
 * </p>
 *
 * @author Luana
 */

public class FundoState extends DeepSeaStateAdapter {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Construtor do estado de Fundo
     * @param data Referência para os dados do jogo.
     * @param context Referência para o contexto da FSM
     */
    public FundoState(DeepSeaData data, DeepSeaContext context){ super(data,context); }

    /**
     * Move o navio no fundo.
     * Caso o jogador encontre um artefacto, o Puzzle é iniciado
     * para recuperar o objeto.
     * @param direcao String que indica a direção (CIMA, BAIXO, ESQ, DIR).
     * @return true se o movimento foi bem-sucedido.
     */
    @Override
    public boolean mover(String direcao) {
        if (direcao.equalsIgnoreCase("CIMA") && data.getDroneSelecionado().getLin() == 0) {
            return subir();
        }

        boolean move = data.moverDrone(direcao, true);
        if(data.isDronePerdido()){
            perderDrone();
            return false;
        }
        if(data.isDroneEmCimaArtefacto()){
            data.iniciarPuzzle();
            changeState(DeepSeaState.PUZZLE);
        }
        return move;
    }

    /**
     * Inicia a subida do Fundo para o Fosso.
     */
    @Override
    public boolean subir() {
        DeepSeaLog.getInstance().log("A iniciar subida para o Fosso.");
        data.subirDrone();
        changeState(DeepSeaState.FOSSO);
        return true;
    }

    /**
     * Faz a transição para o estado de Fosso caso a expedição falhe
     * e faz o processamento dos dados pela perda do drone.
     */
    private void perderDrone() {
        DeepSeaLog.getInstance().log("A expedição no Fundo do oceano falhou. Regresso imediato à Superfície.");
        data.processaPerdaDrone(); // reposiciona recursos e deixa celulas escuras
        if (data.terminou()) {
            changeState(DeepSeaState.FIM_DE_JOGO);
        } else {
            changeState(DeepSeaState.FOSSO);
        }
    }

    /**
     * Faz a transição para o estado de Puzzle.
     * @return true se foi possível iniciar o Puzzle.
     */
    @Override
    public boolean abrir() {
        if(data.iniciarPuzzle()){
            changeState(DeepSeaState.PUZZLE);
            return true;
        }
        return false;
    }

    /**
     * Transita diretamente para o estado 'FIM DE JOGO'.
     */
    @Override
    public void desistir() {
        changeState(DeepSeaState.FIM_DE_JOGO);
    }

    @Override
    public DeepSeaState getState(){
        return DeepSeaState.FUNDO;
    }


}
