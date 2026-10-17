package pt.isec.pa.deepsea.model.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DeepSeaDataTest {

    private DeepSeaData data;

    @BeforeEach
    void setUp() {
        // Antes de cada teste, criamos um jogo novo e limpo
        data = new DeepSeaData();
    }

    @Test
    void testInicializacaoModelo() {
        assertNotNull(data.getNavio(), "O navio deve ser inicializado");
        assertNotNull(data.getSuperficie(), "A superfície deve ser inicializada");

        // Verificar posição inicial do navio
        assertEquals(Constantes.SUPERFICIE_LINHAS / 2, data.getNavio().getLin());
        assertEquals(Constantes.SUPERFICIE_COLUNAS / 2, data.getNavio().getCol());
    }



    @Test
    void testOrdenacaoDronesPorIntegridade() {
        List<Drone> drones = data.getDronesOrdenadosPorIntegridade();
        String idDroneDanificado = drones.get(0).getIdentificacao();

        // Destruímos o primeiro drone propositadamente
        drones.get(0).ficarPerdido();

        List<Drone> ordenados = data.getDronesOrdenadosPorIntegridade();

        assertEquals(idDroneDanificado, ordenados.get(2).getIdentificacao(),
                "O drone com integridade 0 deve ter afundado para a última posição da lista");

        assertTrue(ordenados.get(0).getIntegridade() > ordenados.get(2).getIntegridade(),
                "O primeiro da lista tem de ter estritamente mais vida que o último (que está morto).");
    }


    @Test
    void testNavioConsomeCombustivelCorretamente() {
        double combustivelInicial = data.getNavio().getCombustivel();

        boolean moveu = data.moveNavio("DIR");

        assertTrue(moveu, "O navio devia conseguir mover-se para a direita");
        assertEquals(combustivelInicial - Constantes.CONSUMO_MOVIMENTO_NAVIO, data.getNavio().getCombustivel(),
                "O navio não consumiu " + Constantes.CONSUMO_MOVIMENTO_NAVIO);
    }

    @Test
    void testNavioNaoSaiDosLimitesDaSuperficie() {
        // Forcar navio a ir para ir ate a parede
        for (int i = 0; i < Constantes.SUPERFICIE_LINHAS / 2; i++) {
            data.moveNavio("CIMA");
        }

        boolean rebentouParede = data.moveNavio("CIMA");

        assertFalse(rebentouParede, "navio fugiu do mapa na superficie");
        assertEquals(0, data.getNavio().getLin(), "O navio devia estar na linha 0");
    }

    @Test
    void testMatematicaDeDanoDasRochas() {
        Drone d = new Drone("TestePrimos");

        assertEquals(1, d.getProximoPrimo(), "colisão 1 devia dar 1 de dano");
        assertEquals(2, d.getProximoPrimo(), "colisão 2 devia dar 2 de dano");
        assertEquals(3, d.getProximoPrimo(), "colisão 3 devia dar 3 de dano");
        assertEquals(5, d.getProximoPrimo(), "colisão 4 devia dar 5 de dano");
        assertEquals(7, d.getProximoPrimo(), "colisão 5 devia dar 7 de dano");
        assertEquals(11, d.getProximoPrimo(), "colisão 5 devia dar 11 de dano");
    }

    @Test
    void testLimitesDeSegurancaDoDroneNoFosso() {
        data.selecionaDrone("Drone 1");
        Drone d = data.getNavio().getDroneReal();

        d.prepararDescida();

        assertTrue(data.isDroneNaSaidaFosso(), "O Drone devia ser considerado na saida do fosso (linha == 0)");
        assertFalse(data.isDroneNoFundoDoFosso(), "O Drone está na linha 0, não devia ser considerado no Fundo");

        d.prepararSubida();

        assertFalse(data.isDroneNaSaidaFosso(), "O Drone está no fundo, não devia ser considerado no Fundo");
        assertTrue(data.isDroneNoFundoDoFosso(), "O Drone deveria esta na ultima linha do fosso.");
    }

    @Test
    void testFugaDoPuzzleRestauraOEstadoCorretamente() {
        data.selecionaDrone("Drone 1");
        Drone d = data.getNavio().getDroneReal();

        boolean res = data.perderPuzzle();

        assertTrue(res, "perder puzzle nao correu bem");

        // drone na ultima linha do Fosso
        assertEquals(Constantes.FOSSO_LINHAS - 1, d.getLin(), " drone não foi para o fundo do fosso");
    }
}