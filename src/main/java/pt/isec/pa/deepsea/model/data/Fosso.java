package pt.isec.pa.deepsea.model.data;

import java.io.Serial;
import java.util.Random;

import static pt.isec.pa.deepsea.model.data.Constantes.*;

/**
 * Representa a grelha vertical de transição entre a Superfície e o Fundo.
 * <p>
 * Executa o algoritmo de geração procedimental das paredes rochosas limitadoras e
 * efetua o spawn aleatório de obstáculos (Correntes Animais Marinhos)
 * a cada nova instância de descida ou subida do drone.
 * </p>
 *
 * @author Guilherme Eça, Luana Rabello e Miguel Zara
 */

public class Fosso extends Grelha {
    @Serial
    private static final long serialVersionUID = 1L;

    public enum FossoTipoElemento{
        VAZIO, ROCHA, ANIMAL, CORRENTE
    }

    /**
     * Construtor para o Fosso.
     * Cria a grelha correspondente com Células do Fosso.
     */
    Fosso(){
        super(FOSSO_LINHAS, FOSSO_COLUNAS);
        grelha = new CelulaFosso[linhas][colunas];

        for(int i=0; i<linhas; i++){
            for(int j=0; j<colunas; j++)
                grelha[i][j] = new CelulaFosso(i,j);
        }
    }

    public CelulaFosso getCelula(int l, int c){
        return (CelulaFosso) super.getCelula(l,c);
    }

    /**
     * Gera as rochas para o ambiente do Fosso.
     */
    void prepara(){
        gerarRochas();
    }

    /**
     * Limpa o ambiente de obstáculos antigos e gera novos obstáculos.
     */
    void inicia(){
        limpar();
        geraObstaculos();
    }

    private void gerarRochas(){
        int limite, rochaEsq, rochaDir, varEsq, varDir;
        limite = (int)(colunas * 0.5);
        Random rand = new Random();

        // partida
        rochaEsq = rand.nextInt(0, limite/2)+1;
        rochaDir = rand.nextInt(0, limite/2)+1;

        for(int i = 0; i<getLinhas(); i++) {
            if(i>0) {
                varEsq = rand.nextInt(3) - 1; // gera -1, 0 ou 1 -> volta uma coluna, fica mesma ou avança 1
                varDir = rand.nextInt(3) - 1;

                rochaEsq += varEsq;
                rochaDir += varDir;

                if (rochaEsq < 1) rochaEsq = 1;
                if (rochaDir < 1) rochaDir = 1;

                // ajuste se ultrapassar metade -> tira da maior
                while ((rochaEsq + rochaDir) > limite) {
                    if (rochaEsq > rochaDir) rochaEsq--;
                    else rochaDir--;
                }
            }
            for (int j = 0; j < getColunas(); j++) {
                if (j < rochaEsq) {
                    addElemento(new Rocha(i,j));
                } else if (j >= getColunas() - rochaDir) {
                    addElemento(new Rocha(i,j));
                }
            }
        }
    }

    private void geraObstaculos(){

        Random rand = new Random();
        int nObstaculos = rand.nextInt(MAX_OBSTACULOS_FOSSO);
        for (int i = 0; i < nObstaculos; i++){
            int[] pos = encontrarEspacoVazio(0, FOSSO_LINHAS, 0, FOSSO_COLUNAS);
            if (pos == null) return;
            addElemento(rand.nextBoolean() ? new AnimalMarinho(pos[0],pos[1])
                    : new CorrenteAgua(pos[0],pos[1]));
        }
    }

    void preparaDefesa(){
        int linCentro = FOSSO_LINHAS / 2;

        addElemento(new Rocha(linCentro, 0));
        addElemento(new Rocha(linCentro, getColunas()-1));

        /* ou se for uma parede de rochas:
        for(int i=0; i<getLinhas(); i++){
            addElemento(new Rocha(i, 0));
            addElemento(new Rocha(i, getColunas()-1));
        }
        */
    }

    void iniciaDefesa(){
        int linCentro = FOSSO_LINHAS / 2;
        int colCentro = FOSSO_COLUNAS / 2;

        addElemento(new AnimalMarinho(linCentro, colCentro));
        addElemento(new CorrenteAgua(linCentro+1, colCentro));
    }

    FossoTipoElemento getTipoElemento(int l, int c){
        CelulaFosso cel = getCelula(l,c);
        Elemento el = cel.getElemento();
        return switch (el) {
            case Rocha rocha -> FossoTipoElemento.ROCHA;
            case CorrenteAgua correnteAgua -> FossoTipoElemento.CORRENTE;
            case AnimalMarinho animalMarinho -> FossoTipoElemento.ANIMAL;
            case null, default -> FossoTipoElemento.VAZIO;
        };
    }
}
