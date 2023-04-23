import Objetos.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Programa {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int tamanoPartidos = 6;
        int tamanoPronostico = 8;
        try {
            System.out.println("Introducir el camino al archivo con los partidos:");
            Path partidos = Path.of(sc.nextLine());
            verificadorDeArchivos(partidos, tamanoPartidos);
            System.out.println("Introducir el camino al archivo con las predicciones:");
            Path pronostico = Path.of(sc.nextLine());
            verificadorDeArchivos(pronostico, tamanoPronostico);
            ArrayList<Ronda> rondas = creadorDeRondas(partidos);
            ArrayList<Persona> personas = cargarPersonas(pronostico);
            contarPuntos(rondas, personas);
            for (Persona persona : personas) {
                System.out.println("Nombre: " + persona.getNombre());
                System.out.println("Pronosticos acertados: " + persona.getPuntos());
                System.out.println("Puntos: " + persona.getPuntos());
            }

        } catch (Exception e) {
        }
    }

    private static void verificadorDeArchivos(Path directorio, int size) throws Exception {
        if ((Files.isReadable(directorio)) || (Files.isDirectory(directorio))) {
            List<String> tmp = Files.readAllLines(directorio);
            for (int index = 1; index< tmp.size(); index++) {
                String[] split = tmp.get(index).split(",");
                if (split.length != size) {
                    System.out.println("Formato de archivo incorrecto");
                    throw new IllegalArgumentException();
                }
                if(size==6){
                    for(int i = 3;i<split.length-1;i++){
                        if(!split[i].matches("\\d{1,2}")){
                            System.out.println("Formato invalido el campo de goles:");
                            System.out.println(split[0]+","+split[1]+","+split[2]+","+split[3]+","+split[4]+","+split[5]);
                            throw new IllegalArgumentException();
                        }
                    }
                }
            }
        } else {
            System.out.println("El archivo de resultados no existe o no se tiene acceso a el mismo");
            throw new IllegalArgumentException();
        }
    }

    public static ArrayList<Ronda> creadorDeRondas(Path pronostico) throws IOException {
        ArrayList<String> tmp = (ArrayList<String>) Files.readAllLines(pronostico);
        ArrayList<Ronda> rondas = new ArrayList<>();
        Ronda rondaHolder = new Ronda();
        rondaHolder.setNro(1);
        int e = 0;
        for (int i = 1; i < tmp.size(); i++) {
            String[] temp = tmp.get(i).split(",");
            if (Integer.parseInt(temp[0]) != rondaHolder.getNro()) {
                rondas.add(rondaHolder);
                rondaHolder = new Ronda();
                rondaHolder.setNro(Integer.parseInt(temp[0]));
                rondaHolder.addPartido(new Partido(Integer.parseInt(temp[1]), new Equipo(temp[2]), new Equipo(temp[5]), Integer.parseInt(temp[3]), Integer.parseInt(temp[4])));
            } else {
                rondaHolder.addPartido(new Partido(Integer.parseInt(temp[1]), new Equipo(temp[2]), new Equipo(temp[5]), Integer.parseInt(temp[3]), Integer.parseInt(temp[4])));
            }
            if (i == tmp.size() - 1) {
                rondaHolder.addPartido(new Partido(Integer.parseInt(temp[1]), new Equipo(temp[2]), new Equipo(temp[5]), Integer.parseInt(temp[3]), Integer.parseInt(temp[4])));
                rondas.add(rondaHolder);
            }
        }
        return rondas;
    }


    public static ArrayList<Persona> cargarPersonas(Path pronostico) throws IOException {
        ArrayList<String> tmp = (ArrayList<String>) Files.readAllLines(pronostico);
        Persona persona = null;
        ArrayList<Persona> personas = new ArrayList<>();
        ArrayList<Integer> holder = new ArrayList<>();
        int nroDeRonda = 1;
        for (int i = 1; i < tmp.size(); i++) {
            String[] temp = tmp.get(i).split(",");
            if (persona == null) {
                persona = new Persona();
                persona.setNombre(temp[1]);
            }

            if (!(temp[1].equals(persona.getNombre()))) {
                persona.addPrediccion(new PrediccionesPorRonda(nroDeRonda, holder));
                nroDeRonda = Integer.parseInt(temp[0]);
                holder.clear();
                personas.add(persona);
                persona = new Persona();
                persona.setNombre(temp[1]);
            }
            if (Integer.parseInt(temp[0]) != nroDeRonda) {
                persona.addPrediccion(new PrediccionesPorRonda(nroDeRonda, holder));
                nroDeRonda = Integer.parseInt(temp[0]);
                holder.clear();
            }
            for (int e = 4; e < temp.length - 1; e++) {
                if (temp[e].equals("x")) {
                    holder.add(e);
                }
            }
            if (i == tmp.size() - 1) {
                persona.addPrediccion(new PrediccionesPorRonda(nroDeRonda, holder));
                personas.add(persona);
            }

        }
        return personas;
    }
    public static void contarPuntos(ArrayList<Ronda> rondas, ArrayList<Persona> personas) throws IOException {

        for (Persona persona : personas) {

            int puntos = 0;
            ArrayList<PrediccionesPorRonda> prediccionesPorRondas = persona.getPredicciones();
            for (int i = 0; i < rondas.size(); i++) {
                PrediccionesPorRonda prediccion = prediccionesPorRondas.get(i);
                Ronda ronda = rondas.get(i);
                for (int e = 0; e < prediccion.getPredicciones().size(); e++) {
                    if (ronda.getPartido(e).resultado()
                            == prediccion.getPrediccion(e)) {
                        puntos++;
                    }
                }


            }
            persona.setPuntos(puntos);
        }
    }

}
