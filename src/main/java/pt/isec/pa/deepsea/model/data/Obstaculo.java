package pt.isec.pa.deepsea.model.data;
import java.io.Serial;

import static pt.isec.pa.deepsea.model.data.Constantes.ID_INICIAL_OBSTACULOS;

/**
 * Classe abstrata base para elementos que causam impacto negativo ao drone.
 * <p>
 * Armazena a carga de dano infligida em colisões e centraliza a geração do
 * identificador único (ID sequencial) partilhado entre todas as instâncias de obstáculos.
 * </p>
 *
 * @author Miguel Zara
 */

abstract public class Obstaculo extends Elemento {
    @Serial
    private static final long serialVersionUID = 1L;

    static private int contadorID = ID_INICIAL_OBSTACULOS;
    private final int id;
    private final double dano;

    Obstaculo(int lin, int col, double dano){
        super(lin, col);
        this.dano = dano;
        this.id = contadorID++;
    }

    public int getId(){return id;}

    public double getDano(){
        return dano;
    }

    public static int getContadorAtual() {
        return contadorID;
    }

    public static void setContador(int valorRecuperado) {
        contadorID = valorRecuperado;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Obstaculo ob = (Obstaculo) o;
        return ob.id == id;
    }

    @Override
    public int hashCode(){return id;}

}
