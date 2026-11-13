import java.util.List;

public class FootballPlayer {
	private int dorsal;
	private String nombre;
	private List<String> demarcation;
	private String team;
	
	
	public FootballPlayer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FootballPlayer(int dorsal, String nombre, List<String> demarcation, String team) {
		super();
		this.dorsal = dorsal;
		this.nombre = nombre;
		this.demarcation = demarcation;
		this.team = team;
	}

	
	public int getDorsal() {
		return dorsal;
	}
	public void setDorsal(int dorsal) {
		this.dorsal = dorsal;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
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
	
	
}
