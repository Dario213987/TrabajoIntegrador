import Objetos.Persona;
import Objetos.Ronda;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProgramaTest extends Programa{

    private ArrayList<Persona> personas;
    private ArrayList<Ronda> rondas;

    public ProgramaTest() throws IOException{
        this.personas = cargarPersonas(Path.of("src/test/java/pronostico.csv").toAbsolutePath());
        this.rondas = creadorDeRondas(Path.of("src/test/java/resultados.csv").toAbsolutePath());
        contarPuntos(rondas, personas);
    }

    @Test
    void puntosTest() {
            assertEquals(2, personas.get(0).getPuntos());
    }

    @Test
    void cantidadDeRondasTest() {
        assertEquals(2,this.rondas.size());
    }

    @Test
    void cantidadDePersonasTest() {
        assertEquals(1,this.personas.size());
    }
}