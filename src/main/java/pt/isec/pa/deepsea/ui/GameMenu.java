package pt.isec.pa.deepsea.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pt.isec.pa.deepsea.model.DeepSeaManager;

import java.io.File;
import java.util.List;

/**
 * Barra de menus superior (MenuBar) da aplicação.
 * <p>
 * Disponibiliza dois menus estruturais autónomos:
 * </p>
 * <ul>
 *   <li><b>Jogo:</b> Permite iniciar uma nova sessão, gravar/carregar o jogo atual
 *   ou aceder a ficheiros recentemente abertos.</li>
 *   <li><b>Log:</b> Controlo e a persistência  da
 *   janela de monitorização e depuração de eventos do jogo.</li>
 * </ul>
 */
public class GameMenu extends MenuBar {
    private DeepSeaManager manager;
    private LogWindow logWindow;
    private Menu menuLog ;
    private MenuItem showLogs;
    private MenuItem saveLogs;
    private MenuItem clearLogs;

    private Menu menuGame;
    private MenuItem newGame;
    private MenuItem openGame;
    private Menu menuOpenRecent;
    private MenuItem saveGame;
    private MenuItem exit;

    public GameMenu(DeepSeaManager manager, LogWindow logWindow){
        this.manager = manager;
        this.logWindow = logWindow;
        createViews();
        registerHandles();
        update();
    }

    private void createViews(){

        menuGame = new Menu("_Jogo"); // O '_' permite usar Alt+G
        newGame = new MenuItem("Novo jogo");
        openGame = new MenuItem("Abrir jogo");
        menuOpenRecent = new Menu("Abrir jogos recentes");
        saveGame = new MenuItem("Salvar jogo");
        exit = new MenuItem("Sair");

        menuGame.getItems().addAll(newGame, new SeparatorMenuItem(), openGame, menuOpenRecent, saveGame, new SeparatorMenuItem(), exit);

        menuLog = new Menu("_Log");
        showLogs = new MenuItem("Mostrar/Esconder");
        saveLogs = new MenuItem("Salvar logs");
        clearLogs = new MenuItem("Limpar");

        menuLog.getItems().addAll(showLogs, saveLogs, clearLogs);


        this.getMenus().addAll(menuGame, menuLog);

        this.useSystemMenuBarProperty().set(false); // Garante que usa o nosso estilo e não o do OS
    }

    private void registerHandles(){
        manager.addPropertyChangeListener(DeepSeaManager.PROP_FILES, ev -> {
            updateFilesMenu();
        });

        showLogs.setOnAction(e -> {
            if (logWindow.isShowing())
                logWindow.hide();
            else
                logWindow.show();
        });
        saveLogs.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salvar logs");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Ficheiros de Texto (*.txt)", "*.txt")
            );
            File hFile = fileChooser.showSaveDialog(this.getScene().getWindow());
            if (hFile != null) {
                manager.exportarLogTexto(hFile);
            }
        });
        clearLogs.setOnAction(e -> {
            manager.limparLogTexto();
        });

        newGame.setOnAction(e -> {
            confirmarSubstituicaoJogo( () -> manager.novoJogo());

        });
        saveGame.setOnAction(e ->  {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salvar jogo");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("DeepSeaGame (*.bin)", "*.bin"),
                    new FileChooser.ExtensionFilter("All", "*.*")
            );
            File hFile = fileChooser.showSaveDialog(this.getScene().getWindow());
            if (hFile != null) {
                manager.saveGame(hFile);
            }
        });
        openGame.setOnAction(e -> confirmarSubstituicaoJogo(() -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Abrir jogo");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("DeepSeaGame (*.bin)", "*.bin"),
                    new FileChooser.ExtensionFilter("All", "*.*")
            );
            File hFile = fileChooser.showOpenDialog(this.getScene().getWindow());
            if (hFile != null) {
                manager.loadGame(hFile);
            }
        }));

        exit.setOnAction(e -> {
            // Em vez de fechar abruptamente, pede a janela para  fechar
            this.getScene().getWindow().fireEvent(
                    new javafx.stage.WindowEvent(this.getScene().getWindow(), javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST)
            );
        });



    }
    private void update(){
        updateFilesMenu();
    }

    // Interface funcional para a callback
    private interface IWindowFunction {
        void func();
    }

    /** Abre uma janela de confirmação para a substituição do jogo */
    private void confirmarSubstituicaoJogo(IWindowFunction func) {
        Stage stage = new Stage();
        stage.setTitle("Confirmação");
        stage.initModality(Modality.APPLICATION_MODAL); // Bloqueia a janela principal

        Label label = new Label("Um jogo já está a decorrer.\n" +
                "Deseja interromper e perder o progresso atual?");
        label.setStyle("-fx-text-alignment: center; -fx-padding: 20;");

        Button btnSim = new Button("Sim");
        Button btnGuardar = new Button("Guardar primeiro");
        Button btnNao = new Button("Não");

        btnSim.setOnAction(e -> {
            func.func();
            stage.close();
        });

        btnGuardar.setOnAction(e -> {
            saveGame.fire();
            func.func();
            stage.close();
        });
        btnNao.setOnAction(e -> stage.close());

        HBox rootBtn = new HBox(20, btnSim, btnGuardar, btnNao);
        rootBtn.setAlignment(Pos.CENTER);
        rootBtn.setStyle("-fx-padding: 10;");

        VBox root = new VBox(10, label, rootBtn);
        root.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(root, 350, 150));
        stage.showAndWait();
    }

    private void updateFilesMenu() {
        menuOpenRecent.getItems().clear();
        List<String> recents = manager.getRecentFiles();

        if (recents.isEmpty()) {
            MenuItem none = new MenuItem("Sem jogos recentes");
            none.setDisable(true);
            menuOpenRecent.getItems().add(none);
            return;
        }

        for (String path : recents) {
            File file = new File(path);
            MenuItem mi = new MenuItem(file.getName());
            mi.setOnAction(e -> manager.loadGame(file));
            menuOpenRecent.getItems().add(mi);
        }
    }

}