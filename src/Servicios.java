import clases.Tarea;
import utils.CSVReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NO modificar la interfaz de esta clase ni sus métodos públicos.
 * Sólo se podrá adaptar el nombre de la clase "Tarea" según sus decisiones
 * de implementación.
 */
public class Servicios {
    Map<String, Tarea> tareas= new HashMap<>();

    /*
     * Expresar la complejidad temporal del constructor.
     */
    public Servicios(String pathProcesadores, String pathTareas)
    {
        CSVReader reader = new CSVReader();
        ArrayList<Tarea> tareasNuevas= reader.readTasks(pathTareas);
        for(Tarea t: tareasNuevas){
            tareas.put(t.getIdTarea(), t);
        }
        //reader.readProcessors(pathProcesadores);

    }

    /*
     * Expresar la complejidad temporal del servicio 1:
     * La complejidad es O(1) ya que con la estructura HashMap se puede acceder directo a la clave, que en nuestro caso es el ID_TAREA
     */
    public Tarea servicio1(String ID) {
        return tareas.get(ID) != null ? tareas.get(ID) : null ;
    }

    /*
     * Expresar la complejidad temporal del servicio 2.
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
     * Expresar la complejidad temporal del servicio 3.
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
        //ADD EXCEPTION
        return resultado;
    }

}
