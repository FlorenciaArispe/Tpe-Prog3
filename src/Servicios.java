import clases.Mergesort;
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
    private Map<Procesador, LinkedList<Tarea>> solucionFinal;
    private Map<Procesador, LinkedList<Tarea>> solucionParcial;
    private ArrayList<Tarea> arrayTareas;
    private ArrayList<Tarea> tareasCriticas;
    private ArrayList<Tarea> tareasNoCriticas;
    private ArrayList<Procesador> arrayProcesadores;
    private int mejorTiempo;
    private int cantidadDeEstados;
    private int tiempoMaximoDeEjecucion;
    /*
     * Expresar la complejidad temporal del constructor.
     */
    public Servicios(String pathProcesadores, String pathTareas)
    {
        CSVReader reader = new CSVReader();
        tareas = reader.readTasks(pathTareas);
        procesadores = reader.readProcessors(pathProcesadores);
        solucionFinal = new HashMap<>();
        arrayTareas = new ArrayList<>();
        arrayProcesadores = new ArrayList<>();
        arrayTareas.addAll(tareas.values());
        arrayProcesadores.addAll(procesadores.values());
        tareasCriticas = new ArrayList<>();
        tareasNoCriticas = new ArrayList<>();
        for(Tarea t : tareas.values()){
            if(t.isEs_critica())
                tareasCriticas.add(t);
            else
                tareasNoCriticas.add(t);
        }
        mejorTiempo=Integer.MAX_VALUE;
        solucionParcial = new HashMap<>();
        for (Procesador p : procesadores.values()) {
            solucionParcial.put(p, new LinkedList<>());
        }
        cantidadDeEstados = 0;
        tiempoMaximoDeEjecucion = 0;
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
     * La complejidad es O(1) porque directamente copia el arreglo de tareas ya sean críticas o no críticas.
     */
    public List<Tarea> servicio2(boolean esCritica) {
        List<Tarea> resultado;
        if(esCritica)
            resultado = new ArrayList<>(tareasCriticas);
        else
            resultado = new ArrayList<>(tareasNoCriticas);

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
                if(tarea.getNivelPrioridad()>= prioridadInferior && tarea.getNivelPrioridad() <= prioridadSuperior)
                    resultado.add(tarea);

            }
        }
        return resultado;
    }

    // PARTE 2
    public void asignarTareaBacktraking(int tiempoX) {
        if(!(tareasCriticas.size() / 2 > procesadores.size()))
            backtracking(tiempoX);

        else
            System.out.println("No es posible asignar las tareas." );
    }
    private void backtracking(int tiempoX){
        if(arrayTareas.isEmpty()) {
            int tiempoMayor = 0;
            for (Procesador proc : solucionParcial.keySet()) {
                int tiempoProcesador = proc.getTiempoDeEjecucion();
                if (tiempoProcesador > tiempoMayor)
                    tiempoMayor = tiempoProcesador;
            }
            if(tiempoMayor < mejorTiempo) {
                this.solucionFinal = new HashMap<>(solucionParcial.size());
                for(Procesador p : solucionParcial.keySet()){
                    solucionFinal.put(new Procesador(p.getId(), p.getCodigo(), p.getRefrigerado(), p.getAnio(),
                                    p.getCantTareasCriticas(), p.getTiempoDeEjecucion()),
                                    new LinkedList<>(solucionParcial.get(p)));
                }
                this.mejorTiempo = tiempoMayor;
                tiempoMaximoDeEjecucion = mejorTiempo;
            }
        }
        else {
            Tarea tarea = arrayTareas.removeFirst();
            for(Procesador p : solucionParcial.keySet()){
                if(sePuedeAsignar(tarea, p)){
                    solucionParcial.get(p).add(tarea);
                    actualizarCriticas(tarea, p, 1);
                    p.setTiempoDeEjecucion(tarea.getTiempoEjecucion());
                    if(procesadorValido(p, tiempoX)){
                        cantidadDeEstados++;
                        backtracking(tiempoX);
                    }
                    solucionParcial.get(p).removeLast();
                    p.setTiempoDeEjecucion(-tarea.getTiempoEjecucion());
                    actualizarCriticas(tarea, p, -1);
                }
            }
            arrayTareas.add(tarea);
        }
    }

    public void asignarTareaGreedy(int tiempoX){
        tiempoMaximoDeEjecucion = 0;
        cantidadDeEstados = 0;
        solucionFinal.clear();
        for (Procesador p : procesadores.values()) {
            solucionFinal.put(p, new LinkedList<>());
        }
        arrayTareas.sort(new Comparator<Tarea>() {
            @Override
            public int compare(Tarea t1, Tarea t2) {
                return t2.getTiempoEjecucion() - t1.getTiempoEjecucion();
            }
        });
        greedy(tiempoX);
    }
    private void greedy(int tiempoX){
        int tarea = 0;
        // p1 p2 p3 p4 p4 p3 p2 p1 p1 p2 p3 p4
        while(tarea < arrayTareas.size()) {
            Tarea t = arrayTareas.get(tarea); //agarro la tarea q esta en el indice while
            Procesador pMenorTiempo = getProcesadorMenorTiempo(tiempoX, t);
            if (sePuedeAsignar(t, pMenorTiempo)) {
                if (procesadorValido(pMenorTiempo, tiempoX)) {
                    pMenorTiempo.setTiempoDeEjecucion(t.getTiempoEjecucion());
                    actualizarCriticas(t, pMenorTiempo, 1);
                    cantidadDeEstados++;

                    solucionFinal.get(pMenorTiempo).add(t);
                }
            }
            tarea++;
        }
    }

    private boolean procesadorValido(Procesador p, int tiempoX){
        if((!p.getRefrigerado() && p.getTiempoDeEjecucion() <= tiempoX) || p.getRefrigerado())
            return true;
        return false;
    }
    private boolean sePuedeAsignar(Tarea t, Procesador p){
        if(!t.isEs_critica() || (t.isEs_critica() && p.getCantTareasCriticas() < 2)){
            return true;
        }
        return false;
    }
    private void actualizarCriticas(Tarea t, Procesador p, int num){
        if(t.isEs_critica())
            p.setCantTareasCriticas(num);
    }
    private Procesador getProcesadorMenorTiempo(int tiempoX, Tarea t) {
        int tiempoMenor = Integer.MAX_VALUE;
        Procesador indiceProcesador = null;
        for (Procesador p : solucionFinal.keySet()) {
            int tiempoTotalDelProcesador = p.getTiempoDeEjecucion() + t.getTiempoEjecucion();
            if ((tiempoTotalDelProcesador <= tiempoX || p.getRefrigerado()) && p.getTiempoDeEjecucion() < tiempoMenor) {
                tiempoMenor = p.getTiempoDeEjecucion();
                indiceProcesador = p;
            }
        }
        return indiceProcesador;
    }

    public int getCantidadDeEstados() {
        return cantidadDeEstados;
    }
    public int getTiempoMaximoDeEjecucion() {
        return tiempoMaximoDeEjecucion;
    }

    public Map<Procesador, LinkedList<Tarea>> getSolucionFinal() {
        return solucionFinal;
    }
}