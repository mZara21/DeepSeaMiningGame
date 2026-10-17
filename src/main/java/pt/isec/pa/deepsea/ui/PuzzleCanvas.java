package pt.isec.pa.deepsea.ui;

import javafx.application.Platform;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.ui.res.ImageLoader;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.BlurType;
import pt.isec.pa.deepsea.ui.res.SoundManager;

/**
 * Representa o ecrã da fase de recuperação do artefacto - puzzle
 * @author Luana Rabello
 */

public class PuzzleCanvas extends Canvas {
    private final DeepSeaManager manager;

    public PuzzleCanvas(DeepSeaManager m){
        super(0,0);
        manager = m;
        this.setFocusTraversable(true);
        this.setManaged(false);

        registerHandlers();
        update();
    }

    /**
     * Regista os "listeners" necessários para o funcionamento do puzzle.
     * Fica à escuta de cliques do rato nas peças da grelha para efetuar os movimentos no modelo.
     */
    private void registerHandlers(){
        manager.addPropertyChangeListener(DeepSeaManager.PROP_MOVE, e -> {
            Platform.runLater(() -> update());
        });

        manager.addPropertyChangeListener(DeepSeaManager.PROP_STATE,e -> {
            Platform.runLater( ()-> {
                update();
                if(isVisible())
                    SoundManager.getInstance().play(SoundEffect.ARTEFACTO);
            });
        });


        this.visibleProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) update();
        });
        // Sempre que a visibilidade do painel mudar, verifica o novo valor (newValue) e ai chama o update se for verdadeiro

        this.setOnMouseClicked(e -> {
            int [][]grelha = manager.getPuzzleGrelha();
            if (grelha == null) return;

            double w = getWidth();
            double h = getHeight();

            double availableSize = Math.min(w, h) * 0.7;
            double tileSize = availableSize / manager.getDimensaoPuzzle();

            double padding = 30;
            double divX = 50; // Margem de 50px à esquerda do Canvas
            double offsetX = divX + padding;
            double offsetY = (h - (manager.getDimensaoPuzzle() * tileSize)) / 2;

            // verificar area do clique -> se foi feito dentro da area do mini-jogo
            if (e.getX() >= offsetX && e.getX() <= offsetX + (manager.getDimensaoPuzzle() * tileSize) &&
                    e.getY() >= offsetY && e.getY() <= offsetY + (manager.getDimensaoPuzzle() * tileSize)) {

                int linClicada = (int) ((e.getY() - offsetY) / tileSize);
                int colClicada = (int) ((e.getX() - offsetX) / tileSize);

                boolean sucesso = manager.moverPeca(linClicada, colClicada);
                if(sucesso)
                    SoundManager.getInstance().play(SoundEffect.SLIDE);
            }
        });
    }

    /**
     * Atualiza o conteúdo gráfico do canvas.
     * Limpa o ecrã e desenha a imagem de fundo, as peças do minijogo com os seus efeitos (neon),
     * o título e o indicador de movimentos restantes.
     */
    private void update(){

        DeepSeaState state = manager.getState();

        if(state != DeepSeaState.PUZZLE){
            this.setVisible(false);
            return;
        }

        this.setVisible(true);
        this.requestFocus();

        GraphicsContext gc = this.getGraphicsContext2D();
        double w = getWidth();
        double h = getHeight();

        gc.clearRect(0, 0, w, h);
        try {
            Image fundoImage = ImageLoader.getImage("puzzle/fundo-puzzle.jpeg");
            if (fundoImage != null) {
                gc.drawImage(fundoImage, 0, 0, w, h);
            } else {
                gc.setFill(Color.web("#051a33"));
                gc.fillRect(0, 0, w, h);
            }
        } catch (Exception e) {
            gc.setFill(Color.web("#051a33"));
            gc.fillRect(0, 0, w, h);
        }

        int[][] grelha = manager.getPuzzleGrelha();
        if (grelha == null) return;

        // centralização
        double availableSize = Math.min(w, h) * 0.7;
        double tileSize = availableSize / manager.getDimensaoPuzzle();

        //div do tabuleiro com as peças
        double padding = 30;
        double topPadding = 70;
        double bottomPadding = 70;

        double divX = 50;
        double divW = (manager.getDimensaoPuzzle() * tileSize) + (padding * 2);
        double offsetX = divX + padding;
        double offsetY = (h - (manager.getDimensaoPuzzle() * tileSize)) / 2;
        double divY = offsetY - topPadding;
        double divH = (manager.getDimensaoPuzzle() * tileSize) + topPadding + bottomPadding;

        // PAINEL
        // fundo
        gc.setFill(Color.rgb(16, 38, 59, 0.9));
        gc.fillRoundRect(divX, divY, divW, divH, 10, 10);

        //borda
        gc.setStroke(Color.web("#244361"));
        gc.setLineWidth(2);
        gc.strokeRoundRect(divX, divY, divW, divH, 10, 10);

        //textos
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        double centroDivX = divX + (divW / 2);

        // titulo
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Poppins", FontWeight.BOLD, 22));
        gc.fillText("DESBLOQUEIE O ARTEFACTO", centroDivX, divY + 32);

        // alertas visuais
        int movRestantes = manager.getPuzzleMovimentosRestantes();
        // se tiver 5 restantes fica vermelho
        if (movRestantes <= 5) {
            gc.setFill(Color.web("#ff4d4d"));
        } else {
            gc.setFill(Color.YELLOW);
        }
        gc.setFont(Font.font("Poppins", FontWeight.BOLD, 20));
        gc.fillText("Movimentos Restantes: " + movRestantes, centroDivX, divY + divH - 35);

        // peça
        double entrePecas = 6.0;
        double tamPeca = tileSize - entrePecas;

        //efeito neon
        DropShadow neonTexto = new DropShadow(BlurType.GAUSSIAN, Color.web("#a3f7ff"), 4, 0.1, 0, 0);

        for (int l = 0; l < manager.getDimensaoPuzzle(); l++) {
            for (int c = 0; c < manager.getDimensaoPuzzle(); c++) {
                int valor = grelha[l][c];

                double x = offsetX + (c * tileSize) + (entrePecas / 2);
                double y = offsetY + (l * tileSize) + (entrePecas / 2);

                if (valor == 0) {
                    gc.setFill(Color.web("#051a33", 0.8));
                    gc.fillRoundRect(x, y, tamPeca, tamPeca, 5, 5);

                    gc.setStroke(Color.web("#0d1e2d"));
                    gc.setLineWidth(2);
                    gc.strokeRoundRect(x, y, tamPeca, tamPeca, 5, 5);
                } else {
                    gc.setFill(Color.web("#0e4d92", 0.7)); // mais transparente pro nenon sobressair
                    gc.fillRoundRect(x, y, tamPeca, tamPeca, 5, 5);

                    // contorno
                    gc.setStroke(Color.web("#00f2ff"));
                    gc.setLineWidth(1.0);
                    gc.strokeRoundRect(x, y, tamPeca, tamPeca, 5, 5);

                    // numero
                    gc.setEffect(neonTexto);
                    gc.setFill(Color.WHITE);
                    gc.setFont(Font.font("Poppins", FontWeight.BOLD, tamPeca * 0.4));
                    gc.fillText(String.valueOf(valor), x + (tamPeca / 2), y + (tamPeca / 2));

                    // limpar efeito para nao pegar em outros canvas
                    gc.setEffect(null);
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
    }}
