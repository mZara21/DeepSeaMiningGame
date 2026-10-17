package pt.isec.pa.deepsea.model.state.states;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.data.DeepSeaData;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;

import static org.junit.jupiter.api.Assertions.*;

class SuperficieStateTest {

    private DeepSeaData data;
    private DeepSeaContext context;
    private SuperficieState superficieState;

    @BeforeEach
    void setUp() {
        data = new DeepSeaData();
        context = new DeepSeaContext();

        superficieState = new SuperficieState(data, context);
    }

    @Test
    void abrirOficina() {
        boolean oficinaAbriu = superficieState.abrir();

        assertTrue(oficinaAbriu, "A ação de abrir a oficina a partir da superfície deve retornar true.");
        assertEquals(DeepSeaState.OFICINA, context.getState(),
                "O Contexto da FSM deve transitar fisicamente para o estado OFICINA.");

        assertNull(data.getDroneSelecionado(), "Nenhum drone deve estar selecionado ao entrar na oficina.");
    }

    @Test
    void mergulharSemDrone() {
        data.desselecionarDrone();

        boolean conseguiuDescer = superficieState.mergulhar();
        assertFalse(conseguiuDescer, "Não pode ser possível descer/mergulhar sem um drone selecionado.");

        // contexto inicializa na Superfície, não deve mudar para o Fosso
        assertEquals(DeepSeaState.SUPERFICIE, context.getState(),
                "O estado da FSM deve continuar na SUPERFICIE uma vez que o mergulho falhou.");
    }
}