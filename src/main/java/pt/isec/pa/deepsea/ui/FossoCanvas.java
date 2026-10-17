package pt.isec.pa.deepsea.ui;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.DroneRec;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.ui.res.ImageLoader;
import pt.isec.pa.deepsea.ui.res.SoundManager;

/**
 * Representa o ecrã da fase de descida e subida do drone no fosso marinho.
 * <p>
 * Apresenta a grelha vertical onde o drone se movimenta para evitar rochas,
 * animais marinhos e correntes. Gere os inputs do teclado (W, A, S, D)
 * para controlar a navegação do drone nesta fase.
 * </p>
 * @author Miguel Zara
 */

public class FossoCanvas extends Canvas {
    private DeepSeaManager manager;

    public FossoCanvas(DeepSeaManager m){
        super(0, 0);
        manager = m;

        this.setFocusTraversable(true);
        this.setManaged(false);

        registerHandlers();
        update();
    }

    private void registerHandlers() {
        manager.addPropertyChangeListener(DeepSeaManager.PROP_MOVE, e -> {
            Platform.runLater(() -> {
                update();
                if(isVisible())
                    handleSom();
            });
        });

        manager.addPropertyChangeListener(DeepSeaManager.PROP_STATE,e -> {
            Platform.runLater( ()-> {
                update();
                if(isVisible())
                    SoundManager.getInstance().play(SoundEffect.TRANSITION);
            });

        });

        manager.addPropertyChangeListener(DeepSeaManager.PROP_EXPLOSAO, e -> {
            Platform.runLater(() -> {
                    SoundManager.getInstance().play(SoundEffect.EXPLOSAO);
            });
        });

        this.setOnMouseClicked(e -> this.requestFocus());

        setOnKeyPressed(ev -> {
            switch (ev.getCode()) {
                case W -> manager.mover("CIMA");
                case S -> manager.mover("BAIXO");
                case A -> manager.mover("ESQ");
                case D -> manager.mover("DIR");
            }
        });
    }
    private void update() {


        DeepSeaState state = manager.getState();

        if(state != DeepSeaState.FOSSO){
            this.setVisible(false);
            return;
        }

        this.setVisible(true);
        this.requestFocus();

        GraphicsContext gc = this.getGraphicsContext2D();

        gc.drawImage(ImageLoader.getImage("fosso/fosso.png"), 0, 0, getWidth(), getHeight());

        int rows = manager.getFossoLin();
        int cols = manager.getFossoCol();

        // largura e altura independentes pa ocupar o canvas todo
        double tileWidth = getWidth() / cols;
        double tileHeight = getHeight() / rows;

        DroneRec drone = manager.getInfoDroneSelecionado();

        gc.setStroke(Color.web("#ffffff", 0.1));
        gc.setLineWidth(1);

        for (int l = 0; l < rows; l++) {
            for (int c = 0; c < cols; c++) {
                double x = c * tileWidth;
                double y = l * tileHeight;

                // grelha
                gc.strokeRect(x, y, tileWidth, tileHeight);

                String el = manager.getFossoElemento(l,c);
                if (el != null) {
                    switch (el) {
                        case "ROCHA" ->
                                gc.drawImage(ImageLoader.getImage("fosso/rocha.png"), x, y, tileWidth, tileHeight);
                        case "ANIMAL" ->
                                gc.drawImage(ImageLoader.getImage("fosso/animal.png"), x, y, tileWidth, tileHeight);
                        case "CORRENTE" ->
                                gc.drawImage(ImageLoader.getImage("fosso/corrente.png"), x, y, tileWidth, tileHeight);
                    }
                }

                if (drone != null && drone.lin() == l && drone.col() == c) {
                    String imgName = "fosso/drone_fosso_nvl" + Math.max(drone.nivelCombustivel(), drone.nivelCasco()) + ".png";
                    gc.drawImage(ImageLoader.getImage(imgName), x, y, tileWidth, tileHeight);
                }
            }
        }
    }
    public void updateSize(double newWidth, double newHeight) {
        setWidth(newWidth);
        setHeight(newHeight);
        update();
    }

    /** Método que lança os sons */
    private void handleSom(){
        DroneRec drone = manager.getInfoDroneSelecionado();

        SoundManager.getInstance().play(SoundEffect.DRONE_MOVED);

        int l = drone.lin();
        int c = drone.col();
        String el = manager.getFossoElemento(l,c);


        switch(el){
            case "ANIMAL" -> SoundManager.getInstance().play(SoundEffect.CACHOLOTE);
            case "CORRENTE" -> SoundManager.getInstance().play(SoundEffect.CORRENTE);
        }
    }
}
