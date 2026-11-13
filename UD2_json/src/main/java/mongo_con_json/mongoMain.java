package mongo_con_json;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class mongoMain {
	public static void main(String[] args) {
		MongoClient mongoCliente =MongoClients.create();
		MongoDatabase baseDatos = mongoCliente.getDatabase("biblioteca");
		MongoCollection col_socios = baseDatos.getCollection("Socios");
		Gson gson = new Gson();
		Bson filtro = null;
		Document docPersona = null;
		Socio socio = null;
		String persona = null;
		
		try {
			filtro = Filters.eq("Nombre","Javier Garcia");
			docPersona =(Document) col_socios.find(filtro).first();
			persona = docPersona.toJson();
			System.out.println(persona);
			socio = gson.fromJson(persona, Socio.class);
			System.out.println("numero de socio: "+socio.getNum_socio());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}