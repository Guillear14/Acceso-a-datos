package ejemplo_test;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;

public class ejercicio3 {
	public static void main(String[] args) {
		JSONParser parse = new JSONParser();
		JSONObject prod;
		
		try {
			JSONArray array = (JSONArray) parse.parse(new FileReader("ficheros/productos.json"));
			
			for(Object prodObj: array) {
				prod = (JSONObject) prodObj;
				if (prod.get("nombre_producto").equals("Pantalones Levi's 501")) {
					System.out.println(prod);
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
