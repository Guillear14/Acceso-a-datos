package ejemplo;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import com.google.gson.Gson;

// Ejercicio 2: crear una lista de objetos Java, convertirla a JSON con Gson
// y guardarla en un fichero.
public class eje2_gson {
	public static void main(String[] args) {
		// Creamos una lista fija con dos jugadores.
		// Arrays.asList convierte los argumentos en una List de solo lectura.
		List <FootballPlayer> jugadores = Arrays.asList(new FootballPlayer(13,"pedri", Arrays.asList("extremo derecho", "media punta"),"barcelona"),new FootballPlayer(19,"Angel", Arrays.asList("delantero centro","delantero centro"),"Real Madrid"));
		// Gson convierte la lista en un texto JSON (serialización).
		String json = new Gson().toJson(jugadores);
		// Mostramos el JSON por consola para comprobarlo.
		System.out.println(json);
		// Escribimos el JSON en un fichero. La carpeta "ficheros" debe existir.
		// FileWriter puede lanzar IOException, por eso va dentro de try/catch.
		try {
			FileWriter file = new FileWriter("ficheros/footballjson.json");
			file.write(json);
			// flush fuerza a escribir el contenido del buffer en disco.
			// Nota: el fichero no se cierra; lo ideal sería usar try-with-resources o file.close().
			file.flush();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
