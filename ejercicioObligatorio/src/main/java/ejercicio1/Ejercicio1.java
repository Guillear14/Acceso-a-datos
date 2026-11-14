package ejercicio1;

import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;

// EJERCICIO 1
// Obtener las personas de la ciudad London, guardarlas en un ArrayList<Persona>
// y mostrarlas por consola.
public class Ejercicio1 {

    public static void main(String[] args) {

        // Lista donde guardaremos las personas que encontremos en la base de datos.
        ArrayList<Persona> personas = new ArrayList<>();

        // Gson convierte el JSON de MongoDB en objetos Persona.
        Gson gson = new Gson();

        // Conexión a MongoDB. Se cierra sola al terminar el bloque.
        try (MongoClient cliente = MongoClients.create("mongodb://localhost:27017")) {

            // Base de datos "Concesionari" y colección "Personas".
            MongoDatabase bd = cliente.getDatabase("Concesionari");
            MongoCollection<Document> coleccion = bd.getCollection("Personas");

            // Buscar solo las personas cuya ciudad sea "London".
            for (Document doc : coleccion.find(Filters.eq("City", "London"))) {
                // doc.toJson() pasa la ficha a texto JSON.
                // gson.fromJson() convierte ese texto en un objeto Persona.
                // Los campos que Persona no tiene (como _id) se ignoran.
                Persona persona = gson.fromJson(doc.toJson(), Persona.class);
                personas.add(persona);
            }
        }

        // Mostrar cada persona de la lista.
        for (Persona persona : personas) {
            System.out.println(persona);
        }
    }
}
