package pt.isec.pa.deepsea.ui;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.data.Constantes;
import pt.isec.pa.deepsea.model.state.DeepSeaState;

/**
 * Representa o ecrã final do jogo (Vitória ou Derrota).
 * @author Guilherme Eça
 */
public class FimDeJogoCanvas extends Canvas {
    private DeepSeaManager manager;

    public FimDeJogoCanvas(DeepSeaManager m) {
        super(0,0);
        this.manager = m;

        this.setManaged(false);
        registerHandlers();
        update();
    }

    private void registerHandlers(){
        manager.addPropertyChangeListener(DeepSeaManager.PROP_STATE, ev -> Platform.runLater(
                () -> update())
        );
    }

    private void update() {

        DeepSeaState state = manager.getState();

        if(state != DeepSeaState.FIM_DE_JOGO){
            this.setVisible(false);
            return;
        }

        this.setVisible(true);
        this.requestFocus();

        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setFill(Color.rgb(10, 20, 40));
        gc.fillRect(0, 0, getWidth(), getHeight());

        gc.setTextAlign(TextAlignment.CENTER);

        if (manager.getArtefactosRecolhidosCount() == Constantes.N_ARTEFACTOS) {
            desenharVitoria(gc);
        } else {
            desenharDerrota(gc);
        }
    }

    /** Método que desenha os textos de vitoria */
    private void desenharVitoria(GraphicsContext gc) {

        gc.setFont(Font.font("Poppins", FontWeight.BOLD, 50));
        gc.setFill(Color.GOLD);
        gc.fillText("VITÓRIA!", getWidth() / 2, getHeight() / 2 - 50);


        gc.setFont(Font.font("Poppins", FontWeight.BOLD, 20));
        gc.setFill(Color.WHITE);
        gc.fillText("Parabéns! Recolheste todos os " + Constantes.N_ARTEFACTOS + " artefactos!",
                getWidth() / 2, getHeight() / 2 + 20);
    }

    /** Método que desenha os textos de derrota */
    private void desenharDerrota(GraphicsContext gc) {
        gc.setFont(Font.font("Poppins", FontWeight.BOLD, 50));
        gc.setFill(Color.RED);
        gc.fillText("FIM DE JOGO", getWidth() / 2, getHeight() / 2 - 50);

        gc.setFont(Font.font("Poppins", FontWeight.BOLD, 20));
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText("Exploração falhada. Artefactos recolhidos: " + manager.getArtefactosRecolhidosCount() + "/" + Constantes.N_ARTEFACTOS,
                getWidth() / 2, getHeight() / 2 + 20);
    }

    public void updateSize(double newWidth, double newHeight) {
        setWidth(newWidth);
        setHeight(newHeight);
        update();
    }
}