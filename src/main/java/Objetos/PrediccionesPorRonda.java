package Objetos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
public class PrediccionesPorRonda {
    @Setter
    @Getter
    int nroDeRonda;
    @Getter
    ArrayList<Integer> predicciones;

    public PrediccionesPorRonda(int nroDeRonda, ArrayList<Integer> predicciones) {
        this.nroDeRonda = nroDeRonda;
        this.predicciones = predicciones;
    }
public int getPrediccion(int index){
        return predicciones.get(index);
}
    public void addPrediccion(int prediccion) {
        this.predicciones.add(prediccion);
    }
}
