package ejercicioOpcional;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// Ejercicio opcional 1: mostrar los ingredientes del plato "Gazpacho" (leyendo cocina.json).
public class ejercicio1 {
	public static void main(String[] args) {
		JSONParser parse = new JSONParser();
		JSONObject coci;
		String temp;
		try {
			JSONArray array = (JSONArray) parse.parse(new FileReader("ficheros/cocina.json"));
			for(Object cociOBJ: array) {
				coci=(JSONObject) cociOBJ;
				// Nombre del plato.
				temp = (String) coci.get("nombre");
				// Si es el Gazpacho, mostramos su lista de ingredientes completa.
				if(temp.equals("Gazpacho")) {
					System.out.println("nombre: "+coci.get("ingredientes"));
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
