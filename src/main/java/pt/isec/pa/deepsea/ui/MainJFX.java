package pt.isec.pa.deepsea.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pt.isec.pa.deepsea.model.DeepSeaManager;

/**
 * Classe principal da interface gráfica JavaFX para o jogo Deep Sea Mining.
 * Governa o ciclo de vida da aplicação gráfica, a instanciação do motor de jogo
 * e a gestão das janelas de visualização do utilizador.
 */
public class MainJFX extends Application {
    /**
     * Referência central para a Facade do modelo de dados do jogo.
     * Partilhada entre todas as janelas geradas para garantir a sincronização em tempo real.
     */
    private DeepSeaManager manager;

    @Override
    public void init(){
        manager = new DeepSeaManager();
    }

    @Override
    public void start(Stage stage) throws Exception {
        criaNovaJanela(stage);
        // criaNovaJanela(new Stage());

        stage.setOnCloseRequest( e -> {
            e.consume(); // para nao fechar imediatamente
            confirmarSair(Platform::exit);
        });
    }

    private interface IFunction{
        void func();
    }
    private void confirmarSair(IFunction func) {
        Stage stage = new Stage();
        stage.setTitle("Confirmação");
        stage.initModality(Modality.APPLICATION_MODAL); // Bloqueia a janela principal

        Text text = new Text("Deseja mesmo sair?\nQualquer progresso que não foi guardado será perdido.");

        Button btnSim = new Button("Sim");
        Button btnNao = new Button("Não");

        btnSim.setOnAction(e -> {
            func.func();
            stage.close();
        });

        btnNao.setOnAction(e -> stage.close());

        HBox rootBtn = new HBox(10, btnSim, btnNao);
        rootBtn.setAlignment(Pos.CENTER);

        VBox root = new VBox(10, text, rootBtn);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 10;");

        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    private void criaNovaJanela(Stage stage){
        RootPane root2 = new RootPane(manager);
        Scene scene2 = new Scene(root2,1000,550);

        stage.setScene(scene2);
        stage.setTitle("Deep Sea Mining");

        stage.setMinWidth(900);
        stage.setMinHeight(550);
        stage.show();
    }
}
