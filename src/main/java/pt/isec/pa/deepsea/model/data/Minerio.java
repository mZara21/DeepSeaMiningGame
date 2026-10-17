package pt.isec.pa.deepsea.model.data;

import java.io.Serial;

/**
 * Representa depósitos de recursos minerais encontrados no fundo do mar.
 * <p>
 * São utilizados como moeda de troca na Oficina para adquirir melhorias
 * para os drones da frota.
 * </p>
 * @author Miguel Zara
 */
public class Minerio extends Elemento {
    @Serial
    private static final long serialVersionUID = 1L;

    private int quantidade;

    Minerio(int lin, int col, int quantidade){
        super(lin, col);
        this.quantidade = quantidade;
    }

    public int getQuantidade() { return quantidade; }

    @Override
    public String toString() { return "m"; }
}
