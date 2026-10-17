package pt.isec.pa.deepsea.model.state.states;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.data.Constantes;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;

import static org.junit.jupiter.api.Assertions.*;

class FossoStateTest {

    private DeepSeaContext context;

    @BeforeEach
    void setUp() {
        context = new DeepSeaContext();

        context.abrir();
        context.selecionaDrone("Drone 1");
        context.abastecer();
        context.fechar();

        context.selecionaDrone("Drone 1");
        boolean conseguiuDescer = context.mergulhar();
        assertTrue(conseguiuDescer, "O setup falhou: O drone não teve permissão para mergulhar!");
    }

    @Test
    void testEstadoInicialGaranteFosso() {
        assertEquals(DeepSeaState.FOSSO, context.getState(), "O estado atual deveria ser FOSSO.");
    }

    @Test
    void testSubirParaSuperficieComSucesso() {
        // O drone entra na linha 0, logo PODE subir.
        boolean subiu = context.mover("CIMA"); // O FossoState interceta o CIMA e chama o subir()

        assertTrue(subiu, "O drone devia conseguir subir da linha 0.");
        assertEquals(DeepSeaState.SUPERFICIE, context.getState(), "O estado devia ter voltado para SUPERFICIE.");
    }

    @Test
    void testDescerParaFundoBloqueadoNaLinha0() {
        // O drone está na linha 0. Tentar usar o comando "BAIXO" não deve transitar para o Fundo.
        // Vai apenas mover o drone um pixel para baixo no Fosso.
        boolean moveu = context.mover("BAIXO");

        assertTrue(moveu, "O drone devia conseguir mover-se para baixo no Fosso.");
        assertEquals(DeepSeaState.FOSSO, context.getState(), "O estado DEVE continuar a ser FOSSO, não Fundo!");
    }

    @Test
    void testDescerParaFundoComSucesso() {
        // Vamos forçar o drone a ir até ao fundo do fosso
        for (int i = 0; i < Constantes.FOSSO_LINHAS - 1; i++) {
            context.mover("BAIXO");
        }

        // Agora ele está na última linha. O próximo "BAIXO" deve acionar a transição!
        boolean desceu = context.mover("BAIXO");

        assertTrue(desceu, "A transição para o Fundo devia ter sucesso.");
        assertEquals(DeepSeaState.FUNDO, context.getState(), "O estado falhou a transição para o FUNDO.");
    }

    @Test
    void testComandosInvalidosNoFosso() {
        // No fosso não podes abastecer nem reparar!
        assertFalse(context.abastecer(), "O FossoState não devia permitir abastecer.");
        assertFalse(context.reparar(), "O FossoState não devia permitir reparar.");
        assertFalse(context.selecionaDrone("Drone 1"), "Não se pode trocar de drone a meio do mergulho.");
        assertEquals(DeepSeaState.FOSSO, context.getState(), "O estado não se devia alterar por comandos inválidos.");
    }
}