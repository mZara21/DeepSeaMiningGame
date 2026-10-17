package pt.isec.pa.deepsea.ui;

import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.isec.pa.deepsea.model.DeepSeaManager;
/**
 * Janela secundária dedicada em exclusivo à exibição dos logs do sistema,
 * através da classe LogPane.
 * @author Guilherme Eça
 * */
public class LogWindow extends Stage {
    private LogPane logs;

    public LogWindow(DeepSeaManager manager) {
        logs = new LogPane(manager);
        this.setTitle("Deep Sea Mining - Logs");
        createViews();
    }

    private void createViews(){
        this.setScene(new Scene(logs, 400, 300));
    }
}