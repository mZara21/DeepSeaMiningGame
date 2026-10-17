package pt.isec.pa.deepsea.model;

import pt.isec.pa.deepsea.model.data.Drone;

import java.util.Objects;

/**
 * Representação imutável do estado do drone.
 * @author Guilherme Eça
 * @param id Identificação
 * @param combustivel Combustivel
 * @param combustivelMaximo Combustivel maximo que o drone pode ter
 * @param integridade Integridade
 * @param integridadeMaxima Inegridade máxima que o drone pode ter
 * @param nivelCombustivel Nivel do combustivel
 * @param nivelCasco Nivel do casco
 * @param isPerdido foi perdido durante o mergulho
 * @param nArtefactos numero de artefactos
 * @param nMinerios numero de minerios
 * @param lin linha atual na grelha
 * @param col coluna atual na grelha
 */
public record DroneRec(
        String id,
        double combustivel,
        double combustivelMaximo,
        double integridade,
        double integridadeMaxima,
        int nivelCombustivel,
        int nivelCasco,
        boolean isPerdido,
        int nArtefactos,
        int nMinerios,
        int lin,
        int col


) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DroneRec d)) return false;
        return d.id().equals(id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}