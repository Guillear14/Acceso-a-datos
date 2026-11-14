package ejemplo_test;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;

// Ejercicio 4: mostrar nombre y stock de los productos del "Almacén 1" con precio menor de 150.
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
				// "ubicacion" es un objeto anidado dentro del producto.
				ubicacion = (JSONObject) prod.get("ubicacion");
				almacen = (String) ubicacion.get("almacen");
				// El cast a double funciona solo si el JSON guarda el precio con decimales.
				// Si viene como entero, daría ClassCastException.
				precio = (double) prod.get("precio");
				
				// Condición compuesta: almacén concreto Y precio por debajo de 150.
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
