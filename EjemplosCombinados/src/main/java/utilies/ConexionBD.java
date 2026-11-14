package utilies;

import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/*
 * QUÉ ES ESTO:
 * Una clase que da CONEXIONES a MySQL, igual que la UD02 (apartado 6).
 *
 * ANALOGÍA DEL POOL (piscina de conexiones):
 * Abrir una conexión a la base de datos es lento, como arrancar un coche cada vez que
 * quieres ir a la tienda. Un "DataSource" (con pool) mantiene varias conexiones ya
 * abiertas, como un parking con coches aparcados. Tú pides uno, lo usas y lo devuelves.
 *
 * DATOS DE LA CONEXIÓN: se leen de un fichero de propiedades (configuracion.properties),
 * así no hay que tocar el código si cambia la contraseña.
 */
public final class ConexionBD {

    // Aquí guardamos el "parking" de conexiones. Empieza vacío (null).
    private static DataSource dataSource;

    // Constructor privado: no se crean objetos de esta clase, solo se usa su método estático.
    private ConexionBD() { }

    // Pide una conexión al parking. "synchronized" = un solo hilo a la vez crea el parking.
    public static synchronized Connection getConnection() throws SQLException {
        if (dataSource == null) {           // si el parking aún no existe...
            dataSource = crearDataSource(); // ...lo creamos (solo la primera vez)
        }
        return dataSource.getConnection();  // sacamos un coche (conexión) del parking
    }

    // Lee el fichero de propiedades y crea el DataSource.
    private static DataSource crearDataSource() throws SQLException {
        // Buscamos el fichero configuracion.properties dentro del proyecto (carpeta resources).
        InputStream in = ConexionBD.class.getClassLoader().getResourceAsStream("configuracion.properties");
        if (in == null) {
            // Si no lo encuentra, avisamos con un mensaje claro.
            throw new SQLException("No se encuentra configuracion.properties");
        }
        // try-with-resources: el "in" se cierra solo al terminar, aunque haya error.
        try (InputStream flujo = in) {
            Properties props = new Properties(); // un diccionario clave=valor
            props.load(flujo);                   // rellenamos el diccionario con el fichero
            return BasicDataSourceFactory.createDataSource(props); // creamos el parking
        } catch (Exception e) {
            // Si algo falla, lo envolvemos en un SQLException para que quien llame lo entienda.
            throw new SQLException("Error al crear el DataSource: " + e.getMessage(), e);
        }
    }
}
