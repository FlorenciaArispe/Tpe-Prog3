import clases.Procesador;
import clases.Tarea;
import utils.CSVReader;

import java.util.*;

/**
 * NO modificar la interfaz de esta clase ni sus métodos públicos.
 * Sólo se podrá adaptar el nombre de la clase "Tarea" según sus decisiones
 * de implementación.
 */
public class Servicios {
    private Map<String, Tarea> tareas;
    private Map<String, Procesador> procesadores;
    private Map<String, LinkedList<Tarea>> asignados;
    private Procesador procesadorMasRapido;
    private int mejorTiempo;
    /*
     * Expresar la complejidad temporal del constructor.
     */
    public Servicios(String pathProcesadores, String pathTareas)
    {
        CSVReader reader = new CSVReader();
        tareas = reader.readTasks(pathTareas);
        procesadores = reader.readProcessors(pathProcesadores);
        asignados = new HashMap<>();
        procesadorMasRapido= null;
        mejorTiempo=Integer.MAX_VALUE;
        for (String id : procesadores.keySet()) {
            asignados.put(id, new LinkedList<>());
        }
    }

    /*
     * SERVICIO 1:
     * La complejidad es O(1) ya que con la estructura HashMap se puede acceder directo a la clave, que en nuestro caso es el ID_TAREA
     */
    public Tarea servicio1(String ID) {
        return tareas.get(ID) != null ? tareas.get(ID) : null ;
    }

    /*
     * SERVICIO 2:
     * La complejidad es O(n) ya que en el peor de los casos hay que recorrer toda la tabla verificando si es o no critica, siendo n la cantidad de Tareas.
     */
    public List<Tarea> servicio2(boolean esCritica) {
        List<Tarea> resultado= new ArrayList<>();
        for (String clave : tareas.keySet()) {
            Tarea tarea = tareas.get(clave);
            if(tarea.isEs_critica()== esCritica){
                resultado.add(tarea);
            }
        }
        return resultado;
    }

    /*
     * SERVICIO 3:
     *La complejidad es O(n) ya que en el peor de los casos hay que recorrer toda la tabla verificando si el nivel de prioridad esta entre los valores, siendo n la cantidad de Tareas.
     */
    public List<Tarea> servicio3(int prioridadInferior, int prioridadSuperior) {
        List<Tarea> resultado= new ArrayList<>();
        if(prioridadInferior>=1 && prioridadSuperior<=100){
            for (String clave : tareas.keySet()) {
                Tarea tarea = tareas.get(clave);
                if(tarea.getNivelPrioridad()>= prioridadInferior && tarea.getNivelPrioridad() <= prioridadSuperior){
                    resultado.add(tarea);
                }
            }
        }
        return resultado;
    }


//PARTE 2
    public void asignarTarea(){
        ArrayList<String> visitados = new ArrayList<>();

        Iterator<String> it_Tareas = tareas.keySet().iterator();
        while(it_Tareas.hasNext()) {
            String idTarea = it_Tareas.next();
            //System.out.println(idTarea);
            Tarea tarea = tareas.get(idTarea);
            asignarTarea(tarea,visitados);
            visitados.clear();
        }
    }
    private void asignarTarea(Tarea t, ArrayList<String> visitados){
        Procesador procesadorNuevo = obtenerProcesadorNoVisitado(visitados);

        //SI NO TENGO MAS PROCESADORES YA VOY A SABER EN CUAL TENGO QUE PONER LA TAREA
        if(procesadorNuevo==null) {
            asignados.get(procesadorMasRapido.getId()).add(t); //AGREGO LA TAREA AL PROCESADOR MAS RAPIDO
            procesadorMasRapido.setTiempoDeEjecucion(t.getTiempoEjecucion()); //REGISTRO SU NUEVO TIEMPO DE EJECUCION
            mejorTiempo=Integer.MAX_VALUE; //INICALIZO MEJOR TIEMPO NUEVAMENTE PARA LA PROXIMA TAREA
        }
        else {
            visitados.add(procesadorNuevo.getId());
            asignados.get(procesadorNuevo.getId()).add(t);
            procesadorNuevo.setTiempoDeEjecucion(t.getTiempoEjecucion());

            if(procesadorNuevo.getTiempoDeEjecucion()< mejorTiempo){
                mejorTiempo= procesadorNuevo.getTiempoDeEjecucion();
                procesadorMasRapido= procesadorNuevo;
            }

            asignarTarea(t,visitados);
            asignados.get(procesadorNuevo.getId()).remove(t);
            procesadorNuevo.setTiempoDeEjecucion(-t.getTiempoEjecucion());
        }
    }

    public Map<String, LinkedList<Tarea>> getAsignados() {
        return asignados;
    }
    private Procesador obtenerProcesadorNoVisitado(ArrayList<String> visitados){
        for(Procesador p : procesadores.values()){
            if(!visitados.contains(p.getId()))
                return p;
        }
        return null;
    }
}