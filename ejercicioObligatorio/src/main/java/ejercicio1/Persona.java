package ejercicio1;

// POJO: ficha de una persona. Solo tiene los datos de la persona,
// no los datos que la relacionan con un coche.
// Los nombres de los campos deben ser IGUALES a las claves del JSON,
// porque Gson los usa para rellenar el objeto.
public class Persona {

    private String nombre;     // "nombre" en MongoDB
    private String apellidos;  // "apellidos" en MongoDB
    private int id_persona;    // "id_persona" en MongoDB
    private String City;       // "City" en MongoDB (con mayúscula, igual que en la base de datos)

    // Constructor vacío: Gson lo necesita para crear la persona antes de rellenarla.
    public Persona() { }

    // Constructor con todos los datos: crea una persona ya rellena.
    public Persona(String nombre, String apellidos, int id_persona, String City) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.id_persona = id_persona;
        this.City = City;
    }

    // GETTERS: leer los datos de la persona.
    public String getNombre()     { return nombre; }
    public String getApellidos()  { return apellidos; }
    public int getIdPersona()     { return id_persona; }
    public String getCity()       { return City; }

    // SETTERS: añadir o cambiar los datos de la persona.
    public void setNombre(String nombre)       { this.nombre = nombre; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public void setIdPersona(int id_persona)   { this.id_persona = id_persona; }
    public void setCity(String City)           { this.City = City; }

    // toString: así se ve la persona al imprimirla con System.out.println.
    @Override
    public String toString() {
        return "Persona [nombre=" + nombre + ", apellidos=" + apellidos
                + ", id_persona=" + id_persona + ", City=" + City + "]";
    }
}
