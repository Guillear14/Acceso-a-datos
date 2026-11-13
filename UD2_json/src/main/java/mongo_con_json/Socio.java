package mongo_con_json;

public class Socio {
	private String nombre;
	private int Num_socio;
	public Socio() {
		super();
	}
	public Socio(String nombre, int num_socio) {
		super();
		this.nombre = nombre;
		this.Num_socio = Num_socio;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getNum_socio() {
		return Num_socio;
	}
	public void setNum_socio(int num_socio) {
		this.Num_socio = Num_socio;
	}
	
	@Override
	public String toString() {
		return "Socio [nombre=" + nombre + ", num_socio=" + Num_socio + "]";
	}
}
