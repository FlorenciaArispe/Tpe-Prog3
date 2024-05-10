import clases.Tarea;

import java.util.ArrayList;
import java.util.List;

public class Main {
        public static void main(String args[]) {
            Servicios servicios = new Servicios("./src/datasets/Procesadores.csv", "./src/datasets/Tareas.csv");

            System.out.println(servicios.servicio1("T1"));

            List<Tarea> servicio2= servicios.servicio2(false);
            for(Tarea t:servicio2){
                System.out.println(t);
            }

            List<Tarea> servicio3= servicios.servicio3(1,78);
            for(Tarea t:servicio3){
                System.out.println(t);
            }
        }

}