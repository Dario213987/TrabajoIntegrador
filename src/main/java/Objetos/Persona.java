package Objetos;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class Persona {
    @Getter
    @Setter
    private String nombre;
    @Getter
    @Setter
    private ArrayList<PrediccionesPorRonda> predicciones;
    @Setter
    @Getter
    private int puntos;
    @Setter
    @Getter
    private int puntosExtraPorRonda;
    @Setter
    @Getter
    private int PuntosExtraPorFase;
    @Setter
    @Getter
    private int aciertos;

    public Persona() {
        this.predicciones = new ArrayList<>();
    }

    public void addPrediccion(PrediccionesPorRonda prediccion){
        this.predicciones.add(prediccion);
    }
}
