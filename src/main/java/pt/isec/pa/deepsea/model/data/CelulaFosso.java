package pt.isec.pa.deepsea.model.data;

import java.io.Serial;

/**
 * Célula específica para a navegação vertical no Fosso.
 * Representa um segmento do percurso entre a superfície e o fundo.
 * @author Luana Rabello
 */

public class CelulaFosso extends Celula{
    @Serial
    private static final long serialVersionUID = 1L;

    CelulaFosso(int l, int c) {
        super(l,c);
    }
}
