import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;

// Ejercicios UD1 MongoDB - Biblioteca (versión corregida)
public class Biblioteca_insert_update {

    public static void main(String[] args) {

        // Conectar al servidor MongoDB de este ordenador
        MongoClient mongoCliente = MongoClients.create();

        try {
            // Base de datos y colecciones
            MongoDatabase baseDatos = mongoCliente.getDatabase("biblioteca");
            MongoCollection<Document> colLibros = baseDatos.getCollection("Libros");
            MongoCollection<Document> colPrestamos = baseDatos.getCollection("Prestamos");
            MongoCollection<Document> colSocios = baseDatos.getCollection("Socios");

            // Variables auxiliares
            Bson bsonFilter;          // aquí guardamos el filtro de cada consulta
            MongoCursor<Document> cursor;
            Document doc;
            Document dLibro;
            String cad;

            // Libro de prueba: se inserta solo si no existe
            bsonFilter = Filters.eq("isbn", "12345");
            if (colLibros.countDocuments(bsonFilter) == 0) {
                List<String> generos = new ArrayList<>();
                generos.add("aventura");
                generos.add("fantasia");
                generos.add("juvenil");

                Document libro = new Document("isbn", "12345")
                        .append("titulo", "el dia de la trilla")
                        .append("autor", new Document("nombre", "rebeca")
                                .append("apellido", "yarros")
                                .append("nacionalidad", "Americana"))
                        .append("editorial", "planeta")
                        .append("genero", generos)
                        .append("año_publicacion", 2026)
                        .append("paginas", 900);

                colLibros.insertOne(libro);
                System.out.println("Libro de prueba insertado.");
            } else {
                System.out.println("El libro de prueba ya existe, no se inserta de nuevo.");
            }

            // EJERCICIO 1
            // Primer libro de la colección.
            Document primerLibro = colLibros.find().first();
            if (primerLibro != null) {
                System.out.println("Ej1 - isbn: " + primerLibro.get("isbn"));
                Document autor = (Document) primerLibro.get("autor");
                System.out.println("Ej1 - nombre del autor: " + autor.get("nombre"));
            }

            // EJERCICIO 2
            // Todos los títulos (sin filtro).
            System.out.println("\nEj2 - todos los títulos:");
            bsonFilter = new Document();
            cursor = colLibros.find(bsonFilter).iterator();
            while (cursor.hasNext()) {
                doc = cursor.next();
                System.out.println("  Ej2 - " + doc.get("titulo"));
            }
            cursor.close();

            // EJERCICIO 3
            // Lista de socios completa.
            System.out.println("\nEj3 - lista de socios:");
            bsonFilter = new Document();
            cursor = colSocios.find(bsonFilter).iterator();
            while (cursor.hasNext()) {
                doc = cursor.next();
                System.out.println("  Ej3 - " + doc.toJson());
            }
            cursor.close();

            // EJERCICIO 4
            // Solo el nombre de cada socio.
            System.out.println("\nEj4 - nombres de socios:");
            bsonFilter = new Document();
            cursor = colSocios.find(bsonFilter).iterator();
            while (cursor.hasNext()) {
                doc = cursor.next();
                System.out.println("  Ej4 - " + doc.get("Nombre"));
            }
            cursor.close();

            // EJERCICIO 5
            // Libro con título "El Quijote".
            System.out.println("\nEj5 - libro El Quijote:");
            bsonFilter = Filters.eq("titulo", "El Quijote");
            cursor = colLibros.find(bsonFilter).iterator();
            while (cursor.hasNext()) {
                doc = cursor.next();
                System.out.println("  Ej5 - " + doc.toJson());
            }
            cursor.close();

            // EJERCICIO 6
            // Préstamos con 2 libros.
            System.out.println("\nEj6 - préstamos con 2 libros:");
            bsonFilter = Filters.size("libro", 2);
            cursor = colPrestamos.find(bsonFilter).iterator();
            while (cursor.hasNext()) {
                doc = cursor.next();
                System.out.println("  Ej6 - " + doc.toJson());
            }
            cursor.close();

            // EJERCICIO 7
            // Títulos de los libros de Follet.
            System.out.println("\nEj7 - libros de Follet:");
            bsonFilter = Filters.eq("autor.apellido", "Follet");
            cursor = colLibros.find(bsonFilter).iterator();
            while (cursor.hasNext()) {
                doc = cursor.next();
                System.out.println("  Ej7 - " + doc.get("titulo"));
            }
            cursor.close();

            // EJERCICIO 8
            // Préstamo del libro con isbn 446854.
            System.out.println("\nEj8 - préstamo del isbn 446854:");
            bsonFilter = Filters.eq("libro.isbn", "446854");
            cursor = colPrestamos.find(bsonFilter).iterator();
            while (cursor.hasNext()) {
                doc = cursor.next();
                System.out.println("  Ej8 - " + doc.toJson());
            }
            cursor.close();

            // EJERCICIO 9
            // Socios que tuvieron el libro 456779.
            // Préstamo -> número de socio -> nombre en Socios.
            System.out.println("\nEj9 - socios que tuvieron el libro 456779:");
            bsonFilter = Filters.eq("libro.isbn", "456779");
            cursor = colPrestamos.find(bsonFilter).iterator();
            while (cursor.hasNext()) {
                doc = (Document) cursor.next().get("socio");
                cad = doc.getString("num_socio");
                int numSocio = Integer.parseInt(cad);

                bsonFilter = Filters.eq("Num_socio", numSocio);
                doc = colSocios.find(bsonFilter).first();
                if (doc != null) {
                    System.out.println("  Ej9 - " + doc.get("Nombre"));
                }
            }
            cursor.close();

            // EJERCICIO 10
            // Año mayor que 2000 Y más de 500 páginas.
            System.out.println("\nEj10 - año > 2000 y más de 500 páginas:");
            bsonFilter = Filters.and(
                    Filters.gt("año_publicacion", 2000),
                    Filters.gt("paginas", 500));
            cursor = colLibros.find(bsonFilter).iterator();
            while (cursor.hasNext()) {
                doc = cursor.next();
                System.out.println("  Ej10 - " + doc.get("titulo"));
            }
            cursor.close();

            // EJERCICIO 11
            // Editados entre 2010 y 2020 (incluidos).
            System.out.println("\nEj11 - editados entre 2010 y 2020:");
            bsonFilter = Filters.and(
                    Filters.gte("año_publicacion", 2010),
                    Filters.lte("año_publicacion", 2020));
            cursor = colLibros.find(bsonFilter).iterator();
            while (cursor.hasNext()) {
                doc = cursor.next();
                System.out.println("  Ej11 - " + doc.get("titulo"));
            }
            cursor.close();

            // EJERCICIO 12
            // Libros de la editorial Debolsillo.
            System.out.println("\nEj12 - editorial Debolsillo:");
            bsonFilter = Filters.eq("editorial", "Debolsillo");
            cursor = colLibros.find(bsonFilter).iterator();
            while (cursor.hasNext()) {
                doc = cursor.next();
                System.out.println("  Ej12 - " + doc.get("titulo"));
            }
            cursor.close();

            // EJERCICIO 13
            // Nombre del socio 2.
            System.out.println("\nEj13 - socio 2:");
            bsonFilter = Filters.eq("Num_socio", 2);
            doc = colSocios.find(bsonFilter).first();
            if (doc != null) {
                System.out.println("  Ej13 - nombre: " + doc.get("Nombre"));
            }

            // EJERCICIO 14
            // Título y nombre del autor de los libros americanos o de la editorial planeta.
            System.out.println("\nEj14 - americanos o editorial planeta (título y nombre autor):");
            bsonFilter = Filters.or(
                    Filters.eq("autor.nacionalidad", "Americana"),
                    Filters.eq("editorial", "planeta"));
            cursor = colLibros.find(bsonFilter)
                    .projection(Projections.fields(
                            Projections.include("titulo", "autor.nombre"),
                            Projections.excludeId())).iterator();
            while (cursor.hasNext()) {
                doc = cursor.next();
                System.out.println("  Ej14 - " + doc.toJson());
            }
            cursor.close();

            // EJERCICIO 15
            // Fecha de préstamo del socio 1 (num_socio es texto en Prestamos).
            System.out.println("\nEj15 - fecha de préstamo del socio 1:");
            bsonFilter = Filters.eq("socio.num_socio", "1");
            cursor = colPrestamos.find(bsonFilter).iterator();
            while (cursor.hasNext()) {
                doc = cursor.next();
                System.out.println("  Ej15 - " + doc.get("fecha_prestamo"));
            }
            cursor.close();

            // EJERCICIO 16
            // Libros editados entre 1990 y 2000.
            System.out.println("\nEj16 - editados entre 1990 y 2000:");
            bsonFilter = Filters.and(
                    Filters.gte("año_publicacion", 1990),
                    Filters.lte("año_publicacion", 2000));
            cursor = colLibros.find(bsonFilter).iterator();
            while (cursor.hasNext()) {
                doc = cursor.next();
                System.out.println("  Ej16 - " + doc.get("titulo"));
            }
            cursor.close();

            // EJERCICIO 17
            // Libros de Asimov, ordenados por título.
            System.out.println("\nEj17 - libros de Asimov (ordenados por título):");
            bsonFilter = Filters.eq("autor.apellido", "Asimov");
            cursor = colLibros.find(bsonFilter)
                    .sort(Sorts.ascending("titulo")).iterator();
            while (cursor.hasNext()) {
                doc = cursor.next();
                System.out.println("  Ej17 - " + doc.get("titulo"));
            }
            cursor.close();

            // EJERCICIO 18 (código del alumno, sin cambios)
            // Mostrar el nombre de los socios que se ha llevado prestado el libro «El Quijote»
            // buscar el libro
            bsonFilter = Filters.eq("titulo", "Los pilares de la tierra");
            dLibro = (Document) colLibros.find(bsonFilter).first();
            cad = dLibro.getString("isbn");
            // Buscar los isbn de "El quijote para buscar en prestamos
            bsonFilter = Filters.eq("libro.isbn", cad);
            doc = (Document) colPrestamos.find(bsonFilter).first();
            System.out.println(" prestamo" + doc.toJson());
            cursor = colPrestamos.find(bsonFilter).iterator();
            while (cursor.hasNext()) {
                doc = (Document) cursor.next().get("socio");
                // buscar el socio en la coleccion socios
                cad = (String) doc.get("num_socio");
                // hago casting de Integer a String del valor numero socio,
                // pq en la colección socio que voy a buscar es de tipo String
                Integer numero = Integer.parseInt(cad);
                bsonFilter = Filters.eq("Num_socio", numero);
                doc = (Document) colSocios.find(bsonFilter).first();
                System.out.println("E18 --Nombre socio: " + doc.get("Nombre"));
            }
            cursor.close();

            /* EJERCICIO 19
               Mostrar el título de los libros que tiene prestados el socio con nombre «Javier García» */
            System.out.println("\nEj19 - libros prestados a Javier García:");
            bsonFilter = Filters.eq("Nombre", "Javier García");
            Document socioJavier = colSocios.find(bsonFilter).first();
            if (socioJavier != null) {
                String numJavier = String.valueOf(socioJavier.get("Num_socio"));
                bsonFilter = Filters.eq("socio.num_socio", numJavier);
                cursor = colPrestamos.find(bsonFilter).iterator();
                while (cursor.hasNext()) {
                    Document prestamo = cursor.next();
                    @SuppressWarnings("unchecked")
                    List<Document> librosDelPrestamo = (List<Document>) prestamo.get("libro");

                    Iterator<Document> itLibros = librosDelPrestamo.iterator();
                    while (itLibros.hasNext()) {
                        Document lib = itLibros.next();
                        bsonFilter = Filters.eq("isbn", lib.getString("isbn"));
                        Document libroEncontrado = colLibros.find(bsonFilter).first();
                        if (libroEncontrado != null) {
                            System.out.println("  Ej19 - " + libroEncontrado.get("titulo"));
                        }
                    }
                }
                cursor.close();
            } else {
                System.out.println("  No existe ningún socio con ese nombre.");
            }

            // EJERCICIO 20
            // Título y autor de los libros de planeta, ordenados por año.
            System.out.println("\nEj20 - libros de planeta (por año):");
            bsonFilter = Filters.eq("editorial", "planeta");
            cursor = colLibros.find(bsonFilter)
                    .sort(Sorts.ascending("año_publicacion"))
                    .projection(Projections.fields(
                            Projections.include("titulo", "autor"),
                            Projections.excludeId())).iterator();
            while (cursor.hasNext()) {
                doc = cursor.next();
                System.out.println("  Ej20 - " + doc.toJson());
            }
            cursor.close();

            /* 21 Mostrar el título y la editorial (proyección) de los libros ordenados por número de
               páginas*/
            cursor = colLibros.find().projection(Projections.fields(Projections.include(
                    "titulo", "editorial", "páginas"),
                    Projections.excludeId())).sort(Sorts.descending("páginas")).iterator();
            while (cursor.hasNext()) {
                System.out.println("EJER21 libro: " + cursor.next().toJson());
            }

            /* 22. Mostrar el título de los libros que tienen algún préstamo */
            cursor = colPrestamos.find().iterator();
            while (cursor.hasNext()) {
                // Obtengo el Array de libros que hay en prestamos
                ArrayList lib = (ArrayList) cursor.next().get("libro");
                // Recorrer el array, obtener los isbn y buscarlo en la colección libros para obtener el título
                for (Object lib_1 : lib) {
                    doc = (Document) lib_1;
                    cad = (String) doc.get("isbn");
                    System.out.println("isbn: " + cad);
                    bsonFilter = Filters.eq("isbn", cad);
                    doc = (Document) colLibros.find(bsonFilter).first();
                    System.out.println("EJER 22: " + doc.toJson());
                }
            }
            cursor.close();

        } catch (Exception e) {
            // Si hay un error, lo mostramos
            e.printStackTrace();
        } finally {
            // Cerrar la conexión al terminar
            mongoCliente.close();
        }
    }
}
