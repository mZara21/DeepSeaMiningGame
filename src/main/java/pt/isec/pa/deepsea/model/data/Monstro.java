package pt.isec.pa.deepsea.model.data;

import java.io.Serial;

/**
 * Representa uma entidade hostil no Fundo.
 * <p>
 * É um obstáculo dinâmico que, em caso de colisão, inflige danos ao drone
 * baseados na constante DANO_MONSTRO, reduzindo a sua integridade física.
 * </p>
 * @author Miguel Zara
 */
public class Monstro extends Obstaculo {
    @Serial
    private static final long serialVersionUID = 1L;

    Monstro(int lin, int col){
        super(lin, col,Constantes.DANO_MONSTRO);
    }

    @Override
    public String toString() { return "M"; }
}
