package pt.isec.pa.deepsea.model.data;


import java.io.Serial;

import static pt.isec.pa.deepsea.model.data.Constantes.MULTIPLICADOR_CORRENTE_FORTE;

/**
 * Representa uma corrente de água forte no Fosso.
 * <p>
 * Ao contrário de outros obstáculos, não causa dano direto à integridade do casco,
 * mas aplica um multiplicador ao consumo de combustível (MULTIPLICADOR_CORRENTE_FORTE)
 * enquanto o drone atravessa a célula.
 * </p>
 * @author Miguel Zara
 */
public class CorrenteAgua extends Obstaculo {
    @Serial
    private static final long serialVersionUID = 1L;

    CorrenteAgua(int lin, int col){
        super(lin, col, 0);
    }

    public double getMultiplicador(){return MULTIPLICADOR_CORRENTE_FORTE;}

    @Override
    public String toString() { return "C"; }
}
