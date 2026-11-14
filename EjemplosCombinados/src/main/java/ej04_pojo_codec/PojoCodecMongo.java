package ej04_pojo_codec;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import pojos.Jugador;
import utilies.ConexionMongo;

import java.util.Arrays;

/*
 * EJEMPLO 4: el camino "automático" con PojoCodec.
 *
 * Aquí NO hay JSON en medio. El traductor de ConexionMongo sabe pasar un Jugador
 * directamente a BSON y viceversa. Es como un ascensor: entras por un lado y sales por el otro.
 *
 * Además, la MISMA colección se puede mirar de dos formas:
 *   - como Jugador  (lo entiende tu programa Java)
 *   - como Document (para ver cómo lo guarda MongoDB por dentro)
 */
public class PojoCodecMongo {

    public static void main(String[] args) {

        try (MongoClient cliente = ConexionMongo.crearCliente()) {

            // getCollection("...", Jugador.class) = "este cajón guarda JUGADORES".
            MongoCollection<Jugador> jugadores =
                    cliente.getDatabase("combinados").getCollection("jugadores_pojo", Jugador.class);

            // Guardar: le pasamos el objeto Jugador tal cual. No hay que convertir nada.
            jugadores.insertOne(new Jugador("Lucía", "femenino", Arrays.asList("ciclismo", "cine")));

            // Leer: nos devuelve un Jugador ya construido, listo para usar con sus getters.
            Jugador lucia = jugadores.find(Filters.eq("nombre", "Lucía")).first();
            System.out.println("Como POJO:     " + lucia);

            // withDocumentClass: "mira el MISMO cajón, pero dame fichas de tipo Document".
            // Sirve para ver el formato real que se guardó en MongoDB.
            MongoCollection<Document> comoDocumento = jugadores.withDocumentClass(Document.class);
            Document doc = comoDocumento.find(Filters.eq("nombre", "Lucía")).first();
            System.out.println("Como Document: " + doc.toJson());

            // Comprobamos que el campo se guardó con la tilde: "género" (no "genero").
            System.out.println("Campo género en BSON: " + doc.get("género"));
        }
    }
}
