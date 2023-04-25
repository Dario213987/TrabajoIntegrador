import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import static org.junit.Assert.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProgramaTest extends Programa{

    public ProgramaTest() throws IOException{
        ArrayList<String> tmp = (ArrayList<String>) Files.readAllLines(Path.of("src/test/java/pronostico.csv").toAbsolutePath());
        for(int i = 1;i< tmp.size();i++){
            pronostico.add(tmp.get(i));
        }
        tmp = (ArrayList<String>) Files.readAllLines(Path.of("src/test/java/resultados.csv").toAbsolutePath());
        for(int i = 1;i< tmp.size();i++){
            resultados.add(tmp.get(i));
        }
        cargarPersonas();
        creadorDeRondas();
        contarPuntos(rondas, personas);
    }



    @Test
    void puntosTest() {
            Assertions.assertEquals(2, personas.get(0).getPuntos());
    }

    @Test
    void cantidadDeRondasTest() {
        Assertions.assertEquals(2, this.rondas.size());
    }

    @Test
    void cantidadDePersonasTest() {
        Assertions.assertEquals(1, this.personas.size());
    }
}