package pt.isec.pa.deepsea.model.data;


import java.io.Serial;

/**
 * Representa um obstáculo físiconas paredes do Fosso.
 * <p>
 * Ao contrário de outros elementos, as rochas não permitem a passagem do drone
 * (isPassavel = false) e causam dano por colisão.
 * </p>
 * @author  Miguel Zara
 */
public class Rocha extends Elemento {
    @Serial
    private static final long serialVersionUID = 1L;

    Rocha(int lin, int col) {
        super(lin, col);
    }

    @Override
    public boolean isPassavel() {
        return false;
    }

    @Override
    public String toString() { return "R"; }
}