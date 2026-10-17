package pt.isec.pa.deepsea.model.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModoDefesaTest {
    private Fosso fosso;
    private Fundo fundo;

    @BeforeEach
    void setup() {
        Constantes.MODO_DEFESA = true;
        fosso = new Fosso();
        fundo = new Fundo();
    }

    @Test
    void testRochasNoFossoModoDefesa() {
        fosso.preparaDefesa();
        fosso.iniciaDefesa();

        CelulaFosso cf1 = fosso.getCelula(Constantes.FOSSO_LINHAS/2,0);
        CelulaFosso cf2 = fosso.getCelula(Constantes.FOSSO_LINHAS/2,Constantes.FOSSO_COLUNAS-1);


        assertTrue(cf1.getElemento() instanceof Rocha); // rocha na esquerda
        assertTrue(cf2.getElemento() instanceof Rocha); // rocha na direita
    }

    @Test
    void testObstaculosNoFossoModoDefesa() {
        fosso.preparaDefesa();
        fosso.iniciaDefesa();

        int contadorAnimais = 0;
        int contadorCorrentes = 0;

        // apenas um animal e uma corrente alinhados verticalmente no centro
        for (int i = 0; i < fosso.getLinhas(); i++) {
            for (int j = 0; j < fosso.getColunas(); j++) {
                Elemento elem = fosso.getCelula(i, j).getElemento();
                if (elem instanceof AnimalMarinho) contadorAnimais++;
                if (elem instanceof CorrenteAgua) contadorCorrentes++;
            }
        }

        assertEquals(1, contadorAnimais, "No modo defesa deve existir apenas 1 animal no fosso ");
        assertEquals(1, contadorCorrentes, "No modo defesa deve existir apenas 1 corrente no fosso ");
    }

    @Test
    void testElementosNoFundoModoDefesa() {
        fundo.preparaDefesa();
        fundo.iniciaDefesa();

        int contadorMonstros = 0;
        int contadorMinerios = 0;
        boolean encontrouMinerioQtd1 = false;
        boolean encontrouMinerioMax = false;

        for (int i = 0; i < fundo.getLinhas(); i++) {
            for (int j = 0; j < fundo.getColunas(); j++) {
                Elemento elem = fundo.getCelula(i, j).getElemento();

                if (elem instanceof Monstro) contadorMonstros++;

                if (elem instanceof Minerio m) {
                    contadorMinerios++;
                    if (m.getQuantidade() == 1) encontrouMinerioQtd1 = true;
                    if (m.getQuantidade() == Constantes.MAX_MINERIOS) encontrouMinerioMax = true;
                }
            }
        }

        assertEquals(1, contadorMonstros); // Deve existir apenas 1 monstro no fundo
        assertEquals(2, contadorMinerios); // Devem existir apenas 2 minerios no fundo
        assertTrue(encontrouMinerioQtd1); // Um minerio deve ter quantidade 1
        assertTrue(encontrouMinerioMax); // Um minerio deve ter a quantidade maxima definida
    }

    @Test
    void testRevelacaoNoFundoModoDefesa() {
        fundo.preparaDefesa();

        // No modo defesa, todas as celulas devem estar reveladas
        for (int i = 0; i < fundo.getLinhas(); i++) {
            for (int j = 0; j < fundo.getColunas(); j++) {
                assertTrue(fundo.getCelula(i, j).isRevelada());
            }
        }
    }
}