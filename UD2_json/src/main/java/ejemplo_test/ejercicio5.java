package ejemplo_test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ejercicio5 {

	public static void main(String[] args) {
		JSONParser parse = new JSONParser();
		JSONObject prod,ubicacion;
		Long stock;
		String almacen;
		try {
			JSONArray array = (JSONArray) parse.parse(new FileReader("ficheros/productos.json"));
			
			for(Object prodObj: array) {
				prod=(JSONObject) prodObj;
				ubicacion = (JSONObject) prod.get("ubicacion");
				almacen = (String) ubicacion.get("almacen");
				stock = (Long) prod.get("stock");
				if( stock<=50) {
					System.out.println("nombre producto: "+prod.get("nombre_producto")+" "+almacen);
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
