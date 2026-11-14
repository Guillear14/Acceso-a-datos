package ejercicioOpcional;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// Ejercicio opcional 2: mostrar los platos que llevan "Tomate" como ingrediente.
public class ejercicio2 {
	public static void main(String[] args) {
		JSONParser parse = new JSONParser();
		JSONObject coci, nombre;
		String ingre;
		try {
			JSONArray array = (JSONArray) parse.parse(new FileReader("ficheros/cocina.json"));
			for(Object cociOBJ: array) {
				coci=(JSONObject) cociOBJ;
				// Lista de ingredientes del plato actual (cada uno es un objeto con "nombre").
				JSONArray ingredientes = (JSONArray) coci.get("ingredientes");
				for(Object ingreObj:ingredientes) {
					nombre = (JSONObject) ingreObj;
					ingre = (String) nombre.get("nombre");
					// Si encontramos "Tomate", mostramos el plato y dejamos de buscar en ese plato.
					// Nota: el bucle sigue recorriendo los ingredientes; un 'break' evitaría repetir.
					if(ingre.equals("Tomate")) {
						System.out.println("los platos con totate son: "+coci.get("nombre"));
					}
				}
			}
			}catch(FileNotFoundException e) {
				e.printStackTrace();
			}catch(IOException e) {
				e.printStackTrace();
			}catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace();
			}
	}

}
