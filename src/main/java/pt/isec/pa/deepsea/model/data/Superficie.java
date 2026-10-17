package pt.isec.pa.deepsea.model.data;

import java.io.Serial;

import static pt.isec.pa.deepsea.model.data.Constantes.*;

/**
 * Representa a grelha horizontal de navegação ao nível do mar.
 * <p>
 * Aloja o Navio e atua como o mapa de planeamento da missão. Gere a inicialização das
 * grelhas verticais e profundas (Fosso e Fundo) inerentes a cada coordenada e efetua o
 * cálculo e posicionamento estático inicial dos artefactos espalhados pelo mundo.
 * </p>
 *
 * @author Guilherme Eça, Miguel Zara e Luana Rabello
 */

public class Superficie extends Grelha{
    @Serial
    private static final long serialVersionUID = 1L;

    Superficie() {
        super(SUPERFICIE_LINHAS, SUPERFICIE_COLUNAS);
        grelha = new CelulaSuperficie[linhas][colunas];

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++)
                grelha[i][j] = new CelulaSuperficie(i,j);
        }
    }

    public CelulaSuperficie getCelulaSuperficie(int l, int c){return (CelulaSuperficie) super.getCelula(l,c);}

    void preparaAmbiente(){
        for (int i = 0; i < linhas; i++){
            for (int j = 0; j < colunas; j++){
                getCelulaSuperficie(i,j).prepara();
            }
        }
        int i = 0;
        while(i < N_ARTEFACTOS){
            int l = (int) (Math.random() * SUPERFICIE_LINHAS);
            int c = (int) (Math.random() * SUPERFICIE_COLUNAS);

            CelulaSuperficie cl = getCelulaSuperficie(l,c);

            cl.colocarArtefacto(i + 1);
            i++;
        }
    }

    void preparaAmbienteDefesa(){
        for (int i = 0; i < linhas; i++){
            for (int j = 0; j < colunas; j++){
                getCelulaSuperficie(i,j).preparaDefesa();
            }
        }
        int l = SUPERFICIE_LINHAS / 2;
        int c = SUPERFICIE_COLUNAS / 2;

        CelulaSuperficie cl1 = getCelulaSuperficie(l,c);
        CelulaSuperficie cl2 = getCelulaSuperficie(l + 1,c);

        for (int i = 1; i <= N_ARTEFACTOS ; i++){
            if (i % 2 == 0){
                cl1.colocarArtefactoDefesa(i);
            }else{
                cl2.colocarArtefactoDefesa(i);
            }
        }
    }

    void iniciaFosso(int l, int c) {
        getCelulaSuperficie(l, c).iniciaFosso();
    }

    void iniciaFundo(int l, int c) {
        getCelulaSuperficie(l, c).iniciaFundo();
    }

    boolean temArtefacto(int l, int c){
        CelulaSuperficie cel = getCelulaSuperficie(l,c);

        if(cel == null)
            return false;

        return cel.getFundo().temArtefacto();
    }
}
