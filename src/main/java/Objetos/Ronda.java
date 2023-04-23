package Objetos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
public class Ronda {
    @Setter
    @Getter
    private int nro;
    @Getter
    private ArrayList<Partido> partidos;
    public Ronda() {
        this.partidos=new ArrayList<>();
    }


    public Partido getPartido(int id){
        return partidos.get(id);
    }

    public void addPartido(Partido partido) {
        this.partidos.add(partido);
    }
}
