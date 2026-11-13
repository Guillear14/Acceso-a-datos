package ejemplo_test;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.*;

public class ejem1_test {
	public static void main(String[] args){
		JSONParser parser = new JSONParser();
		JSONArray array;
		try {
			JSONObject obj =  (JSONObject) parser.parse(new FileReader("ficheros/test.json"));
			String v_name = (String) obj.get("name");
			Long V_age = (Long) obj.get("age");
			System.out.println(v_name+" "+V_age);
			array = (JSONArray) obj.get("messages");
			for(Object msg:array) {
				System.out.println("mensaje: "+msg.toString());
			}
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