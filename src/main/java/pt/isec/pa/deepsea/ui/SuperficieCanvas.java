package pt.isec.pa.deepsea.ui;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.ui.res.ImageLoader;
import pt.isec.pa.deepsea.ui.res.SoundManager;

/**
 * Representa o ecrã da superficie do oceano do jogo.
 * @author Guilherme Eça
 */
public class SuperficieCanvas extends Canvas {
    private final DeepSeaManager manager;

    public SuperficieCanvas(DeepSeaManager m){
        super(0,0);
        manager=m;

        this.setFocusTraversable(true); // Permite que o Canvas receba o foco do teclado
        this.setManaged(false);


        registerHandles();
        update();
    }

    private void registerHandles(){
        manager.addPropertyChangeListener(DeepSeaManager.PROP_MOVE,e -> {
            Platform.runLater( ()-> update());
        });
        manager.addPropertyChangeListener(DeepSeaManager.PROP_STATE,e -> {
            Platform.runLater( ()-> {
                update();
            });
        });

        this.setOnMouseClicked(e -> this.requestFocus());

        setOnKeyPressed(ev -> {
            boolean moveu = false;
            switch (ev.getCode()){
                case W -> moveu = manager.mover("CIMA");
                case S -> moveu =manager.mover("BAIXO");
                case A -> moveu =manager.mover("ESQ");
                case D -> moveu =manager.mover("DIR");
            }

            if (moveu) {
                SoundManager.getInstance().play(SoundEffect.SHIP_MOVED);
            }
        });
    }
    private void update() {

        DeepSeaState state = manager.getState();

        if(state != DeepSeaState.SUPERFICIE){
            this.setVisible(false);
            return;
        }

        this.setVisible(true);
        this.requestFocus();

        GraphicsContext gc = this.getGraphicsContext2D();

        gc.drawImage(ImageLoader.getImage("superficie/oceano_superficie.png"), 0, 0, getWidth(), getHeight());

        int rows = manager.getSuperficieLin();
        int cols = manager.getSuperficieCol();

        double tileWidth = getWidth() / cols;
        double tileHeight = getHeight() / rows;

        gc.setStroke(Color.web("#ffffff", 0.1));
        gc.setLineWidth(1);

        for (int l = 0; l < rows; l++) {
            for (int c = 0; c < cols; c++) {
                double x = c * tileWidth;
                double y = l * tileHeight;

                // Desenhar a grelha
                gc.strokeRect(x, y, tileWidth, tileHeight);

                if (manager.temArtefacto(l, c)) {
                    gc.setFill(Color.web("#e85100",0.6));
                    gc.fillRect(x, y, tileWidth, tileHeight);
                }


                if (l == manager.getNavioLin() && c == manager.getNavioCol()) {
                    gc.drawImage(ImageLoader.getImage("superficie/navio.png"), x, y, tileWidth, tileHeight);
                }
            }
        }

       // System.out.println("[SuperficieCanvas - DEBUG] Update foi chamado.");
    }

    /**
     * Atualiza as dimensões do canvas de forma dinâmica.
     * @param newWidth  A nova largura a aplicar ao canvas.
     * @param newHeight A nova altura a aplicar ao canvas.
     */
    public void updateSize(double newWidth, double newHeight){
        setWidth(newWidth);
        setHeight(newHeight);
        update();
    }


}
