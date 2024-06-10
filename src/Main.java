import clases.Tarea;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Servicios servicios = new Servicios("./src/datasets/Procesadores.csv", "./src/datasets/Tareas.csv");
        System.out.println("Servicio 1:");
        System.out.println(servicios.servicio1("T1"));
        System.out.println("Servicio 2:");
        List<Tarea> servicio2 = servicios.servicio2(true);
        if(!servicio2.isEmpty())
            for(Tarea t : servicio2)
                System.out.println(t);
        else
            System.out.println("No existen tareas que cumplan con ese criterio.");

        System.out.println("Servicio 3:");
        List<Tarea> servicio3= servicios.servicio3(50,60);
        if(!servicio3.isEmpty())
            for(Tarea t : servicio3)
                System.out.println(t);
        else
            System.out.println("Ingrese niveles de prioridad entre 1 y 100.");


        System.out.println("Servicio 4:");
        servicios.asignarTareaBacktraking(80);
        if(servicios.getTiempoMaximoDeEjecucion() == 0)
            System.out.println("No se pudo asignar las tareas en Backtracking.");
        else {
            System.out.println("Solución Final Backtracking: " + servicios.getSolucionFinal());
            System.out.println("Tiempo Maximo de Ejecución: " + servicios.getTiempoMaximoDeEjecucion());
            System.out.println("Métrica: " + servicios.getCantidadDeEstados());
        }


        servicios.asignarTareaGreedy(80);
        if(servicios.getTiempoMaximoDeEjecucion() == 0)
            System.out.println("No se pudo asignar las tareas en Greedy.");
        else {
            System.out.println("Solución Final greedy: " + servicios.getSolucionFinal());
            System.out.println("Tiempo Maximo de Ejecución: " + servicios.getTiempoMaximoDeEjecucion());
            System.out.println("Métrica Greedy: " + servicios.getCantidadDeEstados());
        }
    }
}