package pt.isec.pa.deepsea.model.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DroneTest {
    private Drone drone;

    @BeforeEach
    void setUp() {
        drone = new Drone("Drone Teste");
    }

    @Test
    void testGestaoCombustivel() {
        double combustivelInicial = drone.getCombustivel();
        double adicionado = drone.abastecer(Constantes.COMBUSTIVEL_MAXIMO_DRONE*0.2);
        assertEquals(combustivelInicial+adicionado, drone.getCombustivel(), "O combustível deve ser 50");
        assertEquals(Constantes.COMBUSTIVEL_MAXIMO_DRONE*0.2, adicionado);

        // Testar limite máximo
        double excesso = drone.abastecer(Constantes.COMBUSTIVEL_MAXIMO_DRONE*2);
        assertEquals(Constantes.COMBUSTIVEL_MAXIMO_DRONE, drone.getCombustivel());
        assertTrue(excesso < Constantes.COMBUSTIVEL_MAXIMO_DRONE*2, "Deve retornar apenas a quantidade realmente abastecida");
    }

    @Test
    void testRecolhaEDescargaRecursos() {
        drone.recolherMinerio(10);
        drone.recolherArtefacto(new Artefacto(0, 0, 1));

        assertEquals(10, drone.descarregarMinerios(), "Deve descarregar 10 minérios");
        assertEquals(0, drone.descarregarMinerios(), "Após descarga, deve ter 0 minérios");

        List<Artefacto> artefactos = drone.descarregarArtefactos();
        assertEquals(1, artefactos.size(), "Deve ter descarregado 1 artefacto");
        assertTrue(drone.descarregarArtefactos().isEmpty(), "Lista deve estar vazia após descarga");
    }
}