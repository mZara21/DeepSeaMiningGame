package pt.isec.pa.deepsea.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.data.Constantes;
import pt.isec.pa.deepsea.ui.res.SoundManager;

/**
 * Painel lateral esquerdo responsável por exibir as estatísticas em tempo real.
 * <p>
 * Atua como um painelno layout global do jogo.
 * Apresenta informações vitais sobre o estado do navio, incluindo:
 * </p>
 * <ul>
 *   <li>O estado ou fase atual da máquina de estados.</li>
 *   <li>Uma barra de progresso visual com a percentagem de combustível restante.</li>
 *   <li>Contadores de drones disponíveis, minérios extraídos e artefactos recolhidos.</li>
 *   <li>O indicador visual do estado do Modo de Defesa (Ativo/Inativo).</li>
 *   <li>Um botão de controlo para o utilizador desistir do jogo.</li>
 * </ul>
 * <p>
 * Reage de forma centralizada a múltiplos eventos do modelo
 * garantindo que as informações apresentadas no ecrã estão sempre sincronizadas com o modelo do jogo.
 * </p>
 *
 * @author Guilherme Eça
 */
public class LeftPanelStats extends VBox {

    private final DeepSeaManager manager;


    private Label lblHeaderTitle, lblPhase;
    private Label lblFuelText, lblDrones, lblArtifacts, lblMinerals;
    private ProgressBar pbFuel;
    private Button btnDesistir;
    private CheckBox cbSound;

    private VBox defenseBox;
    private Label lblDefenseStatus;

    public LeftPanelStats(DeepSeaManager m){
        this.manager = m;

        setupMainPanel();
        createViews();
        registerHandles();
        update();
    }

    private void setupMainPanel() {
        this.getStyleClass().add("painel-base");
        this.setSpacing(15);
        this.setPrefWidth(220);
    }

    private void createViews() {
        // CABECALHO
        VBox headerBox = new VBox(4);
        headerBox.getStyleClass().add("sub-painel-box");

        lblHeaderTitle = new Label("Deep Sea Mining");
        lblHeaderTitle.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-font-size: 16px; -fx-margin: 0px");

        lblPhase = new Label(""); // escrito no update
        lblPhase.getStyleClass().add("sub-titulo-painel");

        headerBox.getChildren().addAll(lblHeaderTitle, lblPhase);

        // STATS NAVIO
        VBox statusBox = new VBox(8);
        statusBox.getStyleClass().add("sub-painel-box");

        Label lblStatusTitle = new Label("STATUS DO NAVIO");
        lblStatusTitle.getStyleClass().add("sub-titulo-painel");

        lblFuelText = createStatLabel("Combustível: 0%");

        // Barra de progresso
        pbFuel = new ProgressBar(1.0);
        pbFuel.setMaxWidth(Double.MAX_VALUE);
        pbFuel.getStyleClass().add("fuel-bar");

        lblDrones = createStatLabel("Drones Disponíveis: 3/3");
        lblArtifacts = createStatLabel("Artefactos: 0 / 0");
        lblMinerals = createStatLabel("Minérios: 0");

        statusBox.getChildren().addAll(lblStatusTitle, lblFuelText, pbFuel, lblDrones, lblArtifacts, lblMinerals);

        //  MODO DEFESA
        defenseBox = new VBox();
        defenseBox.setPadding(new Insets(10));
        defenseBox.setAlignment(Pos.CENTER);

        lblDefenseStatus = new Label();
        defenseBox.getChildren().add(lblDefenseStatus);

        // DESISTIR
        btnDesistir = new Button("Desistir");
        btnDesistir.getStyleClass().add("desistir-btn");

        cbSound = new CheckBox(" Sons");
        cbSound.getStyleClass().add("sound-toggle");

        cbSound.selectedProperty().bindBidirectional(
                SoundManager.getInstance().soundEnabledProperty()
        );

        this.getChildren().addAll(headerBox, statusBox, defenseBox, cbSound, btnDesistir);
    }

    private void registerHandles(){
        manager.addPropertyChangeListener(DeepSeaManager.PROP_MOVE, e -> Platform.runLater(this::update));
        manager.addPropertyChangeListener(DeepSeaManager.PROP_DRONE, e -> Platform.runLater(this::update));
        manager.addPropertyChangeListener(DeepSeaManager.PROP_STATE, e -> Platform.runLater(this::update));
        btnDesistir.setOnAction(e -> {
            manager.desistir();
        });
    }

    private void update(){
        // STATUS NAVIO
        double fuelAtual = manager.getNavioCombustivel();
        double fuelMax = Constantes.COMBUSTIVEL_NAVIO;
        double pctFuel = fuelAtual / fuelMax;

        lblFuelText.setText(String.format("Combustível: %.0f%%", pctFuel * 100));
        pbFuel.setProgress(pctFuel);

        lblArtifacts.setText(String.format("Artefactos: %d / %d", manager.getArtefactosRecolhidosCount(), Constantes.N_ARTEFACTOS));
        lblMinerals.setText(String.format("Minérios: %d", manager.getMinerios()));

        if (manager.getState() != null) {
            lblPhase.setText(manager.getState().toString().replace("_"," "));
        }

        // MODO DEFESA
        defenseBox.getStyleClass().removeAll("defesa-ativo", "defesa-inativo");
        lblDefenseStatus.getStyleClass().removeAll("defesa-ativo", "defesa-inativo");

        if (Constantes.MODO_DEFESA) {
            defenseBox.getStyleClass().add("defesa-ativo");
            lblDefenseStatus.getStyleClass().add("defesa-ativo-txt");
            lblDefenseStatus.setText("Modo de Defesa: Ativo");
        } else {
            defenseBox.getStyleClass().add("defesa-inativo");
            lblDefenseStatus.getStyleClass().add("defesa-inativo-txt");
            lblDefenseStatus.setText("Modo de Defesa: Inativo");
        }
    }

    private Label createStatLabel(String text) {
        Label l = new Label(text);
        l.getStyleClass().add("stat-label");
        return l;
    }
}