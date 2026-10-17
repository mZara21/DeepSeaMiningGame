package pt.isec.pa.deepsea.model.state.states;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.data.DeepSeaData;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;

import static org.junit.jupiter.api.Assertions.*;

class FimDeJogoStateTest {

    private DeepSeaData data;
    private DeepSeaContext context;
    private FimDeJogoState fimDeJogoState;

    @BeforeEach
    void setUp() {
        data = new DeepSeaData();
        context = new DeepSeaContext();

        fimDeJogoState = new FimDeJogoState(data, context);
    }

    @Test
    void acoesBloqueadasNoFimDeJogo() {
        assertFalse(fimDeJogoState.mover("CIMA"), "Não deve ser possível mover o navio/drone no fim do jogo.");
        assertFalse(fimDeJogoState.mergulhar(), "Não deve ser possível mergulhar no fim do jogo.");
        assertFalse(fimDeJogoState.abrir(), "Não deve ser possível abrir a oficina ou puzzle no fim do jogo.");
        assertFalse(fimDeJogoState.abastecer(), "Não deve ser possível abastecer no fim do jogo.");
    }

    @Test
    void getStateRetornaFimDeJogo() {
        assertEquals(DeepSeaState.FIM_DE_JOGO, fimDeJogoState.getState(),
                "O estado deve ser corretamente identificado como FIM_DE_JOGO.");
    }
}