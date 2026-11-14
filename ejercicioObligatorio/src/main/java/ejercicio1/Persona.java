package ejercicio1;

import com.google.gson.annotations.SerializedName;

/**
 * POJO que representa una persona de la colección "Personas" de MongoDB.
 * Solo contiene los atributos de la persona, sin los datos que la relacionan con un coche.
 */
public class Persona {

    // @SerializedName indica el nombre que tiene el campo en el JSON/MongoDB,
    // porque en Java usamos camelCase (idPersona) y en la base de datos es "id_persona".
    @SerializedName("id_persona")
    private int idPersona;

    private String nombre;

    private String apellidos;

    // En la BD el campo se llama "City" (con mayúscula), por eso se mapea aquí.
    @SerializedName("City")
    private String city;

    // Constructor vacío: Gson lo necesita para crear el objeto al convertir el JSON.
    public Persona() {
    }

    // Getters y setters: permiten leer y modificar los atributos desde fuera de la clase.
    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    // Se sobrescribe toString() para que System.out.println muestre el contenido del objeto.
    @Override
    public String toString() {
        return "Persona [idPersona=" + idPersona + ", nombre=" + nombre
                + ", apellidos=" + apellidos + ", city=" + city + "]";
    }
}