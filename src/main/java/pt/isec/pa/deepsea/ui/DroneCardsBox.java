package pt.isec.pa.deepsea.ui;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.DroneRec;
import pt.isec.pa.deepsea.ui.res.SoundManager;

import java.util.List;
import java.util.Objects;

/**
 * Componente visual que lista a frota de drones do navio.
 * <p>
 * Apresenta o estado em tempo real (combustível, integridade e perdas)
 * de cada drone em formato de cartões interativos, permitindo a sua
 * seleção através do clique.
 * </p>
 */

class DroneCardsBox extends VBox {
    private DeepSeaManager manager;

    DroneCardsBox(DeepSeaManager manager) {
        this.manager = manager;
        this.setSpacing(10);
        this.setAlignment(Pos.TOP_CENTER);

        registerHandles();
        update();
    }

    private void registerHandles() {
        manager.addPropertyChangeListener(DeepSeaManager.PROP_DRONE, e -> {
            Platform.runLater( () -> update());
        });
    }

    private void update() {
        this.getChildren().clear();

        List<DroneRec> frota = manager.getFrotaInfo();
        DroneRec selecionado = manager.getInfoDroneSelecionado();

        if (frota == null) return;

        for (DroneRec d : frota) {
            String texto = String.format("ID: %s\nCombustível: %.1f%%\nIntegridade: %.1f%%",
                    d.id(),
                    (d.combustivel() / d.combustivelMaximo()) * 100,
                    (d.integridade() / d.integridadeMaxima()) * 100
            );

            Button card = new Button(texto);
            card.setPrefWidth(180);
            card.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            card.getStyleClass().add("drone-card");

            if (d.isPerdido()) {
                card.getStyleClass().add("drone-card-perdido");
                card.setDisable(true);
            } else if (selecionado != null && Objects.equals(d.id(), selecionado.id())) {
                card.getStyleClass().add("drone-card-selecionado");
            } else {
                card.getStyleClass().add("drone-card-normal");
            }

            card.setOnAction(e -> {
                boolean sucesso = manager.selecionaDrone(d.id());
                if(sucesso)
                    SoundManager.getInstance().play(SoundEffect.SELECIONA_DRONE);
                else
                    SoundManager.getInstance().play(SoundEffect.ERRO_OFICINA);
            });

            this.getChildren().add(card);
        }
    }
}