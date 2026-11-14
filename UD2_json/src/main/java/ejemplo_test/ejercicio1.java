package ejemplo_test;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// Ejercicio 1: mostrar el nombre de todos los productos de productos.json.
public class ejercicio1 {
	public static void main(String[] args) {
		JSONParser parse = new JSONParser();
		JSONObject prod;
		try {
			// El fichero es un array JSON de productos ([ {...}, {...} ]).
			JSONArray array = (JSONArray) parse.parse(new FileReader("ficheros/productos.json"));
			// Recorremos cada producto. Como el array contiene Object, hay que castear a JSONObject.
			for(Object prodObj: array) {
				prod=(JSONObject) prodObj;
				// Mostramos el valor de la clave "nombre_producto".
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
