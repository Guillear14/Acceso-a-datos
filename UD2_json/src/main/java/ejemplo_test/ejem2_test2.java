package ejemplo_test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// Ejemplo 2: lee un JSON con "id" y un array "pages".
// Nota: las variables v_msg, v_name y arrayRs están declaradas pero no se usan.
public class ejem2_test2 {
	public static void main(String[] args){
		JSONParser parser = new JSONParser();
		String v_msg, v_name;
		JSONArray arrayPg, arrayRs;
		try {
			// Leemos el fichero y lo convertimos en un objeto JSON.
			JSONObject obj =  (JSONObject) parser.parse(new FileReader("ficheros/test2.json"));
			// Mostramos el valor de "id" tal cual viene (sin cast).
			System.out.println(obj.get("id"));
			// Línea en blanco para separar la salida.
			System.out.println("");
			// Guardamos el array "pages" aunque aún no lo recorremos.
			arrayPg = (JSONArray) obj.get("pages");
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
	}
}
