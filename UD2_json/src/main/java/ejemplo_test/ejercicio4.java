package ejemplo_test;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;

public class ejercicio4 {
	public static void main(String[] args) {
		JSONParser parse = new JSONParser();
		JSONObject prod,ubicacion;
		double precio;
		String almacen;
		try {
			JSONArray array = (JSONArray) parse.parse(new FileReader("ficheros/productos.json"));
			for(Object prodObj: array) {
				prod=(JSONObject) prodObj;
				ubicacion = (JSONObject) prod.get("ubicacion");
				almacen = (String) ubicacion.get("almacen");
				precio = (double) prod.get("precio");
				
				if(almacen.equals("Almacén 1") && precio<150) {
					System.out.println("nombre producto: "+prod.get("nombre_producto")+" "+prod.get("stock"));
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
