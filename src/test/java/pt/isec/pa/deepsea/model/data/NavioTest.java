package pt.isec.pa.deepsea.model.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NavioTest {
    private Navio navio;

    @BeforeEach
    void setUp() {
        navio = new Navio(0, 0);
    }

    @Test
    void testAbastecerDrone() {
        Drone d = navio.getDronePorId("Drone 1");
        double combustivelInicialNavio = navio.getCombustivel();

        navio.abasteceDrone(d);

        assertEquals(d.getCombustivelMaximo(), d.getCombustivel(), "Drone deve ter 20 de combustível");
    }

    @Test
    void testRepararDrone() {
        Drone d = navio.getDronePorId("Drone 1");
        d.ficarPerdido(); // Coloca integridade a 0

        double combustivelAntes = navio.getCombustivel();
        navio.reparaDrone("Drone 1");

        assertEquals(Constantes.INTEGRIDADE_MAXIMA_DRONE, d.getIntegridade(), "Drone deve estar reparado");
        assertEquals(combustivelAntes - Constantes.CONSUMO_REPARACAO_CASCO, navio.getCombustivel(), "Navio deve pagar combustível pela reparação");
    }
}