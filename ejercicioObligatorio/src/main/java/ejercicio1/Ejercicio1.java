package ejercicio1;

import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;

/**
 * Ejercicio 1: obtener de MongoDB las personas cuya ciudad es "London",
 * guardarlas en un ArrayList de objetos Persona y mostrarlas por consola.
 */
public class Ejercicio1 {

    public static void main(String[] args) {
        // Lista donde guardaremos los objetos Persona obtenidos de la BD.
        ArrayList<Persona> personas = new ArrayList<>();

        // Gson sirve para convertir el JSON de MongoDB en objetos Java.
        Gson gson = new Gson();

        // try-with-resources: el cliente de MongoDB se cierra automáticamente al terminar.
        try (MongoClient cliente = MongoClients.create("mongodb://localhost:27017")) {

            // Seleccionamos la base de datos "concesionario" y la colección "Personas".
            MongoDatabase bd = cliente.getDatabase("concesionario");
            MongoCollection<Document> coleccion = bd.getCollection("Personas");

            // Filtramos en la propia consulta para traer solo los documentos con City = "London".
            for (Document doc : coleccion.find(Filters.eq("City", "London"))) {

                // 1. doc.toJson() convierte el Document de MongoDB en un String JSON.
                // 2. gson.fromJson() convierte ese JSON en un objeto Persona.
                // Los campos que no existen en Persona (como _id) se ignoran.
                Persona persona = gson.fromJson(doc.toJson(), Persona.class);

                personas.add(persona);
            }
        }

        // Recorremos el ArrayList y mostramos cada persona por consola.
        for (Persona persona : personas) {
            System.out.println(persona);
        }
    }
}