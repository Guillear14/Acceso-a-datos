import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;

/*
 * EJERCICIOS UD1 MONGODB - BIBLIOTECA
 *
 * Base de datos: "biblioteca"
 * Colecciones:
 *   - Libros    : { isbn, titulo, autor: {nombre, apellido, nacionalidad}, editorial,
 *                   genero: [...], año_publicacion, paginas }
 *   - Socios    : { Num_socio (número), Nombre }
 *   - Prestamos : { socio: {num_socio (String), ...}, libro: [ {isbn, ...}, ... ],
 *                   fecha_prestamo }
 *
 * Los nombres de campo se han deducido del código original. Si en tu base de datos
 * son distintos, cambia los filtros.
 *
 * Índice rápido:
 *   Ej 1-4   : consultas básicas (primer libro, todos los libros, socios)
 *   Ej 5-9   : filtros Bson
 *   Ej 10-16 : filtros combinados, proyecciones y ordenación
 *   Ej 17-19 : búsquedas por autor, título y socio, con relaciones entre colecciones
 *   Ej 20-22 : ordenación, proyección y consultas sobre arrays
 */
public class Biblioteca_insert_update {

    // Colecciones en static para usarlas desde los métodos auxiliares
    static MongoCollection<Document> col_libros;
    static MongoCollection<Document> col_Prestamos;
    static MongoCollection<Document> col_socios;

