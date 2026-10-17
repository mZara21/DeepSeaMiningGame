package pt.isec.pa.deepsea.ui.res;

import javafx.scene.image.Image;

import java.util.HashMap;
/**
 * Gestor e cache de imagens estáticas da aplicação.
 * <p>
 * Utiliza uma estrutura de dados em mapa (HashMap) para armazenar imagens
 * já carregadas em memória, evitando a leitura repetida do disco. Fornece
 * acesso global e otimizado aos recursos visuais do jogo.
 * </p>
 */
public class ImageLoader {
    private static final HashMap<String, Image> images = new HashMap<>();

    public static Image getImage(String name) {
        Image image  = images.get(name);
        if (image == null) {
            image = new Image(ImageLoader.class.getResourceAsStream("/images/"+name));
            images.put(name,image);
        }
        return image;
    }
}