// ============================================================
// IMPORTS: "traemos" las herramientas que necesitamos.
// Java no las conoce por defecto, así que hay que avisarle de dónde están.
// ============================================================
import java.util.ArrayList;   // lista que crece sola (para guardar cosas en fila)
import java.util.List;        // tipo genérico "lista" (puede ser ArrayList, etc.)

import org.bson.Document;     // un "documento" de MongoDB = una ficha con campos (como un JSON)

import com.mongodb.client.MongoClient;      // la conexión con el servidor de MongoDB
import com.mongodb.client.MongoClients;     // fábrica que crea conexiones
import com.mongodb.client.MongoCollection;  // una colección = un cajón de fichas (tabla)
import com.mongodb.client.MongoDatabase;    // una base de datos = un armario con cajones
import com.mongodb.client.model.Filters;    // herramientas para escribir condiciones (filtros)
import com.mongodb.client.model.Projections; // herramienta para elegir qué campos mostrar
import com.mongodb.client.model.Sorts;      // herramienta para ordenar resultados

/*
 * ============================================================
 * EJERCICIOS UD1 MONGODB - BIBLIOTECA
 * ============================================================
 *
 * ¿QUÉ ES MONGODB? (en una frase)
 * Una base de datos donde la información se guarda en "fichas" (documentos)
 * agrupadas en "cajones" (colecciones). Cada ficha es como un JSON.
 *
 * Esquema de nuestra base de datos "biblioteca":
 *   - Libros    : { isbn, titulo, autor: {nombre, apellido, nacionalidad}, editorial,
 *                   genero: [...], año_publicacion, paginas }
 *   - Socios    : { Num_socio (número), Nombre }
 *   - Prestamos : { socio: {num_socio (texto), ...}, libro: [ {isbn, ...}, ... ],
 *                   fecha_prestamo }
 *
 * Palabras clave que verás:
 *   - find()         -> "buscar fichas".
 *   - Filters.eq     -> "igual a" (ejemplo: título igual a "El Quijote").
 *   - first()        -> "dame solo la primera ficha que encuentres".
 *   - for (...)      -> "para cada ficha de la lista, haz esto".
 *   - Document       -> una ficha. Se usa get("campo") para leer un dato.
 */
public class Biblioteca_insert_update {

