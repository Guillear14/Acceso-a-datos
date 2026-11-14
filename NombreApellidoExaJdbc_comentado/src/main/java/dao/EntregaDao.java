package dao;

import pojos.Entrega;
import utilies.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// ============================================================
// DAO de ENTREGAS: operaciones sobre la tabla entregas (las presentaciones de prácticas).
// ============================================================
public class EntregaDao implements DaoGenerico<Entrega, Integer> {

    // Órdenes SQL. Los "?" son huecos para los datos.
    private static final String INSERT =
            "INSERT INTO entregas (id_practica, id_alumno, fecha_entrega, nota) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BASE =
            "SELECT id_entrega, id_practica, id_alumno, fecha_entrega, nota FROM entregas";
    // COUNT(*) cuenta cuántas filas cumplen la condición (0 = ninguna).
    private static final String EXISTE =
            "SELECT COUNT(*) FROM entregas WHERE id_practica = ? AND id_alumno = ?";

    // ---------- INSERTAR una entrega ----------
    @Override
    public void insertar(Entrega e) throws SQLException {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT)) {
            ps.setInt(1, e.getIdPractica());
            ps.setInt(2, e.getIdAlumno());
            ps.setDate(3, new java.sql.Date(e.getFechaEntrega().getTime())); // fecha Java -> fecha SQL
            ps.setDouble(4, e.getNota());
            ps.executeUpdate();
        }
    }

    // ---------- BUSCAR una entrega por su id ----------
    @Override
    public Entrega buscarPorId(Integer id) throws SQLException {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BASE + " WHERE id_entrega = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null; // hay fila -> ficha; no hay -> null
            }
        }
    }

    // ---------- LISTAR todas las entregas ----------
    @Override
    public List<Entrega> listarTodos() throws SQLException {
        List<Entrega> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BASE);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // ---------- ¿YA EXISTE la entrega de este alumno a esta práctica? ----------
    // Método propio (no viene de DaoGenerico). Devuelve true si ya la entregó.
    public boolean existeEntrega(int idPractica, int idAlumno) throws SQLException {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(EXISTE)) {
            ps.setInt(1, idPractica);
            ps.setInt(2, idAlumno);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();                    // hay exactamente una fila: el contador
                return rs.getInt(1) > 0;      // si el contador es mayor que 0, ya existe
            }
        }
    }

    // ---------- MAPEAR: fila de la base de datos -> ficha Java ----------
    private Entrega mapear(ResultSet rs) throws SQLException {
        return new Entrega(
                rs.getInt("id_entrega"),
                rs.getInt("id_practica"),
                rs.getInt("id_alumno"),
                rs.getDate("fecha_entrega"),
                rs.getDouble("nota"));
    }
}
