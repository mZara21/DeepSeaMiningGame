package pt.isec.pa.deepsea.ui.res;

import javafx.scene.Parent;
import pt.isec.pa.deepsea.model.DeepSeaLog;
/**
 * Classe utilitária responsável pelo carregamento e aplicação de folhas de estilo (CSS).
 * <p>
 * Disponibiliza métodos estáticos para aplicar ficheiros CSS a componentes
 * da interface gráfica (JavaFX), registando também o sucesso ou falha da
 * operação no sistema de logs global do jogo.
 * </p>
 */
public class CSSManager {
    private CSSManager() { }
    public static void applyCSS(Parent parent, String filename) {
        var url = CSSManager.class.getResource("/css/"+filename);
        if (url == null) {
            DeepSeaLog.getInstance().log("Ficheiro" + filename + " nao encontrado.");
            return;
        }

        DeepSeaLog.getInstance().log("Ficheiro CSS encontrado: " + filename);
        parent.getStylesheets().add(url.toExternalForm());
    }
}