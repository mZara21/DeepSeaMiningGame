package pt.isec.pa.deepsea.ui;

import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.ui.res.CSSManager;

/**
 * Painel principal da interface gráfica do jogo, baseado num BorderPane.
 * <p>
 * Organiza a disposição global da aplicação. Mantém fixos o menu superior
 * e a área de jogo central. Conforme o estado do jogo muda no modelo
 * este painel altera dinamicamente os painéis laterais
 * </p>
 *
 * @author Guilherme Eça, Miguel Zara, Luana Rabello
 */
public class RootPane extends BorderPane {
    private DeepSeaManager manager;
    private GameMenu gameMenu;
    private GameView gameView;
    private LogWindow logs;
    private LeftPanelStats leftPanel;
    private RightPanelSuperficie panelSuperficie;
    private RightPanelOficina panelOficina;
    private RightPanelFosso panelFosso;
    private RightPanelFundo panelFundo;

    public RootPane(DeepSeaManager manager){
        this.manager = manager;
        this.logs = new LogWindow(manager);

        createViews();
        registerHandles();
        update();
        CSSManager.applyCSS(this, "styles.css");
    }

    private void createViews(){
        gameMenu = new GameMenu(manager,logs);
        gameView = new GameView(manager);
        leftPanel = new LeftPanelStats(manager);
        panelSuperficie = new RightPanelSuperficie(manager);
        panelOficina = new RightPanelOficina(manager);
        panelFosso = new RightPanelFosso(manager);
        panelFundo = new RightPanelFundo(manager);


        setStyle("-fx-background-color: #0e2338;");

        setTop(gameMenu);
        setCenter(gameView);
    }
    private void registerHandles(){
        manager.addPropertyChangeListener(DeepSeaManager.PROP_STATE, e -> {
            Platform.runLater( () -> update());
        });
    }
    private void update(){
        switch (manager.getState()) {
            case SUPERFICIE -> {
                setLeft(leftPanel);
                setRight(panelSuperficie);
            }
            case OFICINA -> {
                setLeft(leftPanel);
                setRight(panelOficina);
            }
            case FOSSO -> {
                setLeft(leftPanel);
                setRight(panelFosso);
            }
            case FUNDO -> {
                setLeft(leftPanel);
                setRight(panelFundo);
            }
            case PUZZLE -> {
                setLeft(leftPanel);
                setRight(null);
            }
            default -> {
                setLeft(null);
                setRight(null);
            } // fim de jogo tambem
        }
    }
}
