package ejemplo_test;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ejercicio1 {
	public static void main(String[] args) {
		JSONParser parse = new JSONParser();
		JSONObject prod;
		try {
			JSONArray array = (JSONArray) parse.parse(new FileReader("ficheros/productos.json"));
			for(Object prodObj: array) {
				prod=(JSONObject) prodObj;
				System.out.println("nombre de los productos: "+prod.get("nombre_producto"));
				}
			}
			catch(FileNotFoundException e) {
				e.printStackTrace();
			}catch(IOException e) {
				e.printStackTrace();
			}catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace();
			}
	}

}
