package clases;

public class Tarea implements Comparable<Tarea> {

    private String idTarea;
    private String nombreTarea;
    private Integer tiempoEjecucion;
    private Boolean es_critica;
    private Integer nivelPrioridad;

    public Tarea(){}

    public Tarea(String idTarea, String nombreTarea, Integer tiempoEjecucion, Boolean es_critica, Integer nivelPrioridad) {
        this.idTarea = idTarea;
        this.nombreTarea = nombreTarea;
        this.tiempoEjecucion = tiempoEjecucion;
        this.es_critica = es_critica;
        this.nivelPrioridad = nivelPrioridad;
    }

    public String getIdTarea() {
        return idTarea;
    }

    public Integer getTiempoEjecucion() {
        return tiempoEjecucion;
    }

    public boolean isEs_critica() {
        return es_critica;
    }

    public Integer getNivelPrioridad() {
        return nivelPrioridad;
    }

    public String getNombreTarea() {
        return nombreTarea;
    }

    @Override
    public int compareTo(Tarea t2) {
        return t2.getTiempoEjecucion() - this.getTiempoEjecucion();
    }
    @Override
    public String toString() {
        return "Tarea: " + idTarea +
                ", tEj: " + tiempoEjecucion +
                " es critica: " + isEs_critica() +
                ", nivelPrioridad: " + nivelPrioridad;
    }
}
