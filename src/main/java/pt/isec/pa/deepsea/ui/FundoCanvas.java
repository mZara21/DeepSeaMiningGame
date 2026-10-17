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
 * Representa o ecrã correspondente à fase de exploração do fundo.
 * @author Luana Rabello
 */
public class FundoCanvas extends Canvas {
    private final DeepSeaManager manager;

    public FundoCanvas(DeepSeaManager m){
        super(0,0);
        manager = m;

        this.setFocusTraversable(true); // Permite que o Canvas receba o foco do teclado
        this.setManaged(false);

        registerHandlers();
        update();
    }

    /**
     * Regista os "listeners" e eventos necessários para o painel.
     * Fica à escuta de movimentos, alterações no drone e capturas de teclas (W, A, S, D) para mover o drone.
     */
    private void registerHandlers(){
        manager.addPropertyChangeListener(DeepSeaManager.PROP_MOVE, e -> {
            Platform.runLater(() -> {
                update();
                if(isVisible())
                    handleSom();
            });
        });

        manager.addPropertyChangeListener(DeepSeaManager.PROP_DRONE, e -> {
            Platform.runLater( () -> update() );
        });

        manager.addPropertyChangeListener(DeepSeaManager.PROP_STATE,e -> {
            Platform.runLater( ()-> update() );
        });

        manager.addPropertyChangeListener(DeepSeaManager.PROP_EXPLOSAO, e -> {
            Platform.runLater(() -> {
                SoundManager.getInstance().play(SoundEffect.EXPLOSAO);
            });
        });

        manager.addPropertyChangeListener(DeepSeaManager.PROP_COLETA, e -> {
            Platform.runLater(() -> {
                if(isVisible()){
                    String el = (String) e.getNewValue();
                    if(el.equalsIgnoreCase("MINERIO"))
                        SoundManager.getInstance().play(SoundEffect.MINERIO);
                }
            });
        });


        // pra desenhar quando ficar as celulas ficarem visíveis
        this.visibleProperty().addListener(e -> {
            update();
        });

        this.setOnMouseClicked(e -> this.requestFocus());

        setOnKeyPressed(ev -> {
            switch (ev.getCode()){
                case W -> manager.mover("CIMA");
                case S -> manager.mover("BAIXO");
                case A -> manager.mover("ESQ");
                case D -> manager.mover("DIR");
            };
        });

    }

    /**
     * Atualiza o conteúdo gráfico do canvas.
     * Limpa o ecrã, desenha a grelha do fundo do mar e revela as células por onde o drone passou,
     * desenhando os respetivos elementos gráficos.
     */
    private void update(){


        DeepSeaState state = manager.getState();

        if(state != DeepSeaState.FUNDO){
            this.setVisible(false);
            return;
        }

        this.setVisible(true);
        this.requestFocus();

        GraphicsContext gc = this.getGraphicsContext2D();
        DroneRec drone = manager.getInfoDroneSelecionado();

        // pinta o fundo do mar com azul escuro
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setFill(Color.web("#0a1931"));
        gc.fillRect(0, 0, getWidth(), getHeight());

        int rows = manager.getFundoLin();
        int cols = manager.getFundoCol();

        if (rows == 0 || cols == 0) return;

        double tileWidth = getWidth() / cols;
        double tileHeight = getHeight() / rows;

        for (int l = 0; l < rows; l++) {
            for (int c = 0; c < cols; c++) {
                double x = c * tileWidth;
                double y = l * tileHeight;

                // Desenhar a grelha
                gc.setStroke(Color.web("#ffffff", 0.1));
                gc.setLineWidth(1);
                gc.strokeRect(x, y, tileWidth, tileHeight);

                // verifica se a celula é revelada, se sim mostra elemento, se nao fica escura
                if (!manager.isFundoRevelado(l, c)) {
                    gc.setFill(Color.web("#000000", 0.7));
                    gc.fillRect(x, y, tileWidth, tileHeight);
                    continue;
                }

                String el = manager.getFundoElemento(l, c);
                if (el != null) {
                    switch (el) {
                        case "MONSTRO" -> gc.drawImage(ImageLoader.getImage("fundo/monstro.png"), x, y, tileWidth, tileHeight);
                        case "ARTEFACTO" -> gc.drawImage(ImageLoader.getImage("fundo/artefacto.png"), x, y, tileWidth, tileHeight);
                        case "MINERIO" -> gc.drawImage(ImageLoader.getImage("fundo/minerio.png"), x, y, tileWidth, tileHeight);
                    }
                }

                // desenha o drone
                if (drone != null && drone.lin() == l && drone.col() == c) {
                    String imgName = "fundo/drone_fundo_nvl" + Math.max(drone.nivelCombustivel(), drone.nivelCasco()) + ".png";
                    gc.drawImage(ImageLoader.getImage(imgName), x, y, tileWidth, tileHeight);
                }
            }
        }
    }

    /**
     * Atualiza as dimensões do canvas de forma dinâmica.
     * @param newWidth  A nova largura a aplicar ao canvas.
     * @param newHeight A nova altura a aplicar ao canvas.
     */

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
        String el = manager.getFundoElemento(l,c);

        if (el == null) return;
        if(el.equals("MONSTRO"))
            SoundManager.getInstance().play(SoundEffect.MONSTRO);
    }
}
