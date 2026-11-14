package a3_gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import pojos.Jugador;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

// ============================================================
// EJERCICIO A3: leer un JSON de un fichero y convertirlo en un objeto Java (Jugador).
//
// Palabras clave:
//   DESERIALIZAR = texto JSON  -> objeto Java   (lo que hacemos al leer)
//   SERIALIZAR   = objeto Java -> texto JSON    (lo que hacemos al volver a escribirlo)
// ============================================================
public class SerializarJugador {

    // Ruta del fichero con los datos del jugador.
    private static final String ARCHIVO = "ficheros/Jugador.JSON";

    public static void main(String[] args) {
        Gson gson = new Gson(); // el traductor JSON <-> Java

        // Abrimos el fichero para leerlo. Se cierra solo al terminar.
        try (Reader lector = Files.newBufferedReader(Paths.get(ARCHIVO), StandardCharsets.UTF_8)) {

            // DESERIALIZAR: Gson mira las claves del JSON ("nombre", "género", "hobby")
            // y rellena los campos del objeto Jugador con el mismo nombre.
            Jugador jugador = gson.fromJson(lector, Jugador.class);
            System.out.println("Objeto creado: " + jugador);  // imprime con el toString() de Jugador

            // SERIALIZAR: volvemos a convertir el objeto en texto JSON, con sangría para verlo bien.
            String json = new GsonBuilder().setPrettyPrinting().create().toJson(jugador);
            System.out.println(json);

        } catch (IOException | JsonParseException e) {
            // Fichero no encontrado o JSON mal escrito.
            System.out.println("Error al procesar Jugador.JSON: " + e.getMessage());
        }
    }
}
