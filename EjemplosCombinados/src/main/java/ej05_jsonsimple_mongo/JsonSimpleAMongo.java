package ej05_jsonsimple_mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import utilies.ConexionMongo;

import java.io.FileReader;
import java.io.IOException;

/*
 * EJEMPLO 5: FICHERO JSON -> json-simple -> MongoDB.
 *
 * CAMINO:
 *   fichero.JSON  --(lector)-->  JSONObject  --(toJSONString)-->  texto  --(parse)-->  Document  --> MongoDB
 *
 * json-simple es otra librería de JSON. Su JSONObject se parece a un mapa (clave -> valor).
 * Como MongoDB no entiende JSONObject, lo convertimos a texto y luego a Document.
 */
public class JsonSimpleAMongo {

    public static void main(String[] args) {

        // El "lector de JSON" de json-simple.
        JSONParser parser = new JSONParser();

        // Dos recursos que se cierran solos al terminar:
        //  - FileReader: abre el fichero del disco.
        //  - MongoClient: abre la conexión con MongoDB.
        try (FileReader lector = new FileReader("ficheros/Jugador.JSON");
             MongoClient cliente = ConexionMongo.crearCliente()) {

            // PASO 1: leer el fichero y convertirlo en un JSONObject.
            // El (JSONObject) delante es un "cast": le decimos a Java qué tipo esperamos.
            JSONObject jugador = (JSONObject) parser.parse(lector);

            // PASO 2: JSONObject -> texto JSON (un String).
            String texto = jugador.toJSONString();

            // PASO 3: texto JSON -> Document (formato que entiende MongoDB).
            Document doc = Document.parse(texto);

            // PASO 4: guardar en el cajón "jugadores_fichero".
            MongoCollection<Document> col = cliente.getDatabase("combinados").getCollection("jugadores_fichero");
            col.insertOne(doc);
            System.out.println("Insertado desde fichero: " + doc.toJson());

        } catch (IOException e) {
            // Si el fichero no existe o no se puede leer.
            System.out.println("Error de lectura: " + e.getMessage());
        } catch (org.json.simple.parser.ParseException e) {
            // Si el texto del fichero no es JSON válido (falta una coma, una llave...).
            System.out.println("JSON no válido: " + e.getMessage());
        }
    }
}
