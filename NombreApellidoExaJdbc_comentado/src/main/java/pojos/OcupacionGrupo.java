package pojos;

// ============================================================
// POJO "OcupacionGrupo": guarda el RESULTADO de la consulta de ocupación.
// No es una tabla de la base de datos: es una "ficha de resumen" que nos construimos
// con los datos que devuelve el SQL: en qué grupo, qué día y cuántos alumnos hay.
// ============================================================
public class OcupacionGrupo {

    private String codigoGrupo;   // por ejemplo "G1"
    private String dia;           // por ejemplo "Jueves"
    private int totalMatriculas;  // cuántos alumnos están apuntados a ese grupo

    // Constructor: crea el resumen con los tres datos.
    public OcupacionGrupo(String codigoGrupo, String dia, int totalMatriculas) {
        this.codigoGrupo = codigoGrupo;
        this.dia = dia;
        this.totalMatriculas = totalMatriculas;
    }

    // Getters: leer cada dato.
    public String getCodigoGrupo()  { return codigoGrupo; }
    public String getDia()          { return dia; }
    public int getTotalMatriculas() { return totalMatriculas; }
}
