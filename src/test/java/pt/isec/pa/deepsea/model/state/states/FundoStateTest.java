package pt.isec.pa.deepsea.model.state.states;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.data.DeepSeaData;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;

import static org.junit.jupiter.api.Assertions.*;

class FundoStateTest {

    private DeepSeaData data;
    private DeepSeaContext context;
    private FundoState fundoState;
    @BeforeEach
    void setUp() {
        data = new DeepSeaData();
        context = new DeepSeaContext();
        data.selecionaDrone("Drone 1");
        fundoState = new FundoState(data, context);
    }

    @Test
    void subirRegressaAoFosso() {
        boolean subiu = fundoState.subir();
        assertTrue(subiu, "O método subir() no FundoState deve ser válido e retornar true.");
        assertEquals(DeepSeaState.FOSSO, context.getState(), "O estado da máquina deve transitar fisicamente para FOSSO.");
    }
}