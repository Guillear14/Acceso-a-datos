package pojos;

import com.google.gson.annotations.SerializedName;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.ArrayList;
import java.util.List;

/*
 * QUÉ ES ESTO:
 * Un "POJO" es una clase normal de Java que guarda datos de una persona/cosa.
 * Piensa en ella como una FICHA de jugador en papel: tiene campos (nombre, género, hobbies).
 *
 * ¿POR QUÉ TIENE ANOTACIONES (@...)?
 * Las dos librerías (Gson y MongoDB) tienen sus propias reglas de nombres.
 * El campo en Java se llama "genero" (sin tilde) pero en el JSON y en la base de datos
 * la clave se llama "género" (con tilde). La anotación es como una etiqueta que dice:
 * "En el papel pone 'género', aunque yo lo llame 'genero' en mi código".
 */
public class Jugador {

    // Nombre del jugador. Ejemplo: "Tom".
    private String nombre;

    // Género del jugador. En el JSON la clave se llama "género".
    // @SerializedName es la etiqueta que entiende GSON (la librería de JSON de Google).
    @SerializedName("género")
    private String genero;

    // Lista de aficiones. Ejemplo: ["fútbol", "lectura"].
    // Se crea vacía (new ArrayList) para que nunca sea null.
    private List<String> hobby = new ArrayList<String>();

    // Constructor vacío: es como una ficha en blanco.
    // Gson y MongoDB lo usan para crear el objeto y luego rellenarlo campo a campo.
    public Jugador() { }

    // Constructor con datos: crea una ficha ya rellena.
    public Jugador(String nombre, String genero, List<String> hobby) {
        this.nombre = nombre;   // "this.nombre" = el campo de la ficha; "nombre" = el dato que llega
        this.genero = genero;
        this.hobby = hobby;
    }

    // GETTERS: "dame el valor" (leer la ficha).
    public String getNombre() { return nombre; }

    // SETTERS: "pon este valor" (escribir en la ficha).
    public void setNombre(String nombre) { this.nombre = nombre; }

    // @BsonProperty es la etiqueta que entiende MongoDB: en la base de datos se guarda como "género".
    @BsonProperty("género")
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public List<String> getHobby() { return hobby; }
    public void setHobby(List<String> hobby) { this.hobby = hobby; }

    // toString: lo que se ve cuando imprimimos el objeto con System.out.println.
    @Override
    public String toString() {
        return "Jugador [nombre=" + nombre + ", género=" + genero + ", hobby=" + hobby + "]";
    }
}
