package ejemplo_test;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// Ejercicio 8: mostrar los productos que pertenecen a la categoría "Deportes".
// Cada producto tiene un array "categorias", así que hay que recorrer dos niveles.
public class ejercicio8 {
	public static void main(String[] args) {
		JSONParser parse = new JSONParser();
		JSONObject prod;
		try {
			JSONArray array = (JSONArray) parse.parse(new FileReader("ficheros/productos.json"));
			for(Object prodObj: array) {
				prod=(JSONObject) prodObj;
				// Array de categorías del producto actual.
				JSONArray categorias = (JSONArray) prod.get("categorias");
				for(Object cat: categorias) {
					// Si alguna categoría es "Deportes", mostramos el producto.
					// Nota: si el producto tiene "Deportes" varias veces, se imprimirá varias veces.
					if(cat.equals("Deportes")) {
						System.out.println("los productos de Deportes son: "+prod.get("nombre_producto"));
					}
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
