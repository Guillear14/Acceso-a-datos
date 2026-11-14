package dao;

import pojos.Practica;
import utilies.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// ============================================================
// DAO de PRÁCTICAS: todas las operaciones sobre la tabla practicas.
// Implementa DaoGenerico con <Practica, Integer>: fichas Practica, claves Integer.
// ============================================================
public class PracticaDao implements DaoGenerico<Practica, Integer> {

    // SQL guardado en constantes. "?" son huecos que rellenaremos después (más seguro que pegar texto).
    private static final String INSERT =
            "INSERT INTO practicas (id_asignatura, nombre, descripcion, fecha_limite) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BASE =
            "SELECT id_practica, id_asignatura, nombre, descripcion, fecha_limite FROM practicas";

    // ---------- INSERTAR una práctica nueva ----------
    @Override
    public void insertar(Practica p) throws SQLException {
        // try-with-resources: la conexión y la sentencia se cierran solas al final.
        try (Connection con = ConexionBD.getConnection();           // 1. pedir conexión
             PreparedStatement ps = con.prepareStatement(INSERT)) { // 2. preparar la orden SQL
            ps.setInt(1, p.getIdAsignatura());     // hueco 1 <- id de la asignatura
            ps.setString(2, p.getNombre());        // hueco 2 <- nombre
            ps.setString(3, p.getDescripcion());   // hueco 3 <- descripción
            // La fecha de Java (java.util.Date) hay que pasarla a fecha de SQL (java.sql.Date).
            ps.setDate(4, new java.sql.Date(p.getFechaLimite().getTime()));
            ps.executeUpdate();                    // 3. ejecutar la orden (INSERT)
        }
    }

    // ---------- BUSCAR una práctica por su id ----------
    @Override
    public Practica buscarPorId(Integer id) throws SQLException {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BASE + " WHERE id_practica = ?")) {
            ps.setInt(1, id);                       // hueco <- el id que buscamos
            try (ResultSet rs = ps.executeQuery()) { // ejecutar y recibir la "tabla de respuesta"
                // rs.next() mueve el cursor a la primera fila. Si hay fila, la convertimos en Practica.
                // Si no hay fila, devolvemos null (= "no existe").
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    // ---------- LISTAR todas las prácticas ----------
    @Override
    public List<Practica> listarTodos() throws SQLException {
        List<Practica> lista = new ArrayList<>();   // lista vacía donde iremos metiendo fichas
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BASE);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {                     // mientras haya filas...
                lista.add(mapear(rs));              // ...convertir cada fila en Practica y guardarla
            }
        }
        return lista;
    }

    // ---------- MAPEAR: fila de la base de datos -> ficha Java ----------
    // Lee cada columna por su nombre y construye un objeto Practica.
    private Practica mapear(ResultSet rs) throws SQLException {
        return new Practica(
                rs.getInt("id_practica"),
                rs.getInt("id_asignatura"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getDate("fecha_limite"));
    }
}