    public static void main(String[] args) {

        // Conexión a MongoDB en localhost:27017 (por defecto)
        MongoClient mongoCliente = MongoClients.create();
        MongoDatabase baseDatos = mongoCliente.getDatabase("biblioteca");
        col_libros = baseDatos.getCollection("Libros");
        col_Prestamos = baseDatos.getCollection("Prestamos");
        col_socios = baseDatos.getCollection("Socios");

        try {

            // ==============================================================
            // INSERCIÓN DE UN LIBRO DE PRUEBA
            // Solo se inserta si no existe ya un libro con isbn "12345".
            // Así evitamos duplicados al ejecutar el programa varias veces.
            // ==============================================================
            insertarLibroSiNoExiste();

            // ==============================================================
            // EJERCICIO 1: Buscar el registro del primer libro
            // find().first() devuelve el primer documento de la colección
            // (sin filtro ni orden, es el primero en el orden natural).
            // ==============================================================
            Document primerLibro = col_libros.find().first();
            if (primerLibro != null) {
                System.out.println("Ej1 - isbn del primer libro: " + primerLibro.get("isbn"));
                // "autor" es un documento embebido, hay que castearlo
                Document autor = (Document) primerLibro.get("autor");
                System.out.println("Ej1 - nombre del autor: " + autor.get("nombre"));
            }

            // ==============================================================
            // EJERCICIO 2: Buscar todos los libros
            // find() sin filtro devuelve todos los documentos de la colección.
            // ==============================================================
            System.out.println("\nEj2 - todos los títulos:");
            mostrar(col_libros.find(), "  Ej2 - ", "titulo");

            // ==============================================================
            // EJERCICIO 3: Obtener la lista de socios
            // Se muestran los documentos completos en formato JSON.
            // ==============================================================
            System.out.println("\nEj3 - lista de socios:");
            mostrar(col_socios.find(), "  Ej3 - ", null);

            // ==============================================================
            // EJERCICIO 4: Obtener el nombre de los socios (usando get())
            // Recorremos los socios y sacamos solo el campo "Nombre".
            // ==============================================================
            System.out.println("\nEj4 - nombres de socios:");
            mostrar(col_socios.find(), "  Ej4 - ", "Nombre");

            // ==============================================================
            // EJERCICIO 5: Buscar el libro con título "El Quijote"
            // Filters.eq compara igualdad exacta (distingue mayúsculas).
            // ==============================================================
            System.out.println("\nEj5 - libro El Quijote:");
            mostrar(col_libros.find(Filters.eq("titulo", "El Quijote")), "  Ej5 - ", null);

            // ==============================================================
            // EJERCICIO 6: Buscar el préstamo con dos libros
            // Filters.size comprueba cuántos elementos tiene un array.
            // Aquí "libro" es un array y buscamos los que tienen 2 elementos.
            // ==============================================================
            System.out.println("\nEj6 - préstamos con 2 libros:");
            mostrar(col_Prestamos.find(Filters.size("libro", 2)), "  Ej6 - ", null);

            // ==============================================================
            // EJERCICIO 7: Títulos de los libros del autor con apellido Follet
            // "autor.apellido" accede a un campo dentro del documento "autor".
            // ==============================================================
            System.out.println("\nEj7 - libros de Follet:");
            mostrar(col_libros.find(Filters.eq("autor.apellido", "Follet")), "  Ej7 - ", "titulo");

            // ==============================================================
            // EJERCICIO 8: Documento del préstamo del libro con ISBN 446854
            // "libro.isbn" busca dentro del array "libro" cualquier elemento
            // con ese isbn. Devuelve el préstamo completo.
            // ==============================================================
            System.out.println("\nEj8 - préstamo del isbn 446854:");
            mostrar(col_Prestamos.find(Filters.eq("libro.isbn", "446854")), "  Ej8 - ", null);

            // ==============================================================
            // EJERCICIO 9: Nombre de los socios que han tenido prestado 456779
            // Usa el método auxiliar imprimirSociosPorIsbn() (ver abajo),
            // que hace el recorrido préstamo -> socio -> nombre.
            // ==============================================================
            System.out.println("\nEj9 - socios que tuvieron el libro 456779:");
            imprimirSociosPorIsbn("456779");

            // ==============================================================
            // EJERCICIO 10: Libros con año > 2000 Y más de 500 páginas
            // Filters.and combina condiciones (todas deben cumplirse).
            // ==============================================================
            System.out.println("\nEj10 - año > 2000 y más de 500 páginas:");
            mostrar(col_libros.find(Filters.and(
                    Filters.gt("año_publicacion", 2000),
                    Filters.gt("paginas", 500))), "  Ej10 - ", "titulo");

            // ==============================================================
            // EJERCICIO 11: Libros editados entre 2010 y 2020 (ambos incluidos)
            // gte = mayor o igual, lte = menor o igual.
            // ==============================================================
            System.out.println("\nEj11 - editados entre 2010 y 2020:");
            mostrar(col_libros.find(Filters.and(
                    Filters.gte("año_publicacion", 2010),
                    Filters.lte("año_publicacion", 2020))), "  Ej11 - ", "titulo");

            // ==============================================================
            // EJERCICIO 12: Libros de la editorial «Debolsillo»
            // Igualdad exacta sobre el campo "editorial".
            // ==============================================================
            System.out.println("\nEj12 - editorial Debolsillo:");
            mostrar(col_libros.find(Filters.eq("editorial", "Debolsillo")), "  Ej12 - ", "titulo");

            // ==============================================================
            // EJERCICIO 13: Nombre del socio 2
            // Num_socio es numérico en la colección Socios, por eso
            // se busca con el número 2 (no con el texto "2").
            // ==============================================================
            System.out.println("\nEj13 - socio 2:");
            Document socio2 = col_socios.find(Filters.eq("Num_socio", 2)).first();
            if (socio2 != null) {
                System.out.println("  Ej13 - nombre: " + socio2.get("Nombre"));
            }

            // ==============================================================
            // EJERCICIO 14: Título y nombre del autor de los libros de autores
            // americanos O de la editorial «planeta»
            // Filters.or = al menos una condición.
            // Projections.include saca solo titulo y autor.nombre.
            // Projections.excludeId quita el campo _id del resultado.
            // ==============================================================
            System.out.println("\nEj14 - americanos o editorial planeta (título y nombre autor):");
            mostrar(col_libros.find(Filters.or(
                    Filters.eq("autor.nacionalidad", "Americana"),
                    Filters.eq("editorial", "planeta")))
                    .projection(Projections.fields(
                            Projections.include("titulo", "autor.nombre"),
                            Projections.excludeId())), "  Ej14 - ", null);

            // ==============================================================
            // EJERCICIO 15: Fecha de préstamo del libro prestado al socio 1
            // En Prestamos, num_socio se guarda como texto (String),
            // por eso se compara con "1" y no con el número 1.
            // ==============================================================
            System.out.println("\nEj15 - fecha de préstamo del socio 1:");
            mostrar(col_Prestamos.find(Filters.eq("socio.num_socio", "1")), "  Ej15 - ", "fecha_prestamo");

            // ==============================================================
            // EJERCICIO 16: Libros editados entre 1990 y 2000
            // ==============================================================
            System.out.println("\nEj16 - editados entre 1990 y 2000:");
            mostrar(col_libros.find(Filters.and(
                    Filters.gte("año_publicacion", 1990),
                    Filters.lte("año_publicacion", 2000))), "  Ej16 - ", "titulo");

            // ==============================================================
            // EJERCICIO 17: Títulos de los libros escritos por Asimov
            // sort(Sorts.ascending("titulo")) ordena alfabéticamente.
            // ==============================================================
            System.out.println("\nEj17 - libros de Asimov (ordenados por título):");
            mostrar(col_libros.find(Filters.eq("autor.apellido", "Asimov"))
                    .sort(Sorts.ascending("titulo")), "  Ej17 - ", "titulo");

            // ==============================================================
            // EJERCICIO 18: Nombre de los socios que se llevaron «El Quijote»
            // Primero se busca el libro para obtener su isbn, y después
            // se usa el método auxiliar imprimirSociosPorIsbn().
            // ==============================================================
            System.out.println("\nEj18 - socios que se llevaron El Quijote:");
            Document quijote = col_libros.find(Filters.eq("titulo", "El Quijote")).first();
            if (quijote != null) {
                imprimirSociosPorIsbn(quijote.getString("isbn"));
            } else {
                System.out.println("  No existe ningún libro con ese título.");
            }

            // ==============================================================
            // EJERCICIO 19: Título de los libros que tiene prestados el socio
            // «Javier García»
            // Pasos:
            //   1. Buscar el socio por nombre y coger su Num_socio.
            //   2. Buscar en Prestamos los préstamos de ese socio.
            //   3. Por cada libro del préstamo, buscar su título en Libros.
            // ==============================================================
            System.out.println("\nEj19 - libros prestados a Javier García:");
            Document socioJavier = col_socios.find(Filters.eq("Nombre", "Javier García")).first();
            if (socioJavier != null) {
                // En Prestamos, num_socio es String, así que lo convertimos
                String numJavier = String.valueOf(socioJavier.get("Num_socio"));

                for (Document prestamo : col_Prestamos.find(Filters.eq("socio.num_socio", numJavier))) {
                    // "libro" es un array de documentos, cada uno con su isbn
                    @SuppressWarnings("unchecked")
                    List<Document> librosDelPrestamo = (List<Document>) prestamo.get("libro");
                    for (Document lib : librosDelPrestamo) {
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

            // ==============================================================
            // EJERCICIO 20: Título y autor de los libros de «planeta»
            // ordenados por año de edición
            // La proyección incluye titulo y autor (documento completo).
            // ==============================================================
            System.out.println("\nEj20 - libros de planeta (por año):");
            mostrar(col_libros.find(Filters.eq("editorial", "planeta"))
                    .sort(Sorts.ascending("año_publicacion"))
                    .projection(Projections.fields(
                            Projections.include("titulo", "autor"),
                            Projections.excludeId())), "  Ej20 - ", null);

            // ==============================================================
            // EJERCICIO 21: Título y editorial de los libros ordenados
            // por número de páginas (proyección)
            // ==============================================================
            System.out.println("\nEj21 - título y editorial (por páginas):");
            mostrar(col_libros.find()
                    .sort(Sorts.ascending("paginas"))
                    .projection(Projections.fields(
                            Projections.include("titulo", "editorial"),
                            Projections.excludeId())), "  Ej21 - ", null);

            // ==============================================================
            // EJERCICIO 22: Título de los libros que tienen algún préstamo
            // 1. distinct() saca la lista de isbn que aparecen en Prestamos.
            // 2. Filters.in busca en Libros los que estén en esa lista.
            // ==============================================================
            System.out.println("\nEj22 - libros con algún préstamo:");
            List<String> isbnPrestados = new ArrayList<>();
            for (String isbn : col_Prestamos.distinct("libro.isbn", String.class)) {
                isbnPrestados.add(isbn);
            }
            mostrar(col_libros.find(Filters.in("isbn", isbnPrestados)), "  Ej22 - ", "titulo");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Cerrar siempre la conexión al terminar
            mongoCliente.close();
        }
    }

    // ==================================================================
    // MÉTODOS AUXILIARES
    // ==================================================================

    /**
     * Muestra los resultados de una consulta.
     * @param resultados la consulta (find, sort, projection...)
     * @param etiqueta   texto que se pone delante de cada línea
     * @param campo      si es null, muestra el documento completo en JSON;
     *                   si no, muestra solo ese campo
     */
    static void mostrar(FindIterable<Document> resultados, String etiqueta, String campo) {
        // for-each cierra el cursor automáticamente al terminar
        for (Document doc : resultados) {
            if (campo == null) {
                System.out.println(etiqueta + doc.toJson());
            } else {
                System.out.println(etiqueta + doc.get(campo));
            }
        }
    }

    /**
     * Imprime el nombre de los socios que tienen prestado un libro con ese isbn.
     * Recorrido: Prestamos (filtrar por libro.isbn) -> socio.num_socio -> Socios.Nombre
     * Se usa en los ejercicios 9 y 18.
     */
    static void imprimirSociosPorIsbn(String isbn) {
        for (Document prestamo : col_Prestamos.find(Filters.eq("libro.isbn", isbn))) {
            // "socio" es un documento embebido dentro del préstamo
            Document socioPrestamo = (Document) prestamo.get("socio");
            // En Prestamos, num_socio es String
            String numSocioTexto = socioPrestamo.getString("num_socio");
            // En Socios, Num_socio es número: convertimos el texto a int
            int numSocio = Integer.parseInt(numSocioTexto);

            Document socio = col_socios.find(Filters.eq("Num_socio", numSocio)).first();
            if (socio != null) {
                System.out.println("  - " + socio.get("Nombre"));
            }
        }
    }

    /**
     * Inserta el libro de prueba solo si todavía no existe (comprueba el isbn "12345").
     * Así no se crean duplicados al ejecutar el programa varias veces.
     */
    static void insertarLibroSiNoExiste() {
        String isbn = "12345";
        if (col_libros.countDocuments(Filters.eq("isbn", isbn)) > 0) {
            System.out.println("El libro de prueba ya existe, no se inserta de nuevo.");
            return;
        }

        String titulo = "el dia de la trilla";
        String nombre = "rebeca";
        String apellido = "yarros";
        String nacionalidad = "Americana";
        String editorial = "planeta";
        int añoPublicacion = 2026;
        int paginas = 900;
        List<String> generos = new ArrayList<>();
        generos.add("aventura");
        generos.add("fantasia");
        generos.add("juvenil");

        Document libro = new Document("isbn", isbn)
                .append("titulo", titulo)
                .append("autor", new Document("nombre", nombre)
                        .append("apellido", apellido)
                        .append("nacionalidad", nacionalidad))
                .append("editorial", editorial)
                .append("genero", generos)
                .append("año_publicacion", añoPublicacion)
                .append("paginas", paginas);

        col_libros.insertOne(libro);
        System.out.println("Libro de prueba insertado.");
    }
}
