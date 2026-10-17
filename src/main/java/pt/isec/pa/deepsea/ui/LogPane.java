package pt.isec.pa.deepsea.ui;

import javafx.scene.control.ListView;
import pt.isec.pa.deepsea.model.DeepSeaLog;
import pt.isec.pa.deepsea.model.DeepSeaManager;

import java.util.List;

/**
 * Painel de visualização de logs do jogo.
 * <p>
 * Estende um ListView de Strings para apresentar as mensagens de log de forma
 * sequencial e vertical. A classe monitoriza as atualizações de log emitidas pelo
 * DeepSeaManager através de um PropertyChangeListener e, sempre que novas mensagens
 * são adicionadas, limpa a vista, recarrega o histórico completo do DeepSeaLog
 * e força um scroll automático para o último elemento inserido.
 * </p>
 *
 * @author Guilherme Eça
 */
public class LogPane extends ListView<String> {
    private DeepSeaManager manager;

    public LogPane(DeepSeaManager manager){
        this.manager = manager;
        registerHandlers();
        update();
    }

    private void registerHandlers(){
        manager.addPropertyChangeListener(DeepSeaManager.PROP_LOG,e -> update() );
    }
    private void update(){
        List<String> logs = DeepSeaLog.getInstance().getLogs();

        this.getItems().clear();
        this.getItems().addAll(logs);

        // SCROLL PARA O FUNDO
        // Se a lista não estiver vazia, faz scroll para o índice do último elemento
        if (!this.getItems().isEmpty()) {
            this.scrollTo(this.getItems().size() - 1);
        }
    }
}
