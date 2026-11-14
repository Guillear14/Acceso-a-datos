package ejemplo_test;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;

// Ejercicio 2: mostrar nombre y precio de los productos de tipo "Ropa".
public class ejercicio2 {

	public static void main(String[] args) {
		JSONParser parse = new JSONParser();
		JSONObject prod;
		
		try {
			JSONArray array = (JSONArray) parse.parse(new FileReader("ficheros/productos.json"));
			
			for(Object prodObj: array) {
				prod = (JSONObject) prodObj;
				// Filtramos: solo los productos cuyo tipo sea "Ropa".
				if (prod.get("tipo_producto").equals("Ropa")) {
					System.out.println("nombre Producto: " + prod.get("nombre_producto")+" y el precio es: "+prod.get("precio"));
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
