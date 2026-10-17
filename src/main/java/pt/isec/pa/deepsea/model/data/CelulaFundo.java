package pt.isec.pa.deepsea.model.data;

import java.io.Serial;

/**
 * Célula do fundo do mar.
 * Gere o estado de visibilidade para o jogador.
 * @author Luana Rabello
 */
public class CelulaFundo extends Celula{
    @Serial
    private static final long serialVersionUID = 1L;

    private boolean revelada;

    CelulaFundo(int l, int c){
        super(l,c);
        revelada = false;
    }
    public boolean isRevelada(){
        return revelada;
    }
    void setRevelada(boolean opt){
        revelada = opt;
    }
}
