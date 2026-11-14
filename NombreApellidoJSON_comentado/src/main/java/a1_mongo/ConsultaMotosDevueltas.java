package a1_mongo;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

// ============================================================
// EJERCICIO A1: motos devueltas en una ciudad (MongoDB).
//
// IDEA GENERAL:
// En MongoDB los datos son "fichas" (documentos) dentro de "cajones" (colecciones).
// Aquí tenemos dos cajones: Clientes (con sus alquileres) y Motos (con sus datos).
// Queremos la matrícula y el tipo de cada moto que se devolvió en una ciudad.
//
// ANALOGÍA DEL PIPELINE: es una cinta transportadora con paso 1, paso 2, paso 3...
// Cada etapa recibe lo que dejó la anterior y lo transforma.
//
// ESTRUCTURA ASUMIDA (ajústala si tus JSON de Aules son distintos):
//   Motos:    { "matricula": "1234ABC", "tipo": "scooter" }
//   Clientes: { "nombre": "...", "alquileres": [ { "matricula": "1234ABC", "ciudad_devolucion": "Valencia" } ] }
// ============================================================
public class ConsultaMotosDevueltas {

    public static void main(String[] args) {

        // Preguntamos al usuario la ciudad por teclado.
        Scanner sc = new Scanner(System.in);
        System.out.print("Introduce la ciudad donde se han devuelto las motos: ");
        String ciudad = sc.nextLine().trim();   // leer la línea y quitar espacios sobrantes

        // Abrimos la conexión con MongoDB. Se cierra sola al final del bloque (try-with-resources).
        try (MongoClient cliente = MongoClients.create("mongodb://localhost:27017")) {

            // Elegimos la base de datos "Alquiler" y el cajón "Clientes".
            MongoDatabase bd = cliente.getDatabase("Alquiler");
            MongoCollection<Document> clientes = bd.getCollection("Clientes");

            // ---------- PIPELINE: la cinta transportadora ----------
            // Arrays.asList(...) mete todas las etapas en una lista, en orden.
            List<Bson> pipeline = Arrays.asList(

                // ETAPA 1: "desenrollar" los alquileres.
                // Si un cliente tiene 3 alquileres, esta etapa crea 3 fichas, una por alquiler.
                Aggregates.unwind("$alquileres"),

                // ETAPA 2: quedarnos solo con los alquileres devueltos en la ciudad que escribió el usuario.
                // "alquileres.ciudad_devolucion" = el campo dentro del alquiler.
                Aggregates.match(Filters.eq("alquileres.ciudad_devolucion", ciudad)),

                // ETAPA 3: "pegar" datos de otro cajón (lookup = buscar y unir).
                // Para cada alquiler, busca en Motos la moto cuya "matricula" coincida,
                // y la guarda en un campo nuevo llamado "moto".
                Aggregates.lookup("Motos", "alquileres.matricula", "matricula", "moto"),

                // ETAPA 4: "moto" llega como una lista de un elemento; la desenrollamos para tenerla directa.
                Aggregates.unwind("$moto"),

                // ETAPA 5: elegir qué campos mostrar (proyección). No queremos todo.
                // excludeId() quita el _id; computed() crea los campos "matricula" y "tipo" que queremos.
                Aggregates.project(Projections.fields(
                        Projections.excludeId(),
                        Projections.computed("matricula", "$moto.matricula"),
                        Projections.computed("tipo", "$moto.tipo")))
            );

            // Ejecutamos la cinta y guardamos el resultado en una lista de fichas.
            List<Document> resultado = new ArrayList<Document>();
            clientes.aggregate(pipeline).into(resultado);

            // Mostramos cada ficha en formato JSON (texto con llaves y comillas).
            for (Document doc : resultado) {
                System.out.println(doc.toJson());
            }
            // El total = cuántas fichas salieron = cuántas motos se devolvieron allí.
            System.out.println("Motos devueltas en " + ciudad + ": " + resultado.size());

        } catch (MongoException e) {
            // Si MongoDB no responde o hay un problema, avisamos sin que el programa "explote".
            System.out.println("Error de MongoDB: " + e.getMessage());
        } finally {
            // Pase lo que pase, cerramos el "oído" del teclado.
            sc.close();
        }
    }
}
