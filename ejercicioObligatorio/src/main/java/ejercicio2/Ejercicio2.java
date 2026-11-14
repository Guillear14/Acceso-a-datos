package ejercicio2;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Ejercicio 2: leer el fichero ficheros/Platos.json e insertar cada plato
 * como un Document en la colección "platos" de la base de datos "Cocina".
 */
public class Ejercicio2 {

    public static void main(String[] args) {
        try {
            // 1. Leemos todo el contenido del fichero JSON en un String.
            String contenido = new String(
                    Files.readAllBytes(Paths.get("ficheros/Platos.json")),
                    StandardCharsets.UTF_8);

            // 2. Parseamos el texto con GSON.
            // En Gson 2.8.0 se usa una instancia de JsonParser (el método estático aún no existe).
            // El resultado es un array JSON, porque el fichero empieza por "[".
            JsonArray platosJson = new JsonParser().parse(contenido).getAsJsonArray();

            // 3. Convertimos cada plato del array en un Document de MongoDB.
            List<Document> documentos = new ArrayList<>();
            for (JsonElement plato : platosJson) {
                // plato.toString() devuelve el objeto como texto JSON
                // y Document.parse() lo convierte en un Document de MongoDB.
                documentos.add(Document.parse(plato.toString()));
            }

            // 4. Insertamos los documentos en MongoDB.
            try (MongoClient cliente = MongoClients.create("mongodb://localhost:27017")) {

                // Base de datos "Cocina" y colección "platos".
                MongoDatabase bd = cliente.getDatabase("Cocina");
                MongoCollection<Document> platos = bd.getCollection("platos");

                // Borramos la colección antes de insertar para no duplicar platos
                // si ejecutamos el programa varias veces.
                platos.drop();

                // Insertamos todos los documentos de una vez.
                platos.insertMany(documentos);

                System.out.println("Insertados " + documentos.size() + " platos en Cocina.platos");
            }
        } catch (Exception e) {
            // Si ocurre cualquier error (fichero no encontrado, JSON mal formado, MongoDB apagado...),
            // se muestra la traza para poder localizarlo.
            e.printStackTrace();
        }
    }
}