package dao;

import utilies.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// ============================================================
// DAO de ALUMNOS. Solo tiene lo que necesitamos: buscar el id de un alumno por su nombre.
// ============================================================
public class AlumnoDao {

    // Devuelve el id del alumno con ese nombre, o null si no existe.
    // Se supone que el nombre es único en la base de datos.
    public Integer buscarIdPorNombre(String nombre) throws SQLException {
        String sql = "SELECT id_alumno FROM alumnos WHERE nombre = ?"; // ? = el nombre que buscamos
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);                 // rellenamos el hueco con el nombre
            try (ResultSet rs = ps.executeQuery()) {
                // Si hay fila, devolvemos su id. Si no, devolvemos null.
                return rs.next() ? rs.getInt("id_alumno") : null;
            }
        }
    }
}
