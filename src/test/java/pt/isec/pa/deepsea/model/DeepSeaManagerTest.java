package pt.isec.pa.deepsea.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.data.Constantes;
import pt.isec.pa.deepsea.model.data.Obstaculo;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class DeepSeaManagerTest {

    private DeepSeaManager manager;
    private File ficheiroTemp;

    @BeforeEach
    void setUp() throws Exception {
        manager = new DeepSeaManager();

        ficheiroTemp = File.createTempFile("deepsea_save_test", ".dat");
    }

    @AfterEach
    void tearDown() {
        if (ficheiroTemp.exists()) {
            ficheiroTemp.delete();
        }
    }

    @Test
    void testSaveLoadRestauraContadorDeIDs() {
        manager.abrir();
        manager.selecionaDrone("Drone 1");
        manager.abastecer();
        manager.fechar();
        manager.selecionaDrone("Drone 1");
        manager.mergulhar();

        int idNoMomentoDeGravar = Obstaculo.getContadorAtual();

        assertTrue(idNoMomentoDeGravar > Constantes.ID_INICIAL_OBSTACULOS,
                "O contador devia ter subido após a geração dos obstáculos no fosso.");

        boolean gravou = manager.saveGame(ficheiroTemp);
        assertTrue(gravou, "O jogo devia gravar com sucesso.");

        Obstaculo.setContador(Constantes.ID_INICIAL_OBSTACULOS);
        assertEquals(Constantes.ID_INICIAL_OBSTACULOS, Obstaculo.getContadorAtual(),
                "A simulação de reinício falhou.");
    }

}