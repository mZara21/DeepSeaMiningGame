package pt.isec.pa.deepsea.model.state.states;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.data.DeepSeaData;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;

import static org.junit.jupiter.api.Assertions.*;

class OficinaStateTest {

    private DeepSeaData data;
    private DeepSeaContext context;
    private OficinaState oficinaState;

    @BeforeEach
    void setUp() {
        data = new DeepSeaData();
        context = new DeepSeaContext();

        oficinaState = new OficinaState(data, context);
    }

    @Test
    void selecionaDroneValidoEInvalido() {
        boolean selecionouValido = oficinaState.selecionaDrone("Drone 1");

        assertTrue(selecionouValido, "A oficina deve conseguir selecionar um drone válido e operacional.");
        assertNotNull(data.getDroneSelecionado(), "O drone tem de ficar guardado no DeepSeaData.");
        assertEquals("Drone 1", data.getDroneSelecionado().getIdentificacao(), "A identificação deve corresponder.");

        // tenta selecionar um nome inventado
        boolean selecionouInvalido = oficinaState.selecionaDrone("Falso Drone");

        // a operação tem de ser bloqueada
        assertFalse(selecionouInvalido, "A oficina não deve permitir selecionar um drone que não existe na frota.");
    }

    @Test
    void abastecerDroneSelecionado() {
        oficinaState.selecionaDrone("Drone 1");

        boolean abasteceu = oficinaState.abastecer();
        assertTrue(abasteceu, "O método deve retornar true quando o drone é abastecido com sucesso.");
    }

    @Test
    void abastecerSemDroneSelecionado() {
        data.desselecionarDrone();

        boolean abasteceu = oficinaState.abastecer();
        assertFalse(abasteceu, "A oficina não pode abastecer se não houver nenhum drone selecionado na oficina.");
    }
}
