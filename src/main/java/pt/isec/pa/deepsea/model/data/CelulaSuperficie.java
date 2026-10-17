package pt.isec.pa.deepsea.model.data;

import java.io.Serial;
import java.util.List;
/**
 * Célula de elite da superfície que ancora as profundezas do oceano.
 * <p>
 * Cada coordenada na superfície é a porta de entrada para um ecossistema vertical
 * (Fosso) e horizontal profundo (Fundo) independente.
 * </p>
 * @author Guilherme Eça, Luana Rabello e Miguel Zara
 */
public class CelulaSuperficie extends Celula{
    @Serial
    private static final long serialVersionUID = 1L;

    private Fosso fosso;
    private Fundo fundo;


    /**
     * Constrói uma nova célula de superfície nas coordenadas especificadas.
     * Ao ser instanciada, cria automaticamente o seu respectivo fosso e fundo.
     *
     * @param l A linha (coordenada Y) da célula na superfície.
     * @param c A coluna (coordenada X) da célula na superfície.
     */

    CelulaSuperficie(int l, int c){
        super(l,c);
        fosso = new Fosso();
        fundo = new Fundo();
    }


    CelulaFosso getCelulaFosso(int l, int c){
        return fosso.getCelula(l,c);
    }
    CelulaFundo getCelulaFundo(int l, int c){
        return fundo.getCelula(l,c);
    }

    Fosso getFosso() {return fosso;}
    Fundo getFundo() {return fundo;}

    void prepara(){
        fosso.prepara();
        fundo.prepara();
    }

    void preparaDefesa(){
        fosso.preparaDefesa();
        fundo.preparaDefesa();
    }

    void iniciaFosso() {
        if (Constantes.MODO_DEFESA) {
            fosso.iniciaDefesa();
        } else {
            fosso.inicia();
        }
    }

    void iniciaFundo() {
        if (Constantes.MODO_DEFESA) {
            fundo.iniciaDefesa();
        } else {
            fundo.inicia();
        }
    }

    /**
     * Reposiciona recursos (artefatos e minérios) que foram perdidos durante a exploração,
     * alocando-os de volta no Fundo associado a esta superfície.
     *
     * @param aPerdidos Lista de {@link Artefacto} que foram perdidos e precisam ser reposicionados.
     * @param mPerdidos Quantidade de minérios perdidos a serem redistribuídos.
     */
    void reposicionarRecursos(List<Artefacto> aPerdidos, int mPerdidos) {
        fundo.reposicionarRecursosPerdidos(aPerdidos, mPerdidos);
    }

    /**
     * Posiciona um artefato específico no Fundo.
     *
     * @param id O identificador único do artefato a ser colocado.
     */
    void colocarArtefacto(int id){
        fundo.colocarArtefacto(id);
    }

    void colocarArtefactoDefesa(int id){
        fundo.colocarArtefactoDefesa(id);
    }
}
