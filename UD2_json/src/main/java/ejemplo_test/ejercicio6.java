package ejemplo_test;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// Ejercicio 6: mostrar nombre y categoría de los productos situados en "Almacén 3", "Pasillo 3".
public class ejercicio6 {
	public static void main(String[] args) {
		JSONParser parse = new JSONParser();
		JSONObject prod,ubicacion;
		String almacen,pasillo;
		try {
			JSONArray array = (JSONArray) parse.parse(new FileReader("ficheros/productos.json"));
			
			for(Object prodObj: array) {
				prod=(JSONObject) prodObj;
				// La ubicación es un objeto anidado con "almacen" y "pasillo".
				ubicacion = (JSONObject) prod.get("ubicacion");
				almacen = (String) ubicacion.get("almacen");
				pasillo = (String) ubicacion.get("pasillo");
				// Ambas condiciones deben cumplirse.
				if(almacen.equals("Almacén 3") && pasillo.equals("Pasillo 3")) {
					System.out.println("el nombre del productoes: "+prod.get("nombre_producto")+" y su categoria es: "+prod.get("tipo_producto"));
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
