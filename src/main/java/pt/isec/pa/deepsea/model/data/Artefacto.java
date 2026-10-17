package pt.isec.pa.deepsea.model.data;

import java.io.Serial;
/**
 * Representao objetivo principal da missão.
 * <p>
 * Estes elementos são gerados no fundo do mar e requerem a resolução
 * de um puzzle para serem extraídos e transportados para o navio.
 * </p>
 * @author Miguel Zara
 */
public class Artefacto extends Elemento {
    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    Artefacto(int lin, int col, int id){
        super(lin,col);
        this.id = id;
    }

    public int getId() { return id; }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Artefacto a = (Artefacto) o;

        return this.id == a.id;
    }

    @Override
    public int hashCode(){return id;} // Integer.hashCode(id) == id

    @Override
    public String toString() { return "a"; }
}
