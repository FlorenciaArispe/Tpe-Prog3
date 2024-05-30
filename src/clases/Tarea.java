package clases;

public class Tarea {
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

    public void setNombreTarea(String nombreTarea) {
        this.nombreTarea = nombreTarea;
    }

    public void setTiempoEjecucion(Integer tiempoEjecucion) {
        this.tiempoEjecucion = tiempoEjecucion;
    }

    public void setEs_critica(boolean es_critica) {
        this.es_critica = es_critica;
    }

    public void setNivelPrioridad(Integer nivelPrioridad) {
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
    public String toString() {
        return "Tarea: " +
                "idTarea='" + idTarea + '\'' +
                ", nombreTarea='" + nombreTarea + '\'' +
                ", tiempoEjecucion=" + tiempoEjecucion +
                ", es_critica=" + es_critica +
                ", nivelPrioridad=" + nivelPrioridad;
    }
}
