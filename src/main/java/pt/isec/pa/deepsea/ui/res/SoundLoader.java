package pt.isec.pa.deepsea.ui.res;

import javafx.scene.media.Media;

import java.util.HashMap;
/**
 * Gestor e cache de ficheiros de áudio (Media) da aplicação.
 * <p>
 * À semelhança do ImageLoader, utiliza um mapa em memória para armazenar
 * ficheiros de som previamente carregados. Garante que os pesados recursos
 * de áudio apenas são lidos do disco e instanciados uma vez, otimizando o desempenho.
 * </p>
 */
public class SoundLoader {
    private static final HashMap<String, Media> map = new HashMap<>();

    private SoundLoader(){}

    public static Media getSound(String filename){

        Media m = map.get(filename);

        if(m == null){
            var url = SoundManager.class.getResource("/sounds/" + filename);
            if(url == null) {
                System.out.println("ERRO GRAVE: O ficheiro " + filename + " não foi encontrado na pasta!");
                return null;
            }

            m = new Media(url.toExternalForm());
            map.put(filename,m);

        }
        return m;

    }

}
