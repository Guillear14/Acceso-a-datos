package ejemplo;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import com.google.gson.Gson;

public class eje2_gson {
	public static void main(String[] args) {
		List <FootballPlayer> jugadores = Arrays.asList(new FootballPlayer(13,"pedri", Arrays.asList("extremo derecho", "media punta"),"barcelona"),new FootballPlayer(19,"Angel", Arrays.asList("delantero centro","delantero centro"),"Real Madrid"));
		String json = new Gson().toJson(jugadores);
		System.out.println(json);
		try {
			FileWriter file = new FileWriter("ficheros/footballjson.json");
			file.write(json);
			file.flush();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}