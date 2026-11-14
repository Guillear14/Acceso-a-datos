package utilies;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

/*
 * QUÉ ES ESTO:
 * Una clase "de ayuda" que crea la conexión con MongoDB.
 *
 * PIENSA EN UN TRADUCTOR:
 * MongoDB habla en "BSON" (su idioma interno). Nuestro programa habla en Java.
 * Un "codec" es el traductor que convierte un idioma en el otro:
 *   - Document (BSON genérico)  -> ya sabe traducirlo el registro por defecto.
 *   - Jugador  (nuestro POJO)   -> le enseñamos el traductor con PojoCodecProvider.
 */
public final class ConexionMongo {

    // Dirección del servidor MongoDB que tienes instalado en tu PC.
    // "localhost" = este mismo ordenador. 27017 = el puerto por defecto de MongoDB.
    public static final String URL = "mongodb://localhost:27017";

    // Lista de traductores que usará el cliente:
    //  1) los que ya trae MongoDB de serie,
    //  2) y los que se crean automáticamente para nuestras clases POJO.
    private static final CodecRegistry REGISTRO = CodecRegistries.fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    // Constructor privado: nadie puede hacer "new ConexionMongo()". Solo usamos el método de abajo.
    private ConexionMongo() { }

    // Devuelve un cliente listo para usar: es como abrir el teléfono para llamar al servidor.
    public static MongoClient crearCliente() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(URL)) // dónde está el servidor
                .codecRegistry(REGISTRO)                          // qué traductores usar
                .build();                                         // construir la configuración
        return MongoClients.create(settings);                     // crear el cliente con esa configuración
    }
}
