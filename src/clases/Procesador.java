package clases;

public class Procesador {
    private String id;
    private String codigo;
    private Boolean refrigerado;
    private Integer anio;
    private int cantTareasCriticas;
    private int tiempoDeEjecucion;

    public Procesador(){
        this.cantTareasCriticas = 0;
        this.tiempoDeEjecucion = 0;
    }
    public Procesador(String id, String codigo, Boolean refrigerado, Integer anio) {
        this.id = id;
        this.codigo = codigo;
        this.refrigerado = refrigerado;
        this.anio = anio;
        this.cantTareasCriticas = 0;
        this.tiempoDeEjecucion = 0;
    }

    public String getId() {
        return id;
    }
    public String getCodigo() {
        return codigo;
    }
    public Boolean getRefrigerado() {
        return refrigerado;
    }
    public Integer getAnio() {
        return anio;
    }

    public int getCantTareasCriticas() {
        return cantTareasCriticas;
    }
    public void setCantTareasCriticas() {
        this.cantTareasCriticas++;
    }

    public int getTiempoDeEjecucion() {
        return tiempoDeEjecucion;
    }

    public void setTiempoDeEjecucion(int tiempoDeEjecucion) {
        this.tiempoDeEjecucion += tiempoDeEjecucion;
    }

    @Override
    public String toString() {
        return "Procesador{" +
                "id='" + id + '\'' +
                ", codigo='" + codigo + '\'' +
                ", refrigerado=" + refrigerado +
                ", anio=" + anio +
                '}';
    }
}
