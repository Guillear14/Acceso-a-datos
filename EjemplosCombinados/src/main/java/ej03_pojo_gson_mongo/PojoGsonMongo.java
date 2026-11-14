package ej03_pojo_gson_mongo;

import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import pojos.Jugador;
import utilies.ConexionMongo;

import java.util.Arrays;

/*
 * EJEMPLO 3: el camino "a mano" usando GSON.
 *
 * Hay DOS sentidos:
 *   IDA:    Jugador (Java)  -> texto JSON -> Document (BSON) -> MongoDB
 *   VUELTA: MongoDB -> Document -> texto JSON -> Jugador (Java)
 *
 * ANALOGÍA: es como traducir una carta en tres pasos con diccionarios intermedios.
 * Tú decides cada paso, así que es muy fácil de seguir.
 */
public class PojoGsonMongo {

    public static void main(String[] args) {

        // "Gson" es el traductor Java <-> JSON de Google.
        Gson gson = new Gson();

        // Creamos un jugador en memoria (todavía no está en la base de datos).
        Jugador tom = new Jugador("Tom", "masculino", Arrays.asList("fútbol", "lectura", "natación"));

        try (MongoClient cliente = ConexionMongo.crearCliente()) {

            // Cajón "jugadores_gson". Usamos Document porque vamos a guardar texto JSON convertido.
            MongoCollection<Document> col = cliente.getDatabase("combinados").getCollection("jugadores_gson");

            // ===== IDA =====
            String json = gson.toJson(tom);   // PASO A: Jugador -> texto JSON
            Document doc = Document.parse(json); // PASO B: texto JSON -> Document (formato de MongoDB)
            col.insertOne(doc);                // PASO C: guardar en MongoDB
            System.out.println("Insertado (JSON): " + json);

            // ===== VUELTA =====
            // Buscamos la ficha cuyo nombre sea Tom.
            Document leido = col.find(Filters.eq("nombre", "Tom")).first();
            String jsonLeido = leido.toJson();  // PASO D: Document -> texto JSON (ahora trae también el _id)

            // PASO E: texto JSON -> Jugador. Gson ignora el campo _id porque Jugador no lo tiene.
            Jugador desdeMongo = gson.fromJson(jsonLeido, Jugador.class);
            System.out.println("POJO recuperado: " + desdeMongo);
        }
    }
}
