package utilies;

import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

// ============================================================
// CONEXIÓN A LA BASE DE DATOS (MySQL).
//
// ANALOGÍA: la base de datos es una biblioteca. Para entrar necesitas una "tarjeta" (conexión).
// Abrir una conexión es lento, así que no se abre una cada vez: se usa un "parking"
// (DataSource o pool) donde hay varias conexiones ya abiertas listas para usar.
//
// Todas las clases DAO piden aquí una conexión con ConexionBD.getConnection().
// ============================================================
public final class ConexionBD {

    // El parking de conexiones. Empieza vacío (null) y se crea la primera vez que hace falta.
    private static DataSource dataSource;

    // Constructor privado: nadie puede hacer "new ConexionBD()". Solo usamos el método estático.
    private ConexionBD() { }

    // Pide una conexión libre al parking. "synchronized" evita que dos hilos la creen a la vez.
    public static synchronized Connection getConnection() throws SQLException {
        if (dataSource == null) {          // si el parking aún no existe...
            dataSource = crearDataSource(); // ...lo creamos (solo la primera vez)
        }
        return dataSource.getConnection(); // sacamos una conexión del parking
    }

    // Crea el parking leyendo los datos de conexión del fichero configuracion.properties.
    private static DataSource crearDataSource() throws SQLException {
        // Busca el fichero dentro del proyecto (carpeta src/main/resources).
        InputStream in = ConexionBD.class.getClassLoader().getResourceAsStream("configuracion.properties");
        if (in == null) {
            // Si no lo encuentra, paramos y avisamos.
            throw new SQLException("No se encuentra el fichero configuracion.properties");
        }
        // try-with-resources: el fichero se cierra solo al terminar, pase lo que pase.
        try (InputStream flujo = in) {
            Properties props = new Properties(); // diccionario clave=valor (ej: url=jdbc:mysql://...)
            props.load(flujo);                   // rellenamos el diccionario con el fichero
            return BasicDataSourceFactory.createDataSource(props); // creamos el parking con esos datos
        } catch (Exception e) {
            // Si algo falla, lo envolvemos en un SQLException para que quien llame lo entienda.
            throw new SQLException("Error al crear el DataSource: " + e.getMessage(), e);
        }
    }
}
