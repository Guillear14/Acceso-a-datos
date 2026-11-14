package pojos;

import java.util.Date;

// ============================================================
// POJO "Entrega": una ficha que representa UNA fila de la tabla entregas.
// Una entrega = un alumno que presenta una práctica en una fecha con una nota.
// ============================================================
public class Entrega {

    // Columnas de la tabla entregas
    private int idEntrega;     // número de la entrega (lo pone la base de datos)
    private int idPractica;    // qué práctica se entrega
    private int idAlumno;      // quién la entrega
    private Date fechaEntrega; // cuándo la entrega
    private double nota;       // la nota (ej: 7.5)

    // Constructor: crea la ficha con todos sus datos.
    public Entrega(int idEntrega, int idPractica, int idAlumno, Date fechaEntrega, double nota) {
        this.idEntrega = idEntrega;
        this.idPractica = idPractica;
        this.idAlumno = idAlumno;
        this.fechaEntrega = fechaEntrega;
        this.nota = nota;
    }

    // Getters: leer cada dato de la ficha.
    public int getIdEntrega()     { return idEntrega; }
    public int getIdPractica()    { return idPractica; }
    public int getIdAlumno()      { return idAlumno; }
    public Date getFechaEntrega() { return fechaEntrega; }
    public double getNota()       { return nota; }
}
