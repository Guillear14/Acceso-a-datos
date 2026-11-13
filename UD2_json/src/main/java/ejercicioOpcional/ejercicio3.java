package ejercicioOpcional;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ejercicio3 {
	public static void main(String[] args) {
		JSONParser parse = new JSONParser();
		JSONObject coci, ingre;
		String tipo;
		try {
			JSONArray array = (JSONArray) parse.parse(new FileReader("ficheros/cocina.json"));
			for(Object cociOBJ: array) {
				coci=(JSONObject) cociOBJ;
				tipo = (String) coci.get("tipo");
				if(tipo.equals("Primero")) {
					System.out.println("el plato de primero es: "+coci.get("nombre"));
					JSONArray ingredientes = (JSONArray) coci.get("ingredientes");
					for(Object ingreObj:ingredientes) {
						ingre = (JSONObject) ingreObj;
						System.out.println("y los ingredientes son: "+ingre.get("nombre"));
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
