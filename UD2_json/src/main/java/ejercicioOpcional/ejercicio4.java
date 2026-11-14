package ejercicioOpcional;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// Ejercicio opcional 4: calcular cuántos huevos hacen falta para todos los platos.
// Busca la cantidad mínima de "Huevo" entre los platos (empezando en 30) y multiplica por 3.
public class ejercicio4 {
	public static void main(String[] args) {
		JSONParser parse = new JSONParser();
		JSONObject coci;
		String ingre;
		// numero: valor leído en cada ingrediente. numeroAnterior: mínimo encontrado (valor inicial 30).
		int numero = 0, numeroAnterior = 30;
		try {
			JSONArray array = (JSONArray) parse.parse(new FileReader("ficheros/cocina.json"));
			for(Object cociOBJ: array) {
				coci=(JSONObject) cociOBJ;
				JSONArray ingredientes = (JSONArray) coci.get("ingredientes");
				for(Object ingreObj:ingredientes) {
					JSONObject nombre = (JSONObject) ingreObj;
					ingre = (String) nombre.get("nombre");
					if(ingre.equals("Huevo")) {
						// La cantidad viene como texto, p. ej. "2 unidades".
						String cantidad_String = (String) nombre.get("cantidad");
						// Nos quedamos con la primera palabra (el número) y la convertimos a int.
						String numeroTexto = cantidad_String.split(" ")[0];
						numero = Integer.parseInt(numeroTexto);
						// Guardamos el valor más pequeño encontrado.
						if(numero<numeroAnterior) {
							numeroAnterior = numero;
						}
					}
				}
			}
			// Resultado: el mínimo multiplicado por 3.
			System.out.println("la cantida de huevos es: "+numeroAnterior*3);
			}catch(FileNotFoundException e) {
				e.printStackTrace();
			}catch(IOException e) {
				e.printStackTrace();
			}catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace();
			}
	}

}
