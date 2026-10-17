package pt.isec.pa.deepsea.model.state.states;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.data.Constantes;
import pt.isec.pa.deepsea.model.data.Fundo;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;

import static org.junit.jupiter.api.Assertions.*;

class PuzzleStateTest {

    private DeepSeaContext context;

    @BeforeEach
    void setUp() {
        Constantes.MODO_DEFESA = true;
        context = new DeepSeaContext();
        prepararEstadoPuzzle();
    }

    @Test
    void testPuzzleEsgotaMovimentosEDevolveAoFosso() {

        esgotarMovimentosDoPuzzle();

        assertEquals(DeepSeaState.FOSSO, context.getState(),
                "Após esgotar os movimentos legais, o drone tem de recuar para o FOSSO.");
    }

    @Test
    void testMovimentoInvalidoIgnorado() {
        // Tentar um movimento completamente fora da grelha (-1, -1)
        boolean moveu = context.moverPeca(-1, -1);

        assertFalse(moveu, "Movimentos com coordenadas inválidas devem ser bloqueados e retornar false.");
        assertEquals(DeepSeaState.PUZZLE, context.getState(), "O estado deve continuar PUZZLE após uma tentativa de movimento inválida.");
    }

    //auxiliares
    private void prepararEstadoPuzzle() {
        context.abrir();
        context.selecionaDrone("Drone 1");
        context.abastecer();
        context.fechar();
        context.selecionaDrone("Drone 1");
        context.mergulhar();

        // Desce até ao fundo do mar
        for (int i = 0; i < Constantes.FOSSO_LINHAS ; i++) context.mover("BAIXO");
        context.mergulhar();

        // Procura o artefacto e anda até ele para ativar o puzzle
        for (int i = 0; i < Constantes.FUNDO_LINHAS; i++) {
            for (int j = 0; j < Constantes.FUNDO_COLUNAS; j++) {
                if (context.getFundoElemento(i, j) == Fundo.FundoTipoElemento.ARTEFACTO) {
                    conduzirDrone(i, j);
                    return;
                }
            }
        }
    }

    private void conduzirDrone(int destinoLin, int destinoCol) {
        int dLin = Constantes.FUNDO_LINHAS / 2;
        int dCol = Constantes.FUNDO_COLUNAS / 2;
        while (dLin < destinoLin) { context.mover("BAIXO"); dLin++; }
        while (dLin > destinoLin) { context.mover("CIMA"); dLin--; }
        while (dCol < destinoCol) { context.mover("DIR"); dCol++; }
        while (dCol > destinoCol) { context.mover("ESQ"); dCol--; }
    }

    private void esgotarMovimentosDoPuzzle() {
        int[][] grelha = context.getPuzzleGrelha();
        int zeroLin = -1, zeroCol = -1;

        for (int i = 0; i < Constantes.DIMENSAO_PUZZLE; i++) {
            for (int j = 0; j < Constantes.DIMENSAO_PUZZLE; j++) {
                if (grelha[i][j] == 0) {
                    zeroLin = i;
                    zeroCol = j;
                }
            }
        }

        int vizinhoLin = (zeroLin > 0) ? zeroLin - 1 : zeroLin + 1;

        for (int i = 0; i < Constantes.MAX_MOVIMENTOS_PUZZLE / 2; i++) {
            context.moverPeca(vizinhoLin, zeroCol);
            context.moverPeca(zeroLin, zeroCol);
        }
    }
}