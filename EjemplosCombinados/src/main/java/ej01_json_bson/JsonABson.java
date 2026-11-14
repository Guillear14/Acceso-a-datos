package ej01_json_bson;

import org.bson.BsonBinaryWriter;
import org.bson.Document;
import org.bson.RawBsonDocument;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;
import org.bson.io.BasicOutputBuffer;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;

/*
 * EJEMPLO 1: JSON (texto) <-> Document (BSON en memoria) <-> bytes BSON
 *
 * IDEA GENERAL EN UNA FRASE:
 * JSON es texto que lee cualquier persona. BSON es el mismo contenido pero en "bits",
 * que MongoDB guarda y lee más rápido. Este ejemplo convierte de un formato al otro.
 *
 * NO NECESITA MONGODB ENCENDIDO. Todo pasa dentro de la memoria del programa.
 */
public class JsonABson {

    public static void main(String[] args) {

        // ---------- PASO 1: de TEXTO JSON a un objeto Document ----------
        // Esto es una frase escrita en JSON. Las comillas \" son necesarias porque estamos dentro de un String de Java.
        String json = "{\"nombre\":\"MongoDB\",\"tipus\":\"base de dades\",\"conte\":1,"
                + "\"versions\":[\"v3.2\",\"v3.0\"],\"info\":{\"x\":203,\"i\":102}}";

        // Document.parse LEE el texto y construye un objeto Document (como un diccionario en memoria).
        Document doc = Document.parse(json);

        // getClass().getName() nos dice de qué tipo Java es cada campo.
        System.out.println("versions -> " + doc.get("versions").getClass().getName()); // java.util.List   (es una lista)
        System.out.println("info     -> " + doc.get("info").getClass().getName());     // Document       (es un objeto dentro de otro)

        // ---------- PASO 2: "JSON con superpoderes" (extended JSON) ----------
        // En JSON normal no existe "un identificador de MongoDB" ni "una fecha de verdad".
        // MongoDB lo escribe con etiquetas especiales: {"$oid": ...} y {"$date": ...}.
        String extendido = "{\"_id\":{\"$oid\":\"5c2a42221fe649ebdceacc05\"},"
                + "\"creat\":{\"$date\":\"2018-12-31T16:21:54Z\"}}";
        Document conTipos = Document.parse(extendido);

        // Comprobamos que Java ha entendido las etiquetas y ha creado los tipos correctos.
        System.out.println("_id   -> " + conTipos.get("_id").getClass().getName());   // org.bson.types.ObjectId
        System.out.println("creat -> " + conTipos.get("creat").getClass().getName()); // java.util.Date

        // ---------- PASO 3: de Document a TEXTO JSON, de tres maneras ----------
        // Es como escribir la misma carta en tres estilos distintos:
        //  STRICT  -> formal y completo, lo entiende cualquier programa.
        //  RELAXED -> más fácil de leer para una persona.
        //  SHELL   -> como lo escribirías tú en la consola de MongoDB.
        System.out.println("\nSTRICT:\n"  + conTipos.toJson(JsonWriterSettings.builder().outputMode(JsonMode.STRICT).build()));
        System.out.println("RELAXED:\n" + conTipos.toJson(JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build()));
        System.out.println("SHELL:\n"   + conTipos.toJson(JsonWriterSettings.builder().outputMode(JsonMode.SHELL).build()));

        // ---------- PASO 4: de Document a BYTES BSON (lo que MongoDB guarda en disco) ----------
        // Un "escritor binario" anota el Document en una hoja de bits (BasicOutputBuffer).
        BasicOutputBuffer buffer = new BasicOutputBuffer();
        new DocumentCodec().encode(new BsonBinaryWriter(buffer), doc, EncoderContext.builder().build());
        byte[] bytes = buffer.toByteArray(); // la hoja convertida en una lista de bytes (números 0-255)

        // Comparamos tamaños: suele salir similar, pero el BSON puede ser algo más grande o más pequeño.
        System.out.println("\nTamaño JSON: " + json.length() + " caracteres | BSON: " + bytes.length + " bytes");

        // ---------- PASO 5: de BYTES BSON de vuelta a Document ----------
        // RawBsonDocument coge los bytes y DocumentCodec los vuelve a convertir en un Document.
        Document recuperado = new RawBsonDocument(bytes).decode(new DocumentCodec());
        System.out.println("Recuperado desde BSON: " + recuperado.toJson());
    }
}
