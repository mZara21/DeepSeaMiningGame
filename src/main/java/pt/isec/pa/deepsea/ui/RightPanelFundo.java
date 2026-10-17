package pt.isec.pa.deepsea.ui;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.DroneRec;
import pt.isec.pa.deepsea.ui.res.ImageLoader;

/**
 * Painel lateral direito utilizado na fase de exploração no Fundo do mar.
 * <p>
 * Semelhante ao painel do Fosso, apresenta as estatísticas e carga do drone
 * em tempo real, mas inclui o botão de ação para iniciar a subida
 * de regresso à superfície a qualquer momento.
 * </p>
 */
public class RightPanelFundo extends RightPanel {
    private Label lblCombustivel, lblIntegridade, lblMinerios, lblArtefactos;
    private ImageView imgDrone;
    private Button btnSubir;

    public RightPanelFundo(DeepSeaManager m){
        super(m);
        createViews();
        registerHandles();
        update();
    }

    private void createViews(){

        Label title = new Label("ESTADO DO MERGULHO");
        title.getStyleClass().add("titulo-painel");

        lblCombustivel = createStatLabel("Combustível");
        lblIntegridade = createStatLabel("Integridade");
        lblMinerios = createStatLabel("Minérios");
        lblArtefactos = createStatLabel("Artefactos");

        imgDrone = new ImageView();
        imgDrone.setFitHeight(80);
        imgDrone.setFitWidth(80);

        VBox vbox = new VBox(title, imgDrone, lblCombustivel, lblIntegridade, lblMinerios, lblArtefactos);
        vbox.getStyleClass().add("sub-painel-box");
        vbox.setSpacing(5);

        btnSubir = new Button("Subir");
        btnSubir.getStyleClass().addAll("botao-base","botao-destaque");

        this.getChildren().addAll(vbox, btnSubir);
    }

    private Label createStatLabel(String text) {
        Label l = new Label(text + ": --");
        l.getStyleClass().add("stat-label");
        return l;
    }

    private void registerHandles(){
        // atualiza quando o drone se move com sucesso
        manager.addPropertyChangeListener(DeepSeaManager.PROP_MOVE, e -> {
            Platform.runLater( () -> update());
        });

        // atualiza quando o drone sofre alterações
        manager.addPropertyChangeListener(DeepSeaManager.PROP_DRONE, e -> {
            Platform.runLater(() -> update());
        });

        // garante que os valores aparecem mal o painel é mostrado
        manager.addPropertyChangeListener(DeepSeaManager.PROP_STATE, e -> {
            Platform.runLater(() -> update());
        });

        btnSubir.setOnAction(e -> manager.subir());

    }

    private void update() {
        DroneRec drone = manager.getInfoDroneSelecionado();
        if (drone == null) return;

        lblMinerios.setText("Minérios: " + drone.nMinerios());
        lblArtefactos.setText("Artefactos: " + drone.nArtefactos());

        updateStatLabel(lblCombustivel, String.format("Combustível: %.1f%%",(drone.combustivel() / drone.combustivelMaximo()) * 100), (drone.combustivel() / drone.combustivelMaximo()) < 0.20);
        updateStatLabel(lblIntegridade, String.format("Integridade: %.1f%%", (drone.integridade() / drone.integridadeMaxima()) * 100), (drone.integridade() / drone.integridadeMaxima()) < 0.30);

        String imgName = "fosso/drone_fosso_nvl" + Math.max(drone.nivelCombustivel(), drone.nivelCasco()) + ".png";
        imgDrone.setImage(ImageLoader.getImage(imgName));
    }

    private void updateStatLabel(Label lbl, String text, boolean isCritical) {
        lbl.setText(text);
        lbl.getStyleClass().remove("stat-critical");
        if (isCritical) {
            lbl.getStyleClass().add("stat-critical");
        }
    }

}
