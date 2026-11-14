package ejemplo;

import java.io.*;

import com.google.gson.Gson;

// Lee un fichero JSON con jugadores y lo convierte en un array de objetos FootballPlayer.
public class footballPlayerFrom {

	public static void main(String[] args) {
		File archivo = null;
		FileReader reader = null;
		BufferedReader buffer = null;
		// jsonAll acumulará todo el contenido del fichero en un único String.
		String linea, jsonAll = new String();
		FootballPlayer[] footballPlayers;
		try {
			// Abrimos el fichero para lectura.
			reader = new FileReader("ficheros/FootballPlay.json");
			buffer = new BufferedReader(reader);
			
			// Leemos el fichero línea a línea y las concatenamos.
			// readLine devuelve null al llegar al final del fichero.
			while((linea = buffer.readLine()) != null) {
				jsonAll = jsonAll+linea;
			}
			
			// Gson convierte el texto JSON en un array de FootballPlayer (deserialización).
			Gson gson = new Gson();
			footballPlayers = gson.fromJson(jsonAll, FootballPlayer[].class);
			 
			// Recorremos el array e imprimimos cada jugador.
			for(FootballPlayer footballplayer: footballPlayers) {
				System.out.println("jugador"+footballplayer.toString()+" nombre: "+footballplayer.getName());
			}
		
		}catch(IOException e) {
			// Si el fichero no existe o no se puede leer, mostramos el error.
			e.printStackTrace();
		}
		// Nota: reader y buffer no se cierran. Lo correcto sería cerrarlos en un finally.
	}

}
