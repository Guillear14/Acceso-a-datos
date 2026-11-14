package pojos;

// ============================================================
// POJO "Evento": una ficha que representa UN evento del fichero Eventos.json.
// Sus atributos tienen el MISMO nombre que las claves del JSON,
// así Gson sabe qué dato va en cada sitio automáticamente.
// ============================================================
public class Evento {

    private String nombre;   // ej: "Concierto de verano"
    private String fecha;    // ej: "2026-07-14" (lo guardamos como texto)
    private String tipo;     // ej: "música", "teatro"...
    private int aforo;       // cuántas personas caben (número entero)

    // Getters: funciones para leer cada dato de la ficha.
    public String getNombre() { return nombre; }
    public String getFecha()  { return fecha; }
    public String getTipo()   { return tipo; }
    public int getAforo()     { return aforo; }

    // toString: cómo se ve la ficha si la imprimimos.
    @Override
    public String toString() {
        return "Evento [nombre=" + nombre + ", fecha=" + fecha + ", tipo=" + tipo + ", aforo=" + aforo + "]";
    }
}
