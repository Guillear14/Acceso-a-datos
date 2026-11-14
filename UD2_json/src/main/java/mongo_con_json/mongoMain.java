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

// Conecta con MongoDB, busca un socio por nombre y convierte el documento en un objeto Socio usando Gson.
public class mongoMain {
	public static void main(String[] args) {
		// Creamos el cliente conectado al servidor local por defecto (localhost:27017).
		MongoClient mongoCliente =MongoClients.create();
		// Seleccionamos la base de datos "biblioteca" y la colección "Socios".
		MongoDatabase baseDatos = mongoCliente.getDatabase("biblioteca");
		MongoCollection col_socios = baseDatos.getCollection("Socios");
		Gson gson = new Gson();
		// Filtro de búsqueda (se asigna más abajo).
		Bson filtro = null;
		Document docPersona = null;
		Socio socio = null;
		String persona = null;
		
		try {
			// Filtro: documentos cuyo campo "Nombre" sea "Javier Garcia".
			filtro = Filters.eq("Nombre","Javier Garcia");
			// Buscamos el primer documento que cumpla el filtro.
			docPersona =(Document) col_socios.find(filtro).first();
			// Convertimos el documento de MongoDB a texto JSON.
			persona = docPersona.toJson();
			System.out.println(persona);
			// Gson convierte ese JSON en un objeto Socio.
			// Nota: el campo "Nombre" del documento debe coincidir con el atributo de Socio para que Gson lo rellene.
			socio = gson.fromJson(persona, Socio.class);
			System.out.println("numero de socio: "+socio.getNum_socio());
		}catch(Exception e) {
			// Captura cualquier error (conexión, documento no encontrado con NullPointerException, etc.).
			e.printStackTrace();
		}
	}
}
