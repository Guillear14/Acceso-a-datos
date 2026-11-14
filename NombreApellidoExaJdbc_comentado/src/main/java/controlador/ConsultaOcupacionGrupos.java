package controlador;

import dao.GrupoDao;
import pojos.OcupacionGrupo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// ============================================================
// CONTROLADOR de la consulta B31 (ocupación de grupos).
//
// El CONTROLADOR decide QUÉ hacer con los datos (las reglas del negocio).
// Aquí la regla es: si hay menos de 5 alumnos -> BAJA; si hay 5 o más -> ALTA.
// No habla con la base de datos directamente: eso lo hace el DAO (GrupoDao).
// ============================================================
public class ConsultaOcupacionGrupos {

    // Límite: a partir de 5 alumnos el grupo está "ALTA" ocupación.
    private static final int LIMITE_ALTA = 5;

    // Pedimos los datos al DAO. Creamos el objeto una vez para toda la clase.
    private final GrupoDao grupoDao = new GrupoDao();

    // Devuelve una lista de TEXTOS listos para mostrar, por ejemplo: "G1-Jueves- ocupación BAJA"
    public List<String> obtenerOcupacion(String codigoAsignatura) throws SQLException {
        List<String> lineas = new ArrayList<>();   // aquí guardaremos los textos finales

        // Recorremos cada grupo que nos devuelve la base de datos.
        for (OcupacionGrupo g : grupoDao.ocupacionPorAsignatura(codigoAsignatura)) {
            // Regla: menos de 5 alumnos -> "BAJA"; si no -> "ALTA".  (? : es un "si... entonces... si no...")
            String ocupacion = g.getTotalMatriculas() < LIMITE_ALTA ? "BAJA" : "ALTA";
            // Montamos el texto final pegando trozos: código - día - ocupación.
            lineas.add(g.getCodigoGrupo() + "-" + g.getDia() + "- ocupación " + ocupacion);
        }
        return lineas;
    }
}
