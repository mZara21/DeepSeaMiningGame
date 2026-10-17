package pt.isec.pa.deepsea.model.data;

import java.io.Serial;

/**
 * Representa um animal marinho no Fosso.
 * <p>
 * Ao colidir com o drone, inflige uma quantidade fixa de dano (DANO_ANIMAL_MARINHO)
 * ao casco, reduzindo a sua integridade.
 * </p>
 * @author Miguel Zara
 */
public class AnimalMarinho extends Obstaculo {
    @Serial
    private static final long serialVersionUID = 1L;

    AnimalMarinho(int lin, int col){
        super(lin, col, Constantes.DANO_ANIMAL_MARINHO);
    }

    @Override
    public String toString() { return "A"; }
}
