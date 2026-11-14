package controlador;

import dao.AlumnoDao;
import dao.EntregaDao;
import dao.PracticaDao;
import excepciones.ExcepcionPresentacion;
import pojos.Entrega;
import pojos.Practica;

import java.sql.SQLException;
import java.util.Date;

// ============================================================
// CONTROLADOR de la operación B2: PRESENTAR UNA PRÁCTICA.
//
// Es como un portero de discoteca: antes de dejar entrar la entrega (guardarla),
// revisa 3 reglas. Si una no se cumple, lanza una ExcepcionPresentacion y NO guarda nada.
//
// 1. La fecha de entrega NO puede ser posterior a la fecha límite.
// 2. La nota NO puede ser mayor que los puntos máximos.
// 3. El alumno NO puede haber entregado esa práctica antes.
// ============================================================
public class PresentarPractica {

    // Nota máxima posible. La tabla no lo guarda, así que lo suponemos (10 puntos).
    public static final double PUNTOS_MAXIMOS = 10.0;

    // Los "trabajadores" que hablan con la base de datos.
    private final PracticaDao practicaDao = new PracticaDao();
    private final AlumnoDao alumnoDao = new AlumnoDao();
    private final EntregaDao entregaDao = new EntregaDao();

    // "throws" = esta función puede fallar de dos formas: error de base de datos (SQLException)
    // o regla de negocio incumplida (ExcepcionPresentacion). Quien la llame deberá gestionarlo.
    public void presentar(int idPractica, String nombreAlumno, Date fecha, double nota)
            throws SQLException, ExcepcionPresentacion {

        // PASO 0a: buscar la práctica. Si no existe, no seguimos.
        Practica practica = practicaDao.buscarPorId(idPractica);
        if (practica == null) {  // "== null" significa "no existe"
            throw new ExcepcionPresentacion("no existe ninguna práctica con código " + idPractica + ".");
        }

        // PASO 0b: buscar el alumno por nombre. Si no existe, tampoco seguimos.
        Integer idAlumno = alumnoDao.buscarIdPorNombre(nombreAlumno);
        if (idAlumno == null) {
            throw new ExcepcionPresentacion("no existe ningún alumno llamado " + nombreAlumno + ".");
        }

        // REGLA 1: fecha. "fecha.after(x)" significa "¿fecha es DESPUÉS de x?". Si sí -> tarde -> error.
        if (fecha.after(practica.getFechaLimite())) {
            throw new ExcepcionPresentacion("estás fuera del límite de entrega.");
        }

        // REGLA 2: nota. No puede ser negativa ni mayor que 10.
        if (nota < 0 || nota > PUNTOS_MAXIMOS) {
            throw new ExcepcionPresentacion("la nota está fuera de rango (0 - " + PUNTOS_MAXIMOS + ").");
        }

        // REGLA 3: ¿ya la entregó antes? Si ya existe -> error.
        if (entregaDao.existeEntrega(idPractica, idAlumno)) {
            throw new ExcepcionPresentacion("el alumno ya había entregado esta práctica.");
        }

        // Si llegamos aquí, todo está bien: guardamos la entrega.
        // El 0 es el id de la entrega: la base de datos le pone el número real automáticamente.
        entregaDao.insertar(new Entrega(0, idPractica, idAlumno, fecha, nota));
    }
}
