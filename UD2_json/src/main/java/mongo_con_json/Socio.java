package mongo_con_json;

// Clase modelo de un socio. Gson la usa para convertir el JSON que viene de MongoDB en un objeto Java.
public class Socio {
	// Nombre del socio.
	private String nombre;
	// Número de socio.
	private int Num_socio;
	// Constructor vacío: necesario para que Gson pueda crear el objeto.
	public Socio() {
		super();
	}
	// Constructor con datos.
	// AVISO: hay un error en la asignación de Num_socio. Se hace this.Num_socio = Num_socio,
	// que asigna el campo a sí mismo; debería ser this.Num_socio = num_socio.
	// Lo dejo tal cual para no modificar el código.
	public Socio(String nombre, int num_socio) {
		super();
		this.nombre = nombre;
		this.Num_socio = Num_socio;
	}
	
	// Getters y setters.
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getNum_socio() {
		return Num_socio;
	}
	// Mismo problema que en el constructor: asigna el campo a sí mismo en vez de usar num_socio.
	public void setNum_socio(int num_socio) {
		this.Num_socio = Num_socio;
	}
	
	// Representación en texto del socio.
	@Override
	public String toString() {
		return "Socio [nombre=" + nombre + ", num_socio=" + Num_socio + "]";
	}
}
