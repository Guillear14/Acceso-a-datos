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

    public String getNombre()     { return nombre; }
    public String getApellidos()  { return apellidos; }
    public int getIdPersona()     { return id_persona; }
    public String getCity()       { return City; }

    // toString: así se ve la persona al imprimirla con System.out.println.
    @Override
    public String toString() {
        return "Persona [nombre=" + nombre + ", apellidos=" + apellidos
                + ", id_persona=" + id_persona + ", City=" + City + "]";
    }
}
