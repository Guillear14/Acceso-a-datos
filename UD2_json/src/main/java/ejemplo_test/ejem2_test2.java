package ejemplo_test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ejem2_test2 {
	public static void main(String[] args){
		JSONParser parser = new JSONParser();
		String v_msg, v_name;
		JSONArray arrayPg, arrayRs;
		try {
			JSONObject obj =  (JSONObject) parser.parse(new FileReader("ficheros/test2.json"));
			System.out.println(obj.get("id"));
			System.out.println("");
			arrayPg = (JSONArray) obj.get("pages");
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
