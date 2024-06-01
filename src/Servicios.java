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
    private ArrayList<Tarea> arrayTareas;
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
        arrayTareas = new ArrayList<>();
        arrayTareas.addAll(tareas.values());
        // para ordenar las tareas
        //ordenarArrayTareas(arrayTareas);
        procesadorMasRapido = null;
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
    // ESTO HICIMOS EL VIERNES EN CLASE CON LO QUE NOS DIJO SOLE
     public void asignarTarea(){
        Map<String, LinkedList<Tarea>> solucionParcial = new HashMap<>(asignados);
        asignarTarea(solucionParcial);
    }
    private void asignarTarea(Map<String, LinkedList<Tarea>> solucionParcial){
        // ya ta vacio
        if(arrayTareas.isEmpty()) {
            int tiempoProcesador = 0;
            for(String id : solucionParcial.keySet()){
                Procesador proc = procesadores.get(id);
                tiempoProcesador = proc.getTiempoDeEjecucion();
                if(tiempoProcesador<mejorTiempo){
                    mejorTiempo = tiempoProcesador;
                    asignados.get(proc.getId()).addAll(solucionParcial.get(proc.getId()));
                    //asignados = new HashMap<>(solucionParcial);
                }
            }
            mejorTiempo = Integer.MAX_VALUE;
        }
        else {
            Tarea tarea = arrayTareas.removeFirst();
            //             p1
            for(Procesador p :procesadores.values()){
                //                   tarea 10
                // p1 me queda con las 10 tareas
                solucionParcial.get(p.getId()).add(tarea);
                // p1 tiempo de ejecucion = 500.
                // p2 p3 p4 = 0.
                p.setTiempoDeEjecucion(tarea.getTiempoEjecucion());
                //           p1      p1 = t10.
                asignarTarea(solucionParcial);
                solucionParcial.get(p.getId()).removeLast();
                p.setTiempoDeEjecucion(-tarea.getTiempoEjecucion());
            }
        }
    }




    /*public void asignarTarea(){
        if(arrayTareas.isEmpty()){

        }
        else {
            Tarea tarea = arrayTareas.removeFirst();
            for(Procesador p : procesadores.values()){
                asignados.get(p.getId()).add(tarea);
                p.setTiempoDeEjecucion(tarea.getTiempoEjecucion());
                if(p.getTiempoDeEjecucion() < mejorTiempo){
                    mejorTiempo = p.getTiempoDeEjecucion();

                }
                asignarTarea();
                p.setTiempoDeEjecucion(-tarea.getTiempoEjecucion());
                asignados.get(p.getId()).removeLast();
            }
        }
    }*/

    /*public void asignarTarea(){

        asignarTarea(null, null);
    }
    private void asignarTarea(Tarea t, Procesador procesador){
        if(arrayTareas.isEmpty()){
            asignados.get(procesador.getId()).add(t);

        }
        else{
            Tarea tarea = arrayTareas.removeFirst(); // agarro mi primer tarea y la saco del array
            for(Procesador p :procesadores.values()){
                asignados.get(p.getId()).add(tarea);
                p.setTiempoDeEjecucion(tarea.getTiempoEjecucion());
                if(p.getTiempoDeEjecucion() < mejorTiempo){
                    mejorTiempo = p.getTiempoDeEjecucion();
                }
                asignarTarea(tarea,p);
                asignados.get(p.getId()).removeLast();
                p.setTiempoDeEjecucion(-tarea.getTiempoEjecucion());
            }
        }
    }*/

    /*public void ordenarArrayTareas(ArrayList<Tarea> arrayTareas){
        mergeSort(arrayTareas, 0, arrayTareas.size()-1);
        System.out.println("array ordenado supueeeeeestamente" + arrayTareas);
    }
    //Ordenamiento MergeSort: divide en 2, ordena la parte izquierda, ordena la derecha, y junta ambas ordenadas.
    public void mergeSort(ArrayList<Tarea> arr, int inicio,int fin){
        if(inicio<fin){ //CASO BASE: el array tiene un solo elemento (inicio mayor que fin)
            int medio= (inicio+fin)/2;
            mergeSort(arr,inicio, medio); // ordena la mitad izquierda del array
            mergeSort(arr,medio+1, fin); // ordena la mitad derecha del array
            merge(arr, inicio, medio, fin); // combina ambas mitades ordenadas
        }
    }

    public void merge( ArrayList<Tarea> arr, int inicio, int medio, int fin){
        ArrayList<Tarea> aux = new ArrayList<>(fin - inicio + 1);

        int i = inicio;
        int j = medio+1;
        int k = inicio;

        while( i <= medio && j <= fin){
            if( aux.get(i).getTiempoEjecucion() <= aux.get(j).getTiempoEjecucion()) {
                //arr.get(k) = aux.get(i);
                arr.add(k, aux.get(i));
                i++;
            }
            else{
                arr.add(k, aux.get(j));
                j++;
            }
            k++;
        }
        //si quedan elementos copiarlos en el array original
        while(i <= medio){
            arr.add(k, aux.get(i));
            k++;
            i++;
        }
        while (j<=fin){
            arr.add(k, aux.get(j));
            k++;
            j++;
        }
    }*/
    /*public void asignarTarea(){
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
    }*/

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