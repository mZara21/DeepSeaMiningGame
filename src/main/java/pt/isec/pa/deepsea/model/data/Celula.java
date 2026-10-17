package pt.isec.pa.deepsea.model.data;

import java.io.Serial;
import java.io.Serializable;


/**
 * Unidade fundamental de todas as grelhas do jogo.
 * <p>
 * Armazena as coordenadas matriciais e a referência para o elemento físico
 * (obstáculo ou recurso) que ocupa o espaço.
 * </p>
 * @author Luana Rabello e Guilherme Eça
 */
abstract public class Celula implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected int lin, col;
    protected Elemento elemento;

    Celula(int l, int c){
        lin = l;
        col = c;
    }

    public Elemento getElemento() {
        return elemento;
    }

    void setElemento(Elemento e){ elemento = e;}

    public int getLin(){return lin;}
    public int getCol(){return col;}

}
