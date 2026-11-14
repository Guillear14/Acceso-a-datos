package pojos;

import java.util.Date;

// ============================================================
// POJO "Practica": una ficha que representa UNA fila de la tabla practicas.
// Un POJO es una clase sencilla que solo guarda datos (como una ficha en papel).
// Cada atributo (variable private) es una columna de la tabla.
// ============================================================
public class Practica {

    // Columnas de la tabla practicas
    private int idPractica;      // código de la práctica (ej: 7)
    private int idAsignatura;    // a qué asignatura pertenece
    private String nombre;       // nombre de la práctica
    private String descripcion;  // texto explicativo
    private Date fechaLimite;    // último día para entregarla

    // Constructor: la función que se usa para crear una ficha nueva con todos sus datos.
    public Practica(int idPractica, int idAsignatura, String nombre, String descripcion, Date fechaLimite) {
        this.idPractica = idPractica;      // "this.x" = el atributo de la ficha; "x" = el dato que nos llega
        this.idAsignatura = idAsignatura;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaLimite = fechaLimite;
    }

    // Getters: funciones para LEER un dato de la ficha (no lo cambian).
    public int getIdPractica()     { return idPractica; }
    public int getIdAsignatura()   { return idAsignatura; }
    public String getNombre()      { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Date getFechaLimite()   { return fechaLimite; }

    // toString: cómo se ve la ficha si la imprimimos con System.out.println.
    @Override
    public String toString() {
        return "Practica [id=" + idPractica + ", nombre=" + nombre + ", fechaLimite=" + fechaLimite + "]";
    }
}
