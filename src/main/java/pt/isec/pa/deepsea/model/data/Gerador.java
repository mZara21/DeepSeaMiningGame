package pt.isec.pa.deepsea.model.data;

import static pt.isec.pa.deepsea.model.data.Constantes.MODO_DEFESA;

/**
 * Utilitário responsável por orquestrar a inicialização do ambiente global do jogo.
 * <p>
 * Garante que o mundo é gerado apenas uma vez por execução.
 * Redirecioniona o fluxo para a geração procedimental normal
 * ou para o layout estático de testes (Modo Defesa).
 * </p>
 *
 * @author Guilherme Eça, Miguel Zara
 */

public class Gerador {

    private boolean gerado = false;

    // Funcao chamada pelo "dono" da superficie -> "dono" - DeepSeaGame por agora
    void preparaAmbiente(Superficie s){
        if(gerado) return;

        if(MODO_DEFESA){
            s.preparaAmbienteDefesa();
        } else{
            s.preparaAmbiente();
        }
        gerado = true;
    }
}
