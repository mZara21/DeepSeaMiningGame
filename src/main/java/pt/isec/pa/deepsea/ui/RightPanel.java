package pt.isec.pa.deepsea.ui;

import javafx.scene.layout.VBox;
import pt.isec.pa.deepsea.model.DeepSeaManager;

/**
 * Classe abstrata base para os painéis laterais direitos da interface.
 * <p>
 * Define a estrutura comum, estilo e tamanho padrão para os diferentes
 * painéis de contexto que aparecem à direita da área de jogo,
 * consoante a fase atual (Superfície, Oficina, Fosso, Fundo).
 * </p>
 * @author Guilherme Eça, Miguel Zara, Luana Rabello
 */
abstract public class RightPanel extends VBox {
    protected DeepSeaManager manager;

    public RightPanel(DeepSeaManager manager) {
        this.manager = manager;
        this.getStyleClass().add("painel-base");
        this.setSpacing(15);
        this.setMinWidth(200);
    }
}