    // main = el punto por donde empieza el programa. Java siempre empieza aquí.
    public static void main(String[] args) {

        // ------------------------------------------------------------
        // PASO 1: ABRIR LA CONEXIÓN con MongoDB.
        // MongoClients.create() sin nada dentro = conecta con el servidor
        // que está en TU ordenador (localhost) en el puerto 27017.
        // Es como encender el teléfono antes de llamar.
        // ------------------------------------------------------------
        MongoClient mongoCliente = MongoClients.create();

        // "try" = "intenta esto". Si algo falla, salta al "catch" (ver abajo).
        try {

            // PASO 2: elegir la base de datos "biblioteca" (el armario).
            MongoDatabase baseDatos = mongoCliente.getDatabase("biblioteca");

            // PASO 3: elegir los tres cajones que vamos a usar.
            MongoCollection<Document> col_libros = baseDatos.getCollection("Libros");
            MongoCollection<Document> col_Prestamos = baseDatos.getCollection("Prestamos");
            MongoCollection<Document> col_socios = baseDatos.getCollection("Socios");

            // ------------------------------------------------------------
            // LIBRO DE PRUEBA
            // Antes de consultar, metemos un libro de ejemplo.
            // Solo lo metemos si NO existe ya (miramos si hay alguno con isbn "12345").
            // Así evitamos que se repita cada vez que ejecutamos el programa.
            // ------------------------------------------------------------

            // countDocuments cuenta cuántas fichas cumplen la condición. 0 = ninguna.
            if (col_libros.countDocuments(Filters.eq("isbn", "12345")) == 0) {

                // Creamos una lista vacía y le metemos los géneros uno por uno.
                List<String> generos = new ArrayList<>();
                generos.add("aventura");
                generos.add("fantasia");
                generos.add("juvenil");

                // Creamos la ficha del libro. Cada .append añade un campo nuevo.
                // Fíjate: "autor" es una ficha DENTRO de otra ficha (un documento anidado).
                Document libro = new Document("isbn", "12345")
                        .append("titulo", "el dia de la trilla")
                        .append("autor", new Document("nombre", "rebeca")
                                .append("apellido", "yarros")
                                .append("nacionalidad", "Americana"))
                        .append("editorial", "planeta")
                        .append("genero", generos)
                        .append("año_publicacion", 2026)
                        .append("paginas", 900);

                // insertOne = "guarda esta ficha en el cajón Libros".
                col_libros.insertOne(libro);
                System.out.println("Libro de prueba insertado.");

            } else {
                // Si ya existe, no lo volvemos a meter.
                System.out.println("El libro de prueba ya existe, no se inserta de nuevo.");
            }

            // ============================================================
            // EJERCICIO 1: primer libro de la colección
            // find().first() = "busca sin filtro y dame la primera ficha".
            // ============================================================
            Document primerLibro = col_libros.find().first();
            if (primerLibro != null) {   // "!= null" = "si existe alguna ficha"
                System.out.println("Ej1 - isbn del primer libro: " + primerLibro.get("isbn"));
                // "autor" está dentro de la ficha: lo sacamos y lo tratamos como ficha (Document).
                Document autor = (Document) primerLibro.get("autor");
                System.out.println("Ej1 - nombre del autor: " + autor.get("nombre"));
            }

            // ============================================================
            // EJERCICIO 2: todos los títulos
            // find() sin nada = "dame todas las fichas".
            // for (Document doc : ...) = "para cada ficha, llamándola doc, haz esto".
            // ============================================================
            System.out.println("\nEj2 - todos los títulos:");
            for (Document doc : col_libros.find()) {
                System.out.println("  Ej2 - " + doc.get("titulo"));  // solo el campo titulo
            }

            // ============================================================
            // EJERCICIO 3: lista de socios (ficha completa)
            // toJson() convierte la ficha en texto JSON para verla entera.
            // ============================================================
            System.out.println("\nEj3 - lista de socios:");
            for (Document doc : col_socios.find()) {
                System.out.println("  Ej3 - " + doc.toJson());
            }

            // ============================================================
            // EJERCICIO 4: nombre de los socios
            // Igual que el 3, pero mostramos solo el campo Nombre.
            // ============================================================
            System.out.println("\nEj4 - nombres de socios:");
            for (Document doc : col_socios.find()) {
                System.out.println("  Ej4 - " + doc.get("Nombre"));
            }

            // ============================================================
            // EJERCICIO 5: libro con título "El Quijote"
            // Filters.eq("titulo", "El Quijote") = "donde el título sea IGUAL a El Quijote".
            // Ojo: distingue mayúsculas y minúsculas.
            // ============================================================
            System.out.println("\nEj5 - libro El Quijote:");
            for (Document doc : col_libros.find(Filters.eq("titulo", "El Quijote"))) {
                System.out.println("  Ej5 - " + doc.toJson());
            }

            // ============================================================
            // EJERCICIO 6: préstamos con exactamente 2 libros
            // "libro" es una LISTA dentro del préstamo.
            // Filters.size("libro", 2) = "donde la lista tenga 2 elementos".
            // ============================================================
            System.out.println("\nEj6 - préstamos con 2 libros:");
            for (Document doc : col_Prestamos.find(Filters.size("libro", 2))) {
                System.out.println("  Ej6 - " + doc.toJson());
            }

            // ============================================================
            // EJERCICIO 7: títulos de los libros de Follet
            // "autor.apellido" = entra en la ficha "autor" y mira su campo "apellido".
            // El punto significa "dentro de".
            // ============================================================
            System.out.println("\nEj7 - libros de Follet:");
            for (Document doc : col_libros.find(Filters.eq("autor.apellido", "Follet"))) {
                System.out.println("  Ej7 - " + doc.get("titulo"));
            }

            // ============================================================
            // EJERCICIO 8: préstamo del libro con isbn 446854
            // "libro.isbn" busca dentro de la lista "libro" si alguna ficha tiene ese isbn.
            // ============================================================
            System.out.println("\nEj8 - préstamo del isbn 446854:");
            for (Document doc : col_Prestamos.find(Filters.eq("libro.isbn", "446854"))) {
                System.out.println("  Ej8 - " + doc.toJson());
            }

            // ============================================================
            // EJERCICIO 9: nombres de los socios que tuvieron el libro 456779
            // Recorrido (como seguir un hilo):
            //   1. Buscar préstamos que tengan ese libro.
            //   2. De cada préstamo, sacar el número de socio.
            //   3. Con ese número, buscar el nombre en la colección Socios.
            // ============================================================
            System.out.println("\nEj9 - socios que tuvieron el libro 456779:");
            for (Document prestamo : col_Prestamos.find(Filters.eq("libro.isbn", "456779"))) {
                // Paso 2a: sacamos la ficha "socio" de dentro del préstamo.
                Document socioPrestamo = (Document) prestamo.get("socio");
                // Paso 2b: en Prestamos el número es TEXTO ("3"); lo convertimos a número entero (3).
                int numSocio = Integer.parseInt(socioPrestamo.getString("num_socio"));
                // Paso 3: buscamos al socio cuyo Num_socio sea ese número. first() = el primero que aparezca.
                Document socio = col_socios.find(Filters.eq("Num_socio", numSocio)).first();
                if (socio != null) {     // si lo encontramos, mostramos su nombre
                    System.out.println("  Ej9 - " + socio.get("Nombre"));
                }
            }

            // ============================================================
            // EJERCICIO 10: año mayor que 2000 Y más de 500 páginas
            // Filters.and(...) = "se tienen que cumplir TODAS las condiciones".
            // gt = mayor que (greater than).
            // ============================================================
            System.out.println("\nEj10 - año > 2000 y más de 500 páginas:");
            for (Document doc : col_libros.find(Filters.and(
                    Filters.gt("año_publicacion", 2000),
                    Filters.gt("paginas", 500)))) {
                System.out.println("  Ej10 - " + doc.get("titulo"));
            }

            // ============================================================
            // EJERCICIO 11: editados entre 2010 y 2020 (los dos años incluidos)
            // gte = mayor o igual (greater than or equal)
            // lte = menor o igual (less than or equal)
            // ============================================================
            System.out.println("\nEj11 - editados entre 2010 y 2020:");
            for (Document doc : col_libros.find(Filters.and(
                    Filters.gte("año_publicacion", 2010),
                    Filters.lte("año_publicacion", 2020)))) {
                System.out.println("  Ej11 - " + doc.get("titulo"));
            }

            // ============================================================
            // EJERCICIO 12: libros de la editorial Debolsillo
            // Igualdad exacta sobre el campo editorial.
            // ============================================================
            System.out.println("\nEj12 - editorial Debolsillo:");
            for (Document doc : col_libros.find(Filters.eq("editorial", "Debolsillo"))) {
                System.out.println("  Ej12 - " + doc.get("titulo"));
            }

            // ============================================================
            // EJERCICIO 13: nombre del socio número 2
            // Aquí Num_socio es NÚMERO (2), no texto ("2"), por eso va sin comillas.
            // ============================================================
            System.out.println("\nEj13 - socio 2:");
            Document socio2 = col_socios.find(Filters.eq("Num_socio", 2)).first();
            if (socio2 != null) {
                System.out.println("  Ej13 - nombre: " + socio2.get("Nombre"));
            }

            // ============================================================
            // EJERCICIO 14: título y nombre del autor de los libros
            // que sean de autores americanos O de la editorial planeta.
            // Filters.or(...) = "basta con que se cumpla UNA de las condiciones".
            // Projections = elegir qué campos queremos ver en el resultado:
            //   include(...)  -> solo estos campos
            //   excludeId()   -> quita el _id (el número interno de MongoDB)
            // ============================================================
            System.out.println("\nEj14 - americanos o editorial planeta (título y nombre autor):");
            for (Document doc : col_libros.find(Filters.or(
                    Filters.eq("autor.nacionalidad", "Americana"),
                    Filters.eq("editorial", "planeta")))
                    .projection(Projections.fields(
                            Projections.include("titulo", "autor.nombre"),
                            Projections.excludeId()))) {
                System.out.println("  Ej14 - " + doc.toJson());
            }

            // ============================================================
            // EJERCICIO 15: fecha de préstamo del socio 1
            // En Prestamos, num_socio es TEXTO, por eso lo buscamos como "1" (entre comillas).
            // ============================================================
            System.out.println("\nEj15 - fecha de préstamo del socio 1:");
            for (Document doc : col_Prestamos.find(Filters.eq("socio.num_socio", "1"))) {
                System.out.println("  Ej15 - " + doc.get("fecha_prestamo"));
            }

            // ============================================================
            // EJERCICIO 16: libros editados entre 1990 y 2000
            // ============================================================
            System.out.println("\nEj16 - editados entre 1990 y 2000:");
            for (Document doc : col_libros.find(Filters.and(
                    Filters.gte("año_publicacion", 1990),
                    Filters.lte("año_publicacion", 2000)))) {
                System.out.println("  Ej16 - " + doc.get("titulo"));
            }

            // ============================================================
            // EJERCICIO 17: libros de Asimov, ordenados por título (A a Z)
            // sort(Sorts.ascending("titulo")) = "ordénalos alfabéticamente".
            // ============================================================
            System.out.println("\nEj17 - libros de Asimov (ordenados por título):");
            for (Document doc : col_libros.find(Filters.eq("autor.apellido", "Asimov"))
                    .sort(Sorts.ascending("titulo"))) {
                System.out.println("  Ej17 - " + doc.get("titulo"));
            }

            // ============================================================
            // EJERCICIO 18: socios que se llevaron "El Quijote"
            // Pasos:
            //   1. Buscar el libro El Quijote y coger su isbn.
            //   2. Buscar los préstamos con ese isbn.
            //   3. Por cada préstamo, sacar el socio y su nombre (igual que el ejercicio 9).
            // ============================================================
            System.out.println("\nEj18 - socios que se llevaron El Quijote:");
            Document quijote = col_libros.find(Filters.eq("titulo", "El Quijote")).first();
            if (quijote != null) {
                // quijote.getString("isbn") = el texto del campo isbn de esa ficha.
                for (Document prestamo : col_Prestamos.find(Filters.eq("libro.isbn", quijote.getString("isbn")))) {
                    Document socioPrestamo = (Document) prestamo.get("socio");
                    int numSocio = Integer.parseInt(socioPrestamo.getString("num_socio"));
                    Document socio = col_socios.find(Filters.eq("Num_socio", numSocio)).first();
                    if (socio != null) {
                        System.out.println("  Ej18 - " + socio.get("Nombre"));
                    }
                }
            } else {
                System.out.println("  No existe ningún libro con ese título.");
            }

            // ============================================================
            // EJERCICIO 19: títulos de los libros que tiene prestados Javier García
            // Pasos:
            //   1. Buscar al socio por nombre y coger su Num_socio.
            //   2. Buscar sus préstamos (num_socio en Prestamos es TEXTO).
            //   3. Por cada libro del préstamo, buscar su título en Libros.
            // ============================================================
            System.out.println("\nEj19 - libros prestados a Javier García:");
            Document socioJavier = col_socios.find(Filters.eq("Nombre", "Javier García")).first();
            if (socioJavier != null) {
                // Pasamos el número a texto porque en Prestamos se guarda como texto.
                String numJavier = String.valueOf(socioJavier.get("Num_socio"));

                for (Document prestamo : col_Prestamos.find(Filters.eq("socio.num_socio", numJavier))) {
                    // "libro" es una lista de fichas dentro del préstamo.
                    // La anotación @SuppressWarnings solo calla un aviso de Java sobre el "cast" (conversión).
                    @SuppressWarnings("unchecked")
                    List<Document> librosDelPrestamo = (List<Document>) prestamo.get("libro");

                    for (Document lib : librosDelPrestamo) {
                        // Buscamos en Libros el libro con ese isbn y sacamos su título.
                        Document libroEncontrado = col_libros.find(
                                Filters.eq("isbn", lib.getString("isbn"))).first();
                        if (libroEncontrado != null) {
                            System.out.println("  Ej19 - " + libroEncontrado.get("titulo"));
                        }
                    }
                }
            } else {
                System.out.println("  No existe ningún socio con ese nombre.");
            }

            // ============================================================
            // EJERCICIO 20: título y autor de los libros de planeta, ordenados por año
            // Projections.include("titulo", "autor") = mostrar solo título y autor.
            // ============================================================
            System.out.println("\nEj20 - libros de planeta (por año):");
            for (Document doc : col_libros.find(Filters.eq("editorial", "planeta"))
                    .sort(Sorts.ascending("año_publicacion"))
                    .projection(Projections.fields(
                            Projections.include("titulo", "autor"),
                            Projections.excludeId()))) {
                System.out.println("  Ej20 - " + doc.toJson());
            }

            // ============================================================
            // EJERCICIO 21: título y editorial de todos los libros, ordenados por páginas
            // ============================================================
            System.out.println("\nEj21 - título y editorial (por páginas):");
            for (Document doc : col_libros.find()
                    .sort(Sorts.ascending("paginas"))
                    .projection(Projections.fields(
                            Projections.include("titulo", "editorial"),
                            Projections.excludeId()))) {
                System.out.println("  Ej21 - " + doc.toJson());
            }

            // ============================================================
            // EJERCICIO 22: títulos de los libros que tienen algún préstamo
            // Pasos:
            //   1. distinct("libro.isbn", ...) = "dame la lista de isbn que aparecen en préstamos,
            //      sin repetir". Lo guardamos en una lista normal.
            //   2. Filters.in("isbn", lista) = "donde el isbn esté dentro de esa lista".
            // ============================================================
            System.out.println("\nEj22 - libros con algún préstamo:");
            List<String> isbnPrestados = new ArrayList<>();
            for (String isbn : col_Prestamos.distinct("libro.isbn", String.class)) {
                isbnPrestados.add(isbn);   // metemos cada isbn en nuestra lista
            }
            for (Document doc : col_libros.find(Filters.in("isbn", isbnPrestados))) {
                System.out.println("  Ej22 - " + doc.get("titulo"));
            }

        } catch (Exception e) {
            // "catch" = si ocurrió un error en el "try" de arriba, entramos aquí.
            // printStackTrace muestra qué falló y dónde (útil para depurar).
            e.printStackTrace();

        } finally {
            // "finally" = se ejecuta SIEMPRE, haya habido error o no.
            // Cerramos la conexión, como colgar el teléfono al terminar la llamada.
            mongoCliente.close();
        }
    }
}
