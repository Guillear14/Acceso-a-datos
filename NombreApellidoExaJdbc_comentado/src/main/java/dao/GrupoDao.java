package dao;

import pojos.OcupacionGrupo;
import utilies.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// ============================================================
// DAO de GRUPOS: la consulta de OCUPACIÓN de los grupos de una asignatura.
// ============================================================
public class GrupoDao {

    // La consulta SQL, en varias líneas para que se lea mejor.
    // Traducción a lenguaje normal:
    //   "Dame, para cada grupo de la asignatura X, su código, su día y cuántos alumnos tiene."
    //
    // - FROM grupospracticas g        -> miramos la tabla de grupos (le ponemos el apodo "g")
    // - INNER JOIN asignaturas        -> la unimos con asignaturas para filtrar por código
    // - LEFT JOIN matriculas          -> la unimos con las matrículas.
    //                                    LEFT = si un grupo no tiene alumnos, IGUAL aparece (con 0)
    // - WHERE a.codigo = ?            -> solo la asignatura que pedimos
    // - COUNT(m.id_matricula)         -> cuenta los alumnos de cada grupo
    // - GROUP BY                      -> hace una fila de resumen por cada grupo
    private static final String OCUPACION =
            "SELECT g.nombre AS codigo_grupo, g.dia_semana, COUNT(m.id_matricula) AS total " +
            "FROM grupospracticas g " +
            "INNER JOIN asignaturas a ON g.id_asignatura = a.id_asignatura " +
            "LEFT JOIN matriculas m ON m.id_grupo = g.id_grupo " +
            "WHERE a.codigo = ? " +
            "GROUP BY g.id_grupo, g.nombre, g.dia_semana";

    // Devuelve una lista de resúmenes (OcupacionGrupo), uno por cada grupo.
    public List<OcupacionGrupo> ocupacionPorAsignatura(String codigoAsignatura) throws SQLException {
        List<OcupacionGrupo> lista = new ArrayList<>(); // lista vacía para ir rellenando
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(OCUPACION)) {
            ps.setString(1, codigoAsignatura);           // rellenar el "?" con el código de la asignatura
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {                      // por cada fila (cada grupo)...
                    // ...creamos un resumen con sus datos y lo guardamos en la lista.
                    lista.add(new OcupacionGrupo(
                            rs.getString("codigo_grupo"),
                            rs.getString("dia_semana"),
                            rs.getInt("total")));
                }
            }
        }
        return lista;
    }
}
