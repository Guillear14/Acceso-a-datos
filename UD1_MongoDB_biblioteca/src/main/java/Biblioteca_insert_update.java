import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;

public class Biblioteca_insert_update {

    // Colecciones como static para poder usarlas en métodos auxiliares
    static MongoCollection<Document> col_libros;
    static MongoCollection<Document> col_Prestamos;
    static MongoCollection<Document> col_socios;

    public static void main(String[] args) {

        MongoClient mongoCliente = MongoClients.create();
        MongoDatabase baseDatos = mongoCliente.getDatabase("biblioteca");
        col_libros = baseDatos.getCollection("Libros");
        col_Prestamos = baseDatos.getCollection("Prestamos");
        col_socios = baseDatos.getCollection("Socios");

        // Datos del libro a insertar
        String titulo = "el dia de la trilla";
        String nombre = "rebeca";
        String apellido = "yarros";
        String nacionalidad = "Americana";
        String editorial = "planeta";
        int año_publicacion = 2026;
        int páginas = 900;
        List<String> género = new ArrayList<>();
        género.add("aventura");
        género.add("fantasia");
        género.add("juvenil");

        MongoCursor<Document> cursor;
        Document libro;
        Document autor;
        Bson_placeholder_removed();

        try {

            // ======================================================
            // EJERCICIO 2: Buscar todos los libros
            // ======================================================
            cursor = col_libros.find().iterator();
            while (cursor.hasNext()) {
                System.out.println("Ej2 - título: " + cursor.next().get("titulo"));
            }
            cursor.close();

            // ======================================================
            // EJERCICIO 1: Buscar el registro del primer libro
            // ======================================================
            libro = col_libros.find().first();
            if (libro != null) {
                System.out.println("Ej1 - isbn primer libro: " + libro.get("isbn"));
                autor = (Document) libro.get("autor");
                System.out.println("Ej1 - nombre autor primer libro: " + autor.get("nombre"));
            }

            // Insertar un libro nuevo (OJO: cada ejecución crea un duplicado con el mismo isbn)
            libro = new Document("isbn", "12345")
                    .append("titulo", titulo)
                    .append("autor", new Document("nombre", nombre).append("apellido", apellido).append("nacionalidad", nacionalidad))
                    .append("editorial", editorial)
                    .append("genero", género)
                    .append("año_publicacion", año_publicacion)
                    .append("paginas", páginas);
            col_libros.insertOne(libro);

            // Filtros de años (originales)
            cursor = col_libros.find(Filters.gte("año_publicacion", 2020)).iterator();
            while (cursor.hasNext()) {
                System.out.println("Libros publicados >= 2020: " + cursor.next().get("titulo"));
            }
            cursor.close();

            cursor = col_libros.find(Filters.and(
                    Filters.gte("año_publicacion", 2000),
                    Filters.lte("año_publicacion", 2020))).iterator();
            while (cursor.hasNext()) {
                System.out.println("Año entre 2000 y 2020: " + cursor.next().get("año_publicacion"));
            }
            cursor.close();

            // ======================================================
            // EJERCICIO 6: Buscar el préstamo con dos libros
            // (array "libro" con tamaño 2)
            // ======================================================
            cursor = col_Prestamos.find(Filters.size("libro", 2)).iterator();
            while (cursor.hasNext()) {
                System.out.println("Ej6 - préstamo con 2 libros: " + cursor.next().toJson());
            }
            cursor.close();

            // ======================================================
            // EJERCICIO 7: Títulos de libros del autor con apellido Follet
            // ======================================================
            cursor = col_libros.find(Filters.eq("autor.apellido", "Follet")).iterator();
            while (cursor.hasNext()) {
                System.out.println("Ej7 - título de Follet: " + cursor.next().get("titulo"));
            }
            cursor.close();

            // ======================================================
            // EJERCICIO 8: Documento del préstamo del libro con ISBN 446854
            // ======================================================
            cursor = col_Prestamos.find(Filters.eq("libro.isbn", "446854")).iterator();
            while (cursor.hasNext()) {
                System.out.println("Ej8 - préstamo del isbn 446854: " + cursor.next().toJson());
            }
            cursor.close();

            // Título del libro con isbn 446854 (original)
            cursor = col_libros.find(Filters.eq("isbn", "446854")).iterator();
            while (cursor.hasNext()) {
                System.out.println("Título del isbn 446854: " + cursor.next().get("titulo"));
            }
            cursor.close();

            // ======================================================
            // EJERCICIO 3: Obtener la lista de socios
            // ======================================================
            cursor = col_socios.find().iterator();
            while (cursor.hasNext()) {
                System.out.println("Ej3 - socio: " + cursor.next().toJson());
            }
            cursor.close();

            // ======================================================
            // EJERCICIO 4: Obtener el nombre de los socios (usando get())
            // ======================================================
            cursor = col_socios.find().iterator();
            while (cursor.hasNext()) {
                Document socio = cursor.next();
                System.out.println("Ej4 - nombre socio: " + socio.get("Nombre"));
            }
            cursor.close();

            // ======================================================
            // EJERCICIO 5: Buscar el libro con título "El Quijote"
            // ======================================================
            cursor = col_libros.find(Filters.eq("titulo", "El Quijote")).iterator();
            while (cursor.hasNext()) {
                System.out.println("Ej5 - libro encontrado: " + cursor.next().toJson());
            }
            cursor.close();

            // ======================================================
            // EJERCICIO 9: Nombre de los socios que han tenido prestado el 456779
            // ======================================================
            System.out.println("Ej9 - socios que tuvieron el libro 456779:");
            imprimirSociosPorIsbn("456779");

            // ======================================================
            // EJERCICIO 10: Libros con año > 2000 Y más de 500 páginas
            // ======================================================
            cursor = col_libros.find(Filters.and(
                    Filters.gt("año_publicacion", 2000),
                    Filters.gt("paginas", 500))).iterator();
            while (cursor.hasNext()) {
                System.out.println("Ej10 - libro: " + cursor.next().get("titulo"));
            }
            cursor.close();

            // ======================================================
            // EJERCICIO 11: Libros editados entre 2010 y 2020
            // ======================================================
            cursor = col_libros.find(Filters.and(
                    Filters.gte("año_publicacion", 2010),
                    Filters.lte("año_publicacion", 2020))).iterator();
            while (cursor.hasNext()) {
                System.out.println("Ej11 - libro: " + cursor.next().get("titulo"));
            }
            cursor.close();

            // ======================================================
            // EJERCICIO 12: Libros de la editorial «Debolsillo»
            // ======================================================
            cursor = col_libros.find(Filters.eq("editorial", "Debolsillo")).iterator();
            while (cursor.hasNext()) {
                System.out.println("Ej12 - libro de Debolsillo: " + cursor.next().get("titulo"));
            }
            cursor.close();

            // ======================================================
            // EJERCICIO 13: Nombre del socio 2
            // ======================================================
            libro = null;
            Document socio2 = col_socios.find(Filters.eq("Num_socio", 2)).first();
            if (socio2 != null) {
                System.out.println("Ej13 - nombre del socio 2: " + socio2.get("Nombre"));
            }

            // ======================================================
            // EJERCICIO 14: Título y nombre del autor de los libros de autores
            // americanos O de la editorial planeta (con proyección)
            // ======================================================
            cursor = col_libros.find(Filters.or(
                    Filters.eq("autor.nacionalidad", "Americana"),
                    Filters.eq("editorial", "planeta")))
                    .projection(Projections.fields(
                            Projections.include("titulo", "autor.nombre"),
                            Projections.excludeId()))
                    .iterator();
            while (cursor.hasNext()) {
                System.out.println("Ej14 - " + cursor.next().toJson());
            }
            cursor.close();

            // ======================================================
            // EJERCICIO 15: Fecha de préstamo del libro prestado al socio num_socio 1
            // (en Prestamos, num_socio se guarda como String)
            // ======================================================
            cursor = col_Prestamos.find(Filters.eq("socio.num_socio", "1")).iterator();
            while (cursor.hasNext()) {
                System.out.println("Ej15 - fecha de préstamo: " + cursor.next().get("fecha_prestamo"));
            }
            cursor.close();

            // ======================================================
            // EJERCICIO 16: Libros editados entre 1990 y 2000
            // ======================================================
            cursor = col_libros.find(Filters.and(
                    Filters.gte("año_publicacion", 1990),
                    Filters.lte("año_publicacion", 2000))).iterator();
            while (cursor.hasNext()) {
                System.out.println("Ej16 - libro: " + cursor.next().get("titulo"));
            }
            cursor.close();

            // ======================================================
            // EJERCICIO 17: Títulos de los libros escritos por Isaac Asimov
            // ======================================================
            cursor = col_libros.find(Filters.eq("autor.apellido", "Asimov"))
                    .sort(Sorts.ascending("titulo")).iterator();
            while (cursor.hasNext()) {
                System.out.println("Ej17 - título Asimov: " + cursor.next().get("titulo"));
            }
            cursor.close();

            // ======================================================
            // EJERCICIO 18: Nombre de los socios que se llevaron prestado «El Quijote»
            // ======================================================
            Document quijote = col_libros.find(Filters.eq("titulo", "El Quijote")).first();
            if (quijote != null) {
                System.out.println("Ej18 - socios que se llevaron El Quijote:");
                imprimirSociosPorIsbn(quijote.getString("isbn"));
            }

            // ======================================================
            // EJERCICIO 19: Título de los libros prestados al socio «Javier García»
            // ======================================================
            Document socioJavier = col_socios.find(Filters.eq("Nombre", "Javier García")).first();
            if (socioJavier != null) {
                String numJavier = String.valueOf(socioJavier.get("Num_socio"));
                cursor = col_Prestamos.find(Filters.eq("socio.num_socio", numJavier)).iterator();
                while (cursor.hasNext()) {
                    // "libro" es un array de documentos, cada uno con su isbn
                    @SuppressWarnings("unchecked")
                    List<Document> librosPrestados = (List<Document>) cursor.next().get("libro");
                    for (Document lib : librosPrestados) {
                        Document libroEncontrado = col_libros.find(
                                Filters.eq("isbn", lib.getString("isbn"))).first();
                        if (libroEncontrado != null) {
                            System.out.println("Ej19 - libro prestado a Javier: " + libroEncontrado.get("titulo"));
                        }
                    }
                }
                cursor.close();
            }

            // ======================================================
            // EJERCICIO 20: Título y autor de los libros de «planeta»
            // ordenados por año de edición
            // ======================================================
            cursor = col_libros.find(Filters.eq("editorial", "planeta"))
                    .sort(Sorts.ascending("año_publicacion"))
                    .projection(Projections.fields(
                            Projections.include("titulo", "autor"),
                            Projections.excludeId()))
                    .iterator();
            while (cursor.hasNext()) {
                System.out.println("Ej20 - " + cursor.next().toJson());
            }
            cursor.close();

            // ======================================================
            // EJERCICIO 21: Título y editorial (proyección) de los libros
            // ordenados por número de páginas
            // ======================================================
            cursor = col_libros.find()
                    .projection(Projections.fields(
                            Projections.include("titulo", "editorial"),
                            Projections.excludeId()))
                    .sort(Sorts.ascending("paginas"))
                    .iterator();
            while (cursor.hasNext()) {
                System.out.println("Ej21 - " + cursor.next().toJson());
            }
            cursor.close();

            // ======================================================
            // EJERCICIO 22: Título de los libros que tienen algún préstamo
            // Se obtienen los isbn distintos de Prestamos y se buscan en Libros
            // ======================================================
            List<String> isbnPrestados = new ArrayList<>();
            for (String isbn : col_Prestamos.distinct("libro.isbn", String.class)) {
                isbnPrestados.add(isbn);
            }
            cursor = col_libros.find(Filters.in("isbn", isbnPrestados)).iterator();
            while (cursor.hasNext()) {
                System.out.println("Ej22 - libro con préstamo: " + cursor.next().get("titulo"));
            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mongoCliente.close();
        }
    }

    // ======================================================
    // Método auxiliar (usado en ejercicios 9 y 18):
    // imprime el nombre de los socios que tienen prestado un libro con ese isbn
    // ======================================================
    static void imprimirSociosPorIsbn(String isbn) {
        MongoCursor<Document> cursorPrestamos = col_Prestamos.find(Filters.eq("libro.isbn", isbn)).iterator();
        try {
            while (cursorPrestamos.hasNext()) {
                // socio es un documento embebido con num_socio (String)
                Document socioPrestamo = (Document) cursorPrestamos.next().get("socio");
                String numSocio = socioPrestamo.getString("num_socio");
                // En Socios, Num_socio se guarda como número
                Document socio = col_socios.find(
                        Filters.eq("Num_socio", Integer.parseInt(numSocio))).first();
                if (socio != null) {
                    System.out.println(" - " + socio.get("Nombre"));
                }
            }
        } finally {
            cursorPrestamos.close();
        }
    }
}
