import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
public class Biblioteca_insert_update {
	public static void main(String[] args) {
		Document autor = null;
		String cad = null;
		MongoCursor <Document> cursor_libros = null;
		MongoCursor <Document> cursor_prestamos = null;
		MongoCursor <Document> cursor_socios = null;
		Document libro = null;
		MongoClient mongoCliente =MongoClients.create();
		MongoDatabase baseDatos = mongoCliente.getDatabase("biblioteca");
		MongoCollection col_libros = baseDatos.getCollection("Libros");
		MongoCollection col_Prestamos = baseDatos.getCollection("Prestamos");
		MongoCollection col_socios = baseDatos.getCollection("Socios");
		String titulo = "el dia de la trilla";
		String nombre = "rebeca";
		String apellido = "yarros";
		String nacionalidad = "Americana";
		String editorial = "planeta";
		int año_publicacion = 2026;
		int páginas = 900;
		List<String> género = new ArrayList();
		género.add("aventura");
		género.add("fantasia");
		género.add("juvenil");
		try {
			cursor_libros = col_libros.find().iterator();
			while(cursor_libros.hasNext()) {
				System.out.println(cursor_libros.next().get("titulo"));
			}
			libro = (Document) col_libros.find().first();
			System.out.println(libro.get("isbn"));
			autor = (Document) libro.get("autor");
			System.out.println(autor.get("nombre"));
			libro = new Document("isbn", "12345").append("titulo", titulo).append("autor", new Document("nombre", nombre).append("apellido", apellido)).append("genero", género).append("año_publicacion", año_publicacion).append("paginas", páginas);
			col_libros.insertOne(libro);
			Bson bsonfilter = Filters.gte("año_publicacion", 2020);
			cursor_libros = col_libros.find(bsonfilter).iterator();
			while(cursor_libros.hasNext()) {
				System.out.println("Titulo de libros publicados >2000 "+cursor_libros.next().get("titulo"));
			}
			bsonfilter = Filters.and(Filters.gte("año_publicacion", 2000), Filters.lte("año_publicacion",2020));
			cursor_libros = col_libros.find(bsonfilter).iterator();
			while(cursor_libros.hasNext()) {
				System.out.println("Titulo de libros publicados entre 2000 y 2020 "+cursor_libros.next().get("año_publicacion"));
			}
			bsonfilter = Filters.size("libro",2);
			cursor_prestamos = col_Prestamos.find(bsonfilter).iterator();
			while(cursor_prestamos.hasNext()) {
				System.out.println("prestamos con 2 libros"+cursor_prestamos.next().toJson());
			}
			bsonfilter = Filters.eq("autor.apellido","Follet");
			cursor_libros = col_libros.find(bsonfilter).iterator();
			while(cursor_libros.hasNext()) {
				System.out.println("titulos de los libros de Follet "+cursor_libros.next().get("titulo"));
			}
			bsonfilter = Filters.eq("libro.isbn", "446854");
			cursor_prestamos = col_Prestamos.find(bsonfilter).iterator();
			while(cursor_prestamos.hasNext()) {
				System.out.println("prestamos con 2 libros"+cursor_prestamos.next().toJson());
			}
			if(cursor_prestamos!=null){
				bsonfilter = Filters.eq("isbn","446854");
				cursor_libros = col_libros.find(bsonfilter).iterator();
				while(cursor_libros.hasNext()) {
					System.out.println("titulos de los libros con isbn 446854: "+cursor_libros.next().get("titulo"));
				}
			}
			//ejercicio 18
			bsonfilter = Filters.eq("titulo", "El Quijote");
			cursor_libros = col_libros.find(bsonfilter).iterator();
			while(cursor_libros.hasNext()) {
			bsonfilter = Filters.eq("libro.isbn", cad);
			cursor_prestamos = col_Prestamos.find(bsonfilter).iterator();
			while (cursor_prestamos.hasNext()) {
				Document socio = (Document) cursor_prestamos.next().get("socio");
				cad = (String) socio.get("num_socio");
				Integer numero = Integer.parseInt(cad);
				bsonfilter = Filters.eq("Num_socio", numero);
				socio = (Document) col_socios.find(bsonfilter).first();
				System.out.println("nombre del libro quijote "+socio.get("Nombre"));
			}
			}
			//ejercicio 17
			bsonfilter = Filters.eq("autor.apellido", "Asimov");
			cursor_libros = col_libros.find(bsonfilter).sort(Sorts.ascending("titulo")).iterator();
			
			while(cursor_libros.hasNext()) {
				System.out.println("lista ordenada "+cursor_libros.next().toJson());
			}
			//proyección
			bsonfilter = Filters.eq("autor.apellido", "Asimov");
			cursor_libros = col_libros.find(bsonfilter).projection(Projections.fields(Projections.include("titulo","año_publicacion"),Projections.excludeId())).iterator();
			while(cursor_libros.hasNext()) {
				System.out.println("lista ordenada "+cursor_libros.next().toJson());
			}
			cursor_libros = col_libros.find(bsonfilter).sort(Sorts.ascending("titulo")).projection(Projections.include("titulo","año_publicacion")).iterator();
			while(cursor_libros.hasNext()) {
				System.out.println("lista ordenada "+cursor_libros.next().toJson());
			}
			//ejercicio 19
			bsonfilter = Filters.eq("Nombre", "Javier Garcia");
			cursor_socios = (MongoCursor<Document>) col_socios.find(bsonfilter).projection(Projections.fields(Projections.include("Num_socio"), Projections.excludeId())).iterator();
			cad = (String) String.valueOf(cursor_socios.next().get("Num_socio"));
			bsonfilter = Filters.eq("socio.Num_socio", cad);
			cursor_prestamos = (MongoCursor<Document>) col_Prestamos.find(bsonfilter).iterator();
			List<Document> lista = new ArrayList();
			while(cursor_prestamos.hasNext()) {
			lista = (List<Document>) cursor_prestamos.next().get("libro");
			for(Document lib_1:lista) {
				cad =(String) lib_1.getString(lib_1);
				bsonfilter = Filters.eq("isbn", cad);
				cursor_libros = (MongoCursor<Document>) col_libros.find(bsonfilter);
				System.out.println(cursor_libros);
			}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}