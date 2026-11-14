package ejemplo;
import java.util.List;

// Clase modelo (POJO) que representa a un jugador de fútbol.
// Gson la usa para convertir entre objetos Java y JSON: los nombres de los
// atributos deben coincidir con las claves del JSON.
public class FootballPlayer {
	// Número de dorsal del jugador.
	private int dorsal;
	// Nombre del jugador. Gson lo buscará en el JSON con la clave "name".
	private String name;
	// Lista de posiciones en el campo (p. ej. "extremo derecho", "media punta").
	private List<String> demarcation;
	// Equipo al que pertenece el jugador.
	private String team;
	
	
	// Constructor vacío: Gson lo necesita para crear objetos al leer JSON.
	public FootballPlayer() {
		super();
	}

	// Constructor con todos los datos.
	// AVISO: hay un error en la asignación de 'name'. Se hace this.name = name,
	// que asigna el campo a sí mismo; el parámetro se llama 'nombre', así que
	// debería ser this.name = nombre. Lo dejo tal cual para no modificar el código.
	public FootballPlayer(int dorsal, String nombre, List<String> demarcation, String team) {
		super();
		this.dorsal = dorsal;
		this.name = name;
		this.demarcation = demarcation;
		this.team = team;
	}

	
	// Getters y setters: permiten leer y modificar los atributos desde fuera.
	// Gson también los puede usar al serializar.
	public int getDorsal() {
		return dorsal;
	}
	public void setDorsal(int dorsal) {
		this.dorsal = dorsal;
	}
	public String getName() {
		return name;
	}
	public void setName(String nombre) {
		this.name = nombre;
	}
	public List<String> getDemarcation() {
		return demarcation;
	}
	public void setDemarcation(List<String> demarcation) {
		this.demarcation = demarcation;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}

	// Devuelve una representación en texto del objeto (se usa al imprimirlo con System.out).
	@Override
	public String toString() {
		return "FootballPlayer [dorsal=" + dorsal + ", nombre=" + name + ", demarcation=" + demarcation + ", team="
				+ team + "]";
	}
	
	
}
