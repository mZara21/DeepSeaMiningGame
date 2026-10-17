package pt.isec.pa.deepsea.ui;

import javafx.application.Platform;
import javafx.scene.control.Button;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.DroneRec;


/**
 * Painel lateral direito utilizado na fase de Superfície do oceano.
 * <p>
 * Disponibiliza as ações principais disponíveis quando o navio está a navegar,
 * permitindo ao jogador abrir a Oficina ou iniciar um mergulho com o drone selecionado.
 * </p>
 */
public class RightPanelSuperficie extends RightPanel {
    private Button btnOficina;
    private Button btnMergulhar;

    public RightPanelSuperficie(DeepSeaManager m) {
        super(m);

        createViews();
        registerHandles();
        update();
    }

    private void createViews() {
        DroneCardsBox droneCardsBox = new DroneCardsBox(manager);

        btnMergulhar = new Button("MERGULHAR");
        btnMergulhar.getStyleClass().addAll("botao-base","botao-destaque");

        btnOficina = new Button("ABRIR OFICINA");
        btnOficina.getStyleClass().addAll("botao-base","botao-destaque");

        this.getChildren().addAll(droneCardsBox, btnOficina,btnMergulhar);
    }

    private void registerHandles() {
        // Registamos o listener. Quando o drone muda, fazemos update.
        manager.addPropertyChangeListener(DeepSeaManager.PROP_DRONE, e -> {
            Platform.runLater(this::update);
        });


        btnMergulhar.setOnAction(e -> manager.mergulhar());

        btnOficina.setOnAction(e -> manager.abrir());
    }

    private void update() {
        DroneRec selecionado = manager.getInfoDroneSelecionado();
        btnMergulhar.setDisable(selecionado == null);
    }
}