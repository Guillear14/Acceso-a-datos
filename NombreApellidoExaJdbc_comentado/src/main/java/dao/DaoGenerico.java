package dao;

import java.sql.SQLException;
import java.util.List;

// ============================================================
// DAO GENÉRICO = una "plantilla" de operaciones básicas sobre una tabla.
//
// DAO = Data Access Object: la clase que SOLO habla con la base de datos.
// Así el resto del programa no escribe SQL; pide cosas a los DAO.
//
// "<T, K>" son huecos que se rellenan al usar la plantilla:
//   T = el tipo de ficha (Practica, Entrega...)
//   K = el tipo de la clave (Integer, normalmente el id)
// ============================================================
public interface DaoGenerico<T, K> {

    // Guardar una ficha nueva en la tabla.
    void insertar(T t) throws SQLException;

    // Buscar una ficha por su id. Devuelve null si no existe.
    T buscarPorId(K id) throws SQLException;

    // Devolver todas las fichas de la tabla.
    List<T> listarTodos() throws SQLException;
}
