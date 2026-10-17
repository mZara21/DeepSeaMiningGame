package pt.isec.pa.deepsea.model;
/**
 * Enumeração que define os tipos de melhorias que podem ser aplicadas aos drones.
 * <p>
 * Utilizado na Oficina para identificar de forma segura (Type-Safe) se
 * o jogador pretende evoluir o Tanque de Combustível ou o Casco.
 * </p>
 */
public enum TipoUpgrade {
    COMBUSTIVEL,
    CASCO
}