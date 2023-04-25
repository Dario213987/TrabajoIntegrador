import Objetos.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;

public class Programa {
    protected static ArrayList<String> resultados = new ArrayList<>();
    protected static ArrayList<String> pronostico = new ArrayList<>();
    private static int puntosExtraRonda;
    private static int puntosFase;
    protected static ArrayList<Ronda> rondas = new ArrayList<>();
    protected static ArrayList<Persona> personas = new ArrayList<>();
    private static Connection con;



    public static void main(String[] args) {


        Scanner sc = new Scanner(System.in);
        int tamanoConfig = 5;
        int tamanoPronostico = 8;
        try {
            System.out.println("Introducir el camino al archivo con el pronostico:");
            Path pronosticoFile = Path.of(sc.nextLine());
            System.out.println("Introducir el camino al archivo con las configuracines:");
            Path config = Path.of(sc.nextLine());
            cargadorDeArchivos(config, tamanoConfig);
            cargadorDeArchivos(pronosticoFile, tamanoPronostico);
            creadorDeRondas();
            cargarPersonas();
            contarPuntos(rondas, personas);
            for (Persona persona : personas) {
                System.out.println("Nombre: " + persona.getNombre());
                System.out.println("Pronosticos acertados: " + persona.getAciertos());
                System.out.println("Puntos: " + persona.getPuntos());
                System.out.println("Puntos extra por acertar una ronda: "+persona.getPuntosExtraPorRonda());
                System.out.println("Puntos extra por acerta una fase: "+persona.getPuntosExtraPorFase());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void cargadorDeArchivos(Path directorio, int size) throws Exception {
        if ((Files.isReadable(directorio)) || !(Files.isDirectory(directorio))) {
            ArrayList<String> tmp = (ArrayList<String>) Files.readAllLines(directorio);
            for (int i = 0; i< tmp.size();i++) {
                String[] split = tmp.get(i).split(",");
                if (split.length != size) {
                    System.out.println("Formato de archivo incorrecto");
                    throw new IllegalArgumentException();
                }
                if (size == 5) {
                    String[] dbSplit = Files.readAllLines(directorio).get(0).split(",");
                    puntosExtraRonda = Integer.parseInt(dbSplit[3]);
                    puntosFase = Integer.parseInt(dbSplit[4]);
                    Class.forName("com.mysql.jdbc.Driver");
                    con = DriverManager.getConnection(dbSplit[0], dbSplit[1], dbSplit[2]);
                    System.out.println("Conexión establecida");
                    Statement declaracion = con.createStatement();
                    ResultSet rs = declaracion.executeQuery("select * from RESULTADOS");
                    while (rs.next()) {
                        resultados.add(rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3) + "," + rs.getString(4) + "," + rs.getString(5) + "," + rs.getString(6));

                    }
                }
                if (size == 8 && i>0) {
                    pronostico.add(tmp.get(i));
                    Class.forName("com.mysql.jdbc.Driver");
                    Statement st = con.createStatement();
                    st.executeUpdate("INSERT INTO PRONOSTICO VALUES ('"+ split[0] + "', '" + split[1] + "','" + split[2] + "','" + split[3] + "','" + split[4] + "','" + split[5] + "','" + split[6] + "','" + split[7]+"')");
                }
            }
        } else {
            System.out.println("El archivo de resultados no existe o no se tiene acceso a el mismo");
            throw new IllegalArgumentException();
        }


    }

    public static void creadorDeRondas() {
        Ronda rondaHolder = new Ronda();
        rondaHolder.setNro(1);
        for (int i = 0; i < resultados.size(); i++) {
            String[] temp = resultados.get(i).split(",");
            if (Integer.parseInt(temp[0]) != rondaHolder.getNro()) {
                rondas.add(rondaHolder);
                rondaHolder = new Ronda();
                rondaHolder.setNro(Integer.parseInt(temp[0]));
                rondaHolder.addPartido(new Partido(Integer.parseInt(temp[1]), new Equipo(temp[2]), new Equipo(temp[5]), Integer.parseInt(temp[3]), Integer.parseInt(temp[4])));
            } else {
                rondaHolder.addPartido(new Partido(Integer.parseInt(temp[1]), new Equipo(temp[2]), new Equipo(temp[5]), Integer.parseInt(temp[3]), Integer.parseInt(temp[4])));
            }
            if (i == resultados.size() - 1) {
                rondaHolder.addPartido(new Partido(Integer.parseInt(temp[1]), new Equipo(temp[2]), new Equipo(temp[5]), Integer.parseInt(temp[3]), Integer.parseInt(temp[4])));
                rondas.add(rondaHolder);
            }
        }
    }


    public static void  cargarPersonas() {
        Persona persona = null;
        ArrayList<Integer> holder = new ArrayList<>();
        int nroDeRonda = 1;
        for (int i = 0; i < pronostico.size(); i++) {
            String[] temp = pronostico.get(i).split(",");
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
            if (i == pronostico.size() - 1) {
                persona.addPrediccion(new PrediccionesPorRonda(nroDeRonda, holder));
                personas.add(persona);
            }

        }
    }
    public static void contarPuntos(ArrayList<Ronda> rondas, ArrayList<Persona> personas) {

        for (Persona persona : personas) {

            int puntosfinales = 0;
            int puntosERonda=0;
            int cantDePartidos = 0;
            ArrayList<PrediccionesPorRonda> prediccionesPorRondas = persona.getPredicciones();
            for (int i = 0; i < rondas.size(); i++) {
                int puntosronda = 0;
                PrediccionesPorRonda prediccion = prediccionesPorRondas.get(i);
                Ronda ronda = rondas.get(i);
                for (int e = 0; e < prediccion.getPredicciones().size(); e++) {
                    if (ronda.getPartido(e).resultado() == prediccion.getPrediccion(e)) {
                        puntosronda++;
                    }
                    cantDePartidos++;
                }
                puntosfinales =puntosfinales + puntosronda;
                if(puntosronda==ronda.getPartidos().size()){
                    puntosERonda++;
                }
            }
            persona.setAciertos(puntosfinales);
            if(puntosfinales==cantDePartidos){
                puntosfinales=puntosfinales+puntosFase;
                persona.setPuntosExtraPorFase(puntosFase);
            }
            persona.setPuntos(puntosfinales+puntosERonda*puntosExtraRonda);
            persona.setPuntosExtraPorRonda(puntosERonda);
        }
    }

}
