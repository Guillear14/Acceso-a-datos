package ej06_jdbc_mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.Decimal128;
import utilies.ConexionBD;
import utilies.ConexionMongo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * EJEMPLO 6: TABLA de MySQL -> MongoDB.
 *
 * ANALOGÍA:
 * En MySQL los datos están en TABLAS con FILAS y COLUMNAS, como una hoja de Excel.
 * En MongoDB están en FICHAS (documentos). Este programa copia cada FILA de la tabla
 * en una FICHA, donde el nombre de la columna es la clave y el valor de la celda es el valor.
 *
 *   Fila:   dorsal=1 | nombre=Pedro | edad=30
 *   Ficha:  { "dorsal": 1, "nombre": "Pedro", "edad": 30 }
 */
public class JdbcAMongo {

    public static void main(String[] args) {

        // Si nos das un nombre de tabla por consola lo usamos; si no, usamos "ciclista".
        String tabla = args.length > 0 ? args[0] : "ciclista";

        // SEGURIDAD: el nombre de la tabla lo vamos a pegar dentro del SQL.
        // Solo permitimos letras, números y guion bajo. Si alguien escribe "ciclista; DROP TABLE x"
        // el programa lo rechaza. A esto se le llama evitar "inyección SQL".
        if (!tabla.matches("[A-Za-z_][A-Za-z0-9_]*")) {
            System.out.println("Nombre de tabla no válido: " + tabla);
            return; // salimos del programa sin hacer nada más
        }

        // La consulta: "dame todas las filas de la tabla".
        String sql = "SELECT * FROM " + tabla;

        // Abrimos TRES cosas (se cierran solas al final, en orden inverso):
        //  - la conexión con MySQL (sacamos un coche del parking)
        //  - la sentencia SQL preparada
        //  - el resultado (las filas)
        //  - y el cliente de MongoDB
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery();
             MongoClient cliente = ConexionMongo.crearCliente()) {

            // "Metadatos": información SOBRE la tabla, por ejemplo cómo se llaman sus columnas.
            // Así no necesitamos saber de antemano qué columnas tiene.
            ResultSetMetaData meta = rs.getMetaData();

            // El cajón de MongoDB donde guardaremos las copias. Su nombre será "ciclista_desde_jdbc".
            MongoCollection<Document> col = cliente.getDatabase("combinados").getCollection(tabla + "_desde_jdbc");

            // Lista donde iremos guardando las fichas antes de meterlas todas juntas.
            List<Document> lote = new ArrayList<Document>();

            // rs.next() avanza a la SIGUIENTE fila. Devuelve false cuando ya no hay más filas.
            while (rs.next()) {
                Document doc = new Document();  // ficha vacía para esta fila

                // Recorremos las columnas de la fila: 1, 2, 3... (JDBC empieza a contar en 1, no en 0).
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    String nombreColumna = meta.getColumnLabel(i);   // ejemplo: "nombre"
                    Object valor = rs.getObject(i);                  // ejemplo: "Pedro"
                    // Añadimos a la ficha: clave = nombre de columna, valor = valor convertido.
                    doc.append(nombreColumna, convertir(valor));
                }
                lote.add(doc); // guardamos la ficha en la lista
            }

            // Si hay fichas, las metemos todas en MongoDB de una sola vez (más rápido).
            if (!lote.isEmpty()) {
                col.insertMany(lote);
            }
            System.out.println("Filas copiadas de '" + tabla + "' a MongoDB: " + lote.size());

        } catch (SQLException e) {
            // Error de MySQL: tabla que no existe, contraseña mal, etc.
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    /*
     * Traductor de tipos: MySQL/JDBC -> MongoDB/BSON.
     *
     * ¿Por qué hace falta? Porque el "traductor BSON" no conoce todos los tipos de Java.
     * Por ejemplo, las fechas de SQL (java.sql.Date) "parecen" fechas, pero el traductor
     * no las reconoce, así que las pasamos a java.util.Date, que sí sabe traducir.
     */
    static Object convertir(Object valor) {
        if (valor == null) return null;                                    // nada que convertir: NULL se queda NULL

        // Fechas y fecha+hora de SQL -> fecha normal de Java.
        if (valor instanceof java.sql.Timestamp) return new java.util.Date(((java.sql.Timestamp) valor).getTime());
        if (valor instanceof java.sql.Date) return new java.util.Date(((java.sql.Date) valor).getTime());

        // Números con decimales exactos (DECIMAL de MySQL) -> Decimal128, el tipo de MongoDB para dinero/precisión.
        if (valor instanceof BigDecimal) return new Decimal128((BigDecimal) valor);

        // Estos tipos ya los entiende MongoDB tal cual.
        if (valor instanceof Integer || valor instanceof Long || valor instanceof Double
                || valor instanceof String || valor instanceof Boolean) return valor;

        // Cualquier otra cosa rara: la guardamos como texto para no perder el dato.
        return valor.toString();
    }
}
