package ej02_json_mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import utilies.ConexionMongo;

import java.util.ArrayList;
import java.util.List;

/*
 * EJEMPLO 2: JSON -> Document -> MongoDB -> Document -> JSON
 *
 * ANALOGÍA:
 * MongoDB es un ARMARIO con cajones (colecciones). Cada cajón guarda FICHAS (documentos).
 * Aquí cogemos varias fichas escritas en JSON, las metemos en el cajón "jugadores_json"
 * y luego las sacamos para mirarlas.
 *
 * REQUISITO: MongoDB tiene que estar encendido (servicio "MongoDB" en Windows).
 */
public class JsonAMongo {

    // Esta línea solo calla un aviso del compilador sobre un "cast" que sabemos que es correcto.
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {

        // Dos jugadores escritos en JSON. Es un ARRAY: empieza por [ y termina por ].
        String jsonArray = "[{\"nombre\":\"Tom\",\"género\":\"masculino\",\"hobby\":[\"fútbol\",\"lectura\"]},"
                + " {\"nombre\":\"Ana\",\"género\":\"femenino\",\"hobby\":[\"natación\"]}]";

        // PROBLEMA: Document.parse solo sabe leer UN objecto { ... }, no un array [ ... ].
        // TRUCO: lo envolvemos en un objeto con una clave "items": {"items": [ ... ]}
        // Luego sacamos la lista con get("items").
        List<Document> documentos = (List<Document>) Document.parse("{\"items\":" + jsonArray + "}").get("items");

        // try-with-resources: el cliente se cierra solo al final (como apagar la luz al salir).
        try (MongoClient cliente = ConexionMongo.crearCliente()) {

            // Elegimos la base de datos "combinados" (se crea sola si no existe).
            MongoDatabase bd = cliente.getDatabase("combinados");

            // Elegimos el cajón (colección) "jugadores_json". Solo guardamos Documents.
            MongoCollection<Document> col = bd.getCollection("jugadores_json");

            // Metemos TODAS las fichas de golpe.
            // Mongo les pone automáticamente un identificador único llamado _id.
            col.insertMany(documentos);
            System.out.println("Insertados: " + documentos.size());

            // BUSCAR: "dame la primera ficha cuyo nombre sea Tom".
            Document tom = col.find(Filters.eq("nombre", "Tom")).first();
            System.out.println("Tom en JSON: " + tom.toJson());

            // BUSCAR TODO: "dame todas las fichas" y guárdalas en una lista normal.
            List<Document> todos = col.find().into(new ArrayList<Document>());
            for (Document d : todos) {          // recorremos la lista ficha a ficha
                System.out.println(d.toJson()); // y mostramos cada una en JSON
            }
        }
    }
}
