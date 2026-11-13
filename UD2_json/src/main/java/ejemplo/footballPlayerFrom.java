package ejemplo;

import java.io.*;

import com.google.gson.Gson;

public class footballPlayerFrom {

	public static void main(String[] args) {
		File archivo = null;
		FileReader reader = null;
		BufferedReader buffer = null;
		String linea, jsonAll = new String();
		FootballPlayer[] footballPlayers;
		try {
			reader = new FileReader("ficheros/FootballPlay.json");
			buffer = new BufferedReader(reader);
			
			while((linea = buffer.readLine()) != null) {
				jsonAll = jsonAll+linea;
			}
			
			Gson gson = new Gson();
			
			footballPlayers = gson.fromJson(jsonAll, FootballPlayer[].class);
			 
			for(FootballPlayer footballplayer: footballPlayers) {
				System.out.println("jugador"+footballplayer.toString()+" nombre: "+footballplayer.getName());
			}
		
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

}
