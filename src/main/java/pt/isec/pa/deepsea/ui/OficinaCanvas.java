package pt.isec.pa.deepsea.ui;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.DroneRec;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.ui.res.ImageLoader;
import pt.isec.pa.deepsea.ui.res.SoundManager;


/**
 * Representa o ambiente visual da Oficina do Navio.
 * <p>
 * Desenha o fundo da oficina e o modelo do drone atualmente selecionado,
 * refletindo visualmente os upgrades de casco aplicados.
 * </p>
 * @author Miguel Zara
 */
public class OficinaCanvas extends Canvas {
    private DeepSeaManager manager;

    public OficinaCanvas(DeepSeaManager m){
        super(0,0);
        manager = m;

        this.setManaged(false);
        registerHandles();
        update();
    }

    private void registerHandles(){
        manager.addPropertyChangeListener(DeepSeaManager.PROP_STATE, e -> {
            Platform.runLater( () -> update());
        });

        manager.addPropertyChangeListener(DeepSeaManager.PROP_DRONE, e -> {
            Platform.runLater( () -> update());
        });

    }

    private void update() {
        DeepSeaState state = manager.getState();

        if(state != DeepSeaState.OFICINA){
            this.setVisible(false);
            return;
        }

        this.setVisible(true);
        this.requestFocus();

        GraphicsContext gc = this.getGraphicsContext2D();
        double w = getWidth();
        double h = getHeight();

        gc.clearRect(0, 0, w, h);

        DroneRec drone = manager.getInfoDroneSelecionado();

        if (drone == null) {
            gc.drawImage(ImageLoader.getImage("oficina/oficina.png"), 0, 0, w, h);
            return;
        }

        String imgName = "oficina/oficina_nvl" + Math.max(drone.nivelCombustivel(), drone.nivelCasco()) + ".png";
        gc.drawImage(ImageLoader.getImage(imgName), 0, 0, w, h);
    }

    public void updateSize(double newWidth, double newHeight){
        setWidth(newWidth);
        setHeight(newHeight);
        update();
    }
}