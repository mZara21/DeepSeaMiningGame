package pt.isec.pa.deepsea.model.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TesteIDS {
    @BeforeEach
    void setUp(){
        Obstaculo.setContador(Constantes.ID_INICIAL_OBSTACULOS);
    }

    @Test
    void testIDInicialESequencial() {
        AnimalMarinho a1 = new AnimalMarinho(0, 0);
        assertEquals(Constantes.ID_INICIAL_OBSTACULOS,a1.getId());

        CorrenteAgua c1 = new CorrenteAgua(0, 1);
        assertEquals(Constantes.ID_INICIAL_OBSTACULOS + 1, c1.getId());
    }

    @Test
    void testIDsDiferentesParaDiferentesSubclasses() {
        AnimalMarinho animal = new AnimalMarinho(1, 1);
        CorrenteAgua corrente = new CorrenteAgua(2, 2);
        Monstro monstro = new Monstro(3, 3);

        assertNotEquals(animal.getId(), corrente.getId());
        assertNotEquals(corrente.getId(), monstro.getId());

        assertEquals(animal.getId() + 1, corrente.getId());
        assertEquals(corrente.getId() + 1, monstro.getId());
    }
}
