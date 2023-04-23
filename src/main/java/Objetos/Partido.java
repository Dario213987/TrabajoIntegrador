package Objetos;

import Enum.*;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Partido {
    private int id;
    private Equipo equipo1;
    private Equipo equipo2;
    private int GolesEquipo1;
    private int GolesEquipo2;
    public int resultado() {
        if (GolesEquipo2 == GolesEquipo1) {
            return Resultado.empate.getUbicacion();
        }
        if (GolesEquipo1 > GolesEquipo2) {
            return Resultado.ganaEquipo1.getUbicacion();
        } else {
            return Resultado.ganaEquipo2.getUbicacion();
        }

        }
    }

