package ejercicioOpcional;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ejercicio4 {
	public static void main(String[] args) {
		JSONParser parse = new JSONParser();
		JSONObject coci;
		String ingre;
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
						String cantidad_String = (String) nombre.get("cantidad");
						String numeroTexto = cantidad_String.split(" ")[0];
						numero = Integer.parseInt(numeroTexto);
						if(numero<numeroAnterior) {
							numeroAnterior = numero;
						}
					}
				}
			}
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
