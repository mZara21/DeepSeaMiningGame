package pt.isec.pa.deepsea.model.data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Classe abstrata base para todas as entidades físicas posicionáveis nas grelhas do jogo.
 * <p>
 * Define a estrutura de localização (coordenadas matriciais) e se é passável ou impeditivo de movimento
 * para qualquer objeto residente numa célula.
 * </p>
 *
 * @author Guilherme Eça e Miguel Zara
 */

abstract public class Elemento implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected int lin, col;

    Elemento(int lin, int col){
        this.lin = lin;
        this.col = col;
    }

    public int getLin(){return lin;}
    public int getCol(){return col;}
    void setLin(int l) {lin = l;}
    void setCol(int c) {col = c;}

    /**
     * Define se o drone pode atravessar a célula ocupada por este elemento.
     * Por padrão, a maioria dos elementos permite a passagem.
     * * @return true se for passável, false se for um obstáculo físico impeditivo.
     */
    public boolean isPassavel() {
        return true;
    }
    
    @Override
    public boolean equals(Object o){
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Elemento e = (Elemento) o;
        return e.lin == this.lin && e.col == this.col;
    }

    @Override
    public int hashCode(){return 31 * lin +col;}

}
