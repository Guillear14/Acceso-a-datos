package a2_json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import pojos.Evento;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// ============================================================
// EJERCICIO A2: filtrar eventos de un fichero JSON y guardar el resultado en otro JSON.
//
// PASOS:
//   1. LEER el fichero Eventos.json  -> lo convertimos en objetos Evento (con Gson).
//   2. FILTRAR: solo los de tipo "música" y con aforo mayor que 400.
//   3. MOSTRAR nombre y fecha por pantalla.
//   4. GUARDAR esos nombres y fechas en un nuevo fichero JSON.
// ============================================================
public class EventosMusica {

    // Rutas de los ficheros (relativas a la carpeta del proyecto).
    private static final String ENTRADA = "ficheros/Eventos.json";
    private static final String SALIDA = "ficheros/EventosMusicaAforoMayor400.json";

    // Criterios de filtro. Si cambia el enunciado, solo cambias estas dos líneas.
    private static final String TIPO_BUSCADO = "música";
    private static final int AFORO_MINIMO = 400; // usamos "mayor que" (>) 400

    // Clase interna sencilla: solo lo que queremos guardar en el fichero final (nombre y fecha).
    static class Resumen {
        String nombre;
        String fecha;

        Resumen(String nombre, String fecha) {
            this.nombre = nombre;
            this.fecha = fecha;
        }
    }

    public static void main(String[] args) {

        // Gson = el traductor JSON <-> Java de Google.
        Gson gson = new Gson();

        // try-with-resources: el lector del fichero se cierra solo al terminar.
        try (Reader lector = Files.newBufferedReader(Paths.get(ENTRADA), StandardCharsets.UTF_8)) {

            // PASO 1: leer. Gson convierte el array JSON en un array de Evento.
            // Evento[].class = "dame un array de Evento".
            Evento[] eventos = gson.fromJson(lector, Evento[].class);

            // PASO 2 y 4 (preparar): filtrar y transformar. Es un "stream" = una cadena de pasos.
            //   filter -> solo los que cumplen la condición (tipo música Y aforo > 400)
            //   map    -> convertir cada Evento en un Resumen (solo nombre y fecha)
            //   collect-> juntar todo en una lista
            List<Resumen> resultado = Arrays.stream(eventos)
                    .filter(e -> TIPO_BUSCADO.equalsIgnoreCase(e.getTipo()) && e.getAforo() > AFORO_MINIMO)
                    .map(e -> new Resumen(e.getNombre(), e.getFecha()))
                    .collect(Collectors.toList());

            // PASO 3: mostrar en pantalla.
            System.out.println("Eventos de tipo " + TIPO_BUSCADO + " con aforo mayor de " + AFORO_MINIMO + ":");
            for (Resumen r : resultado) {
                System.out.println(r.nombre + " - " + r.fecha);
            }

            // PASO 4: guardar en un fichero nuevo. setPrettyPrinting = que quede con sangría y legible.
            try (Writer escritor = Files.newBufferedWriter(Paths.get(SALIDA), StandardCharsets.UTF_8)) {
                new GsonBuilder().setPrettyPrinting().create().toJson(resultado, escritor);
            }
            System.out.println("Fichero creado: " + SALIDA);

        } catch (IOException | JsonParseException e) {
            // IOException = problema con los ficheros (no existe, sin permisos...).
            // JsonParseException = el texto no es JSON válido.
            System.out.println("Error al procesar el JSON: " + e.getMessage());
        }
    }
}
