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
        arrayTareas.addAll(tareas.values());
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
        if(prioridadInferior >= 1 && prioridadSuperior <= 100){
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
    }
    // El método obtiene cada tarea del arreglo de tareas y las va agregando a una solucion parcial según si se puede asignar al
    // procesador. En caso de que la tarea sea crítica actualiza la cantidad de críticas en el procesador, también actualiza el
    // tiempo y verifica que el procesador sea válido para corroborar que sea una posible solución.
    // Si se encuentra una posible solución ( cuando se asignaron todas las tareas a los procesadores ) y el tiempo mayor del
    // procesador es menor que el mejor tiempo hasta ahora, se copia la "solución parcial" que veníamos registrando a una solución final.
    // Se puede acceder a través de "getSolucionFinal()" para visualizar el resultado.
    private void backtracking(int tiempoX){
        if(arrayTareas.isEmpty()) {
            //                      O(n)
            int tiempoMayor = pMayorTiempo(solucionParcial);
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
                    if(procesadorValido(p, tiempoX) && pMayorTiempo(solucionParcial) < mejorTiempo){
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

    // Este método vuelve a resetear los valores del tiempo máximo de ejecución y la métrica y vuelve a cargar la solución
    // final con los procesadores como clave. Luego ordena las tareas por tiempo de ejecución para poder asignarlas a cada
    // procesador de mayor a menor. Ya que al colocar las tareas que más tiempo de ejecución contengan primero, las de menor
    // tiempo irán equilibrando la solucion.
    public void asignarTareaGreedy(int tiempoX){
        tiempoMaximoDeEjecucion = 0;
        cantidadDeEstados = 0;
        cargarSolucionFinalConProc();
        Collections.sort(arrayTareas);
        greedy(tiempoX);
    }
    // Este método recorre las tareas y obtiene el procesador que menor tiempo tenga hasta el momento y sea válido, si
    // existe el procesador, le setea el tiempo de la tarea, actualiza la cantidad de tareas críticas del propio procesador
    // y lo agrega a la solución final, luego obtiene el procesador con mayor tiempo.
    // Si el procesador no existe, se resetea la solución final con los procesadores como clave y finaliza el recorrido de
    // las tareas.
    private void greedy(int tiempoX){
        int tarea = 0;
        while(tarea < arrayTareas.size()) {
            Tarea t = arrayTareas.get(tarea); //agarro la tarea q esta en el indice while
            Procesador pMenorTiempo = getProcesadorMenorTiempo(tiempoX, t);

            if(pMenorTiempo != null) { //que no me traiga un null como procesador
                pMenorTiempo.setTiempoDeEjecucion(t.getTiempoEjecucion());
                actualizarCriticas(t, pMenorTiempo, 1);
                solucionFinal.get(pMenorTiempo).add(t);
                tiempoMaximoDeEjecucion = pMayorTiempo(solucionFinal);
            }
            else {
                cargarSolucionFinalConProc();
                tarea = arrayTareas.size();
            }
            tarea++;
        }
    }
    private void cargarSolucionFinalConProc(){
        solucionFinal.clear();
        tiempoMaximoDeEjecucion = 0;
        for (Procesador p : procesadores.values()) {
            solucionFinal.put(new Procesador(p.getId(), p.getCodigo(), p.getRefrigerado(), p.getAnio(),
                              p.getCantTareasCriticas(), p.getTiempoDeEjecucion()), new LinkedList<>());
        }
    }
    private int pMayorTiempo(Map<Procesador, LinkedList<Tarea>> solucion){
        int tiempoMayor = 0;
            for (Procesador proc : solucion.keySet()) {
                int tiempoProcesador = proc.getTiempoDeEjecucion();
                if (tiempoProcesador > tiempoMayor)
                    tiempoMayor = tiempoProcesador;
            }
        return tiempoMayor;
    }
    private boolean procesadorValido(Procesador p, int tiempoX){
        return p.getRefrigerado() || p.getTiempoDeEjecucion() <= tiempoX;
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
            cantidadDeEstados++;
            int tiempoTotalDelProcesador = p.getTiempoDeEjecucion() + t.getTiempoEjecucion();
            if(sePuedeAsignar(t, p)) {
                if ((tiempoTotalDelProcesador <= tiempoX || p.getRefrigerado()) && p.getTiempoDeEjecucion() < tiempoMenor) {
                    tiempoMenor = p.getTiempoDeEjecucion();
                    indiceProcesador = p;
                }
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