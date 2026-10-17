package pt.isec.pa.deepsea.ui;

import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.state.DeepSeaState;

/**
 * Área central de visualização do jogo que gere e sobrepõe os diferentes Canvas.
 * <p>
 * Baseado num StackPane, este componente empilha todos os canvas do jogo
 * (Superfície, Oficina, Fosso, Fundo, Puzzle e Fim de Jogo). Com base no estado atual do
 * modelo, controla a visibilidade de cada camada e gere o foco do teclado para garantir
 * que os inputs são corretamente direcionados para o cenário ativo.
 * </p>
 * <p>
 * Também monitoriza as alterações de tamanho do próprio painel para redimensionar
 * proporcionalmente todos os canvas em tempo real.
 * </p>
 *
 * @author Guilherme Eça, Miguel Zara, Luana Rabello
 */
public class GameView extends StackPane {
    private DeepSeaManager manager;
    private SuperficieCanvas superficieCanvas;
    private OficinaCanvas oficinaCanvas;
    private FossoCanvas fossoCanvas;
    private FundoCanvas fundoCanvas;
    private PuzzleCanvas puzzleCanvas;
    private FimDeJogoCanvas fimDeJogoCanvas;


    public GameView(DeepSeaManager manager){
        this.manager = manager;
        createViews();
        registerHandles();
    }

    private void createViews(){
        superficieCanvas = new SuperficieCanvas(manager);
        oficinaCanvas = new OficinaCanvas(manager);
        fossoCanvas = new FossoCanvas(manager);
        fundoCanvas = new FundoCanvas(manager);
        puzzleCanvas = new PuzzleCanvas(manager);
        fimDeJogoCanvas = new FimDeJogoCanvas(manager);

        getChildren().addAll(superficieCanvas,oficinaCanvas, fossoCanvas, fundoCanvas, puzzleCanvas, fimDeJogoCanvas);

    }
    private void registerHandles(){
        this.widthProperty().addListener(ev-> updateSizes());
        this.heightProperty().addListener(ev-> updateSizes());
    }

    private void updateSizes() {
        double w = getWidth();
        double h = getHeight();
        superficieCanvas.updateSize(w, h);
        oficinaCanvas.updateSize(w,h);
        fossoCanvas.updateSize(w,h);
        fundoCanvas.updateSize(w,h);
        puzzleCanvas.updateSize(w,h);
        fimDeJogoCanvas.updateSize(w,h);
    }
}
