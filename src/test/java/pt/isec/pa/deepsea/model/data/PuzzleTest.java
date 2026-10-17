package pt.isec.pa.deepsea.model.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PuzzleTest {

    @Test
    void testEstadoInicial() {
        Puzzle p = new Puzzle();

        assertTrue(p.temMovimentos(), "O puzzle deve permitir movimentos ao iniciar");

        assertFalse(p.isResolvido(), "O puzzle deve estar baralhado após a criação");
    }

    @Test
    void testSegurancaEMovimentosInvalidos() {
        Puzzle p = new Puzzle();

        assertFalse(p.moverPeca(-1, 0), "Não deve permitir linhas negativas");
        assertFalse(p.moverPeca(0, Constantes.DIMENSAO_PUZZLE), "Não deve permitir colunas fora do limite");

        assertFalse(p.moverPeca(-1, -1), "Movimentos inválidos devem retornar false e ser ignorados");
    }
}