package ejemplo_test;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.*;

// Ejemplo 1: lee un JSON que es un objeto con "name", "age" y "messages".
public class ejem1_test {
	public static void main(String[] args){
		// JSONParser se encarga de convertir el texto del fichero en objetos Java.
		JSONParser parser = new JSONParser();
		JSONArray array;
		try {
			// parse devuelve un Object; lo casteamos a JSONObject porque el fichero empieza por {.
			JSONObject obj =  (JSONObject) parser.parse(new FileReader("ficheros/test.json"));
			// Los valores se leen por clave. El cast indica el tipo esperado:
			// String para texto y Long para números enteros (json-simple usa Long).
			String v_name = (String) obj.get("name");
			Long V_age = (Long) obj.get("age");
			System.out.println(v_name+" "+V_age);
			// "messages" es un array JSON, así que lo leemos como JSONArray.
			array = (JSONArray) obj.get("messages");
			for(Object msg:array) {
				System.out.println("mensaje: "+msg.toString());
			}
		}catch(FileNotFoundException e) {
			// El fichero no existe.
			e.printStackTrace();
		}catch(IOException e) {
			// Error de lectura.
			e.printStackTrace();
		}catch (org.json.simple.parser.ParseException e) {
			// El texto no es JSON válido.
			e.printStackTrace();
		}
		
	}
}
