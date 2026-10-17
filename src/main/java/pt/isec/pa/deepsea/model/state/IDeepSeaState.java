package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.TipoUpgrade;

/**
 * Interface base para a State Machine do jogo.
 * Define acções de navegação, oficina, puzzle e gestão de drones.
 * @author Guilherme Eça, Luana Rabello e Miguel Zara
 */
public interface IDeepSeaState {

    default boolean mover(String direcao){ return false; }
    default boolean subir(){ return false; }
    default boolean mergulhar(){ return false; }

    default boolean abrir(){ return false; }
    default boolean fechar(){ return false; }
    default void desistir(){}

    default boolean moverPeca(int lin, int col){ return false; }

    default boolean abastecer(){ return false; }
    default boolean reparar(){ return false; }
    default boolean melhorar(TipoUpgrade melhoria){ return false; }

    default boolean selecionaDrone(String droneId){ return false; }

    /** @return O identificador do estado actual. */
    DeepSeaState getState();
}