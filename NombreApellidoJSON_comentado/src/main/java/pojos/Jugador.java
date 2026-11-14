package pojos;

import com.google.gson.annotations.SerializedName;
import java.util.List;

// ============================================================
// POJO "Jugador": una ficha con los datos de un jugador (nombre, género y aficiones).
//
// ¿Por qué @SerializedName?
// En el JSON la clave se llama "género" (con tilde), pero en Java no queremos
// usar tildes en los nombres de variables. La anotación le dice a Gson:
// "el campo genero de Java corresponde a la clave género del JSON".
// ============================================================
public class Jugador {

    private String nombre;   // ej: "Tom"

    @SerializedName("género")
    private String genero;   // ej: "masculino"  (en el JSON aparece como "género")

    private List<String> hobby;  // ej: ["fútbol", "lectura", "natación"]

    // Constructor vacío: Gson lo necesita para crear el objeto antes de rellenarlo.
    public Jugador() { }

    // Getters: leer los datos.
    public String getNombre()       { return nombre; }
    public String getGenero()       { return genero; }
    public List<String> getHobby()  { return hobby; }

    // toString: cómo se ve el jugador al imprimirlo.
    @Override
    public String toString() {
        return "Jugador [nombre=" + nombre + ", género=" + genero + ", hobby=" + hobby + "]";
    }
}
