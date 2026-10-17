package pt.isec.pa.deepsea.model.state.states;

import pt.isec.pa.deepsea.model.DeepSeaLog;
import pt.isec.pa.deepsea.model.data.DeepSeaData;
import pt.isec.pa.deepsea.model.data.Drone;
import pt.isec.pa.deepsea.model.TipoUpgrade;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.state.DeepSeaStateAdapter;

import java.io.Serial;

/**
 * Estado que gere a manutenção e melhoria da frota de drones.
 * <p>
 * Neste estado, o utilizador pode selecionar drones operacionais para reabastecer
 * combustível, reparar danos no casco ou aplicar melhorias tecnológicas permanentes
 * utilizando os minérios recolhidos nas expedições.
 * </p>
 *
 * @author Luana Rabello
 */
public class OficinaState extends DeepSeaStateAdapter {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Construtor do estado de Oficina.
     * @param data Referência para o motor de dados (Model).
     * @param context Referência para o contexto da FSM.
     */

    public OficinaState(DeepSeaData data, DeepSeaContext context){ super(data,context); }

    /**
     * Seleciona um drone da frota para manutenção, verificando a sua validade.
     * <p>
     * O drone deve existir na frota e não pode estar no estado de "Perdido" (destruído)
     * para ser operado na oficina.
     * </p>
     * @param droneId Identificador único do drone.
     * @return true se o drone foi selecionado com sucesso; false se não existir ou estiver destruído.
     */
    @Override
    public boolean selecionaDrone(String droneId) {
        Drone d = data.getNavio().getDronePorId(droneId);
        if (d == null) {
            DeepSeaLog.getInstance().log(String.format("O drone com o ID '%s' não existe na frota.", droneId));
            return false;
        }

        if (d.isPerdido()) {
            DeepSeaLog.getInstance().log(String.format("O drone '%s' está destruído e não pode ser operado na oficina.", droneId));
            return false;
        }

        if (data.selecionaDrone(droneId)) {
            // DeepSeaLog.getInstance().log(String.format("Drone %s selecionado com sucesso.", droneId));
            return true;
        }
        return false; // se for algum outro erro ou exceção
    }

    /**
     * Abastece o drone selecionado utilizando o combustível global do navio.
     * @return true se o drone foi abastecido; false se não houver drone selecionado.
     */
    @Override
    public boolean abastecer() {
        Drone d = data.getDroneSelecionado();
        if(d==null){
            DeepSeaLog.getInstance().log("Nenhum drone selecionado na oficina");
            return false;
        }
        data.abasteceDrone(d.getIdentificacao());
        DeepSeaLog.getInstance().log(String.format("Drone nº %s abastecido", d.getIdentificacao()));
        return true;
    }

    /**
     * Repara a integridade do casco do drone selecionado.
     * <p>
     * Consome uma quantidade predefinida de combustível do navio para restaurar
     * a integridade total.
     * </p>
     * @return true se a reparação foi bem-sucedida; false se combustível insuficiente ou sem drone.
     */
    @Override
    public boolean reparar() {
        Drone d = data.getDroneSelecionado();
        if(d == null){
            DeepSeaLog.getInstance().log("Nenhum drone selecionado na oficina");
            return false;
        }
        if(data.reparaDrone(d.getIdentificacao())) {
            DeepSeaLog.getInstance().log(String.format("Drone nº %s reparado com sucesso!", d.getIdentificacao()));
            return true;
        }
        DeepSeaLog.getInstance().log("Falha na reparação: Combustível insuficiente.");
        return false;
    }

    /**
     * Aplica melhorias tecnológicas permanentes ao drone selecionado.
     * @param melhoria Tipo de upgrade (1 para Tanque, 2 para Casco).
     * @return true se a melhoria foi aplicada; false se recursos insuficientes ou sem drone.
     */
    @Override
    public boolean melhorar(TipoUpgrade melhoria) {
        Drone d = data.getDroneSelecionado();
        if (d == null) {
            DeepSeaLog.getInstance().log("Nenhum drone selecionado.");
            return false;
        }

        if (data.melhorarDrone(d.getIdentificacao(), melhoria)) {
            DeepSeaLog.getInstance().log("Drone " + d.getIdentificacao() + " melhorado com sucesso!");
            return true;
        } else {
            DeepSeaLog.getInstance().log("Recursos insuficientes ou drone inválido.");
            return false;
        }
    }

    /**
     * Termina as operações de manutenção e regressa ao estado de Superfície.
     * <p>
     * Garante a limpeza da seleção de drone antes de transitar.
     * </p>
     * @return true indicando que a transição de estado ocorreu.
     */
    @Override
    public boolean fechar() {
        changeState(DeepSeaState.SUPERFICIE);
        DeepSeaLog.getInstance().log("Voltando à Superfície.");
        data.desselecionarDrone();
        return true;
    }

    /**
     * Transita diretamente para o estado 'FIM DE JOGO'.
     */
    @Override
    public void desistir() {
        changeState(DeepSeaState.FIM_DE_JOGO);
    }

    /** @return O identificador do estado OFICINA. */
    @Override
    public DeepSeaState getState(){
        return DeepSeaState.OFICINA;
    }
}
