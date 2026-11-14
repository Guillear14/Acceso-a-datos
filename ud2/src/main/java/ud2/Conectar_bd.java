package ud2;

import java.sql.Statement; // Corregido: antes era java.beans.Statement
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.io.*;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;

// Programa que conecta con una base de datos de ciclismo (MySQL) mediante un DataSource
// configurado en un fichero de propiedades. Pide datos por teclado y realiza varias operaciones:
// insertar, actualizar, consultar, y ejercicios con comprobaciones de reglas de negocio.
public class Conectar_bd {
	public static void main(String[] args){
		// Scanner para leer lo que el usuario escribe por teclado.
		Scanner sc = new Scanner(System.in);
		
		// Objetos JDBC. Se declaran fuera del try para poder cerrarlos en el finally.
		Connection con = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DataSource ds;
		Properties propiedades = new Properties();
		
		// Variables auxiliares para leer y mostrar datos.
		int vid, result, vcount ;
		String vnombre, vnomeq, sql, vequipo;
		try {
			// Cargamos la configuración de la conexión (URL, usuario, contraseña...) desde un fichero.
			// Nota: la ruta usa \ de Windows; en otros sistemas convendría usar / o Paths.
			propiedades.load(new FileInputStream("configuracion\\propiedadesCiclismo"));
			// Creamos el DataSource (pool de conexiones) a partir de las propiedades.
			ds = BasicDataSourceFactory.createDataSource(propiedades);
			con = ds.getConnection();
			// Desactivamos el autocommit: los cambios no se guardan hasta llamar a commit().
			// Así, si algo falla, podemos deshacer (rollback) el conjunto de operaciones.
			con.setAutoCommit(false);
			
			// ---------- Ejemplo 1: insertar un representante ----------
			System.out.println("ejemplo 1 ");
			System.out.println(" nombre directivo");
			vnombre = sc.nextLine();
			
			System.out.println("nombre equipo");
			vnomeq = sc.nextLine();
			
			System.out.println("id");
			vid = sc.nextInt();
			// nextInt deja el salto de línea en el buffer; nextLine lo consume para no leer una línea vacía después.
			sc.nextLine(); // Limpieza del buffer
			
			// PreparedStatement con "?" como marcadores de parámetro: evita inyección SQL.
			sql = "insert into representante values(?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, vid);
			pstmt.setString(2, vnombre);
			pstmt.setString(3, vnomeq);
			pstmt.executeUpdate();
			
			// ---------- Ejemplo 2: actualizar el equipo de un representante ----------
			System.out.println("ejemplo 2 ");
			System.out.println("nombre equipo");
			vnomeq = sc.nextLine();
			
			System.out.println("id");
			vid = sc.nextInt();
			sc.nextLine(); // Limpieza del buffer
			
			sql = "update representante set nom_eq=? where id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, vnomeq);
			pstmt.setInt(2, vid);
			pstmt.executeUpdate();
			
			// ---------- Ejemplo 3: consultar el número de ciclistas de un equipo ----------
			System.out.println("ejemplo 3");
			System.out.println("nombre equipo");
			vnomeq = sc.nextLine();
			
			// TYPE_SCROLL_INSENSITIVE permite usar rs.first() para ir a la primera fila.
			sql = "Select count(*), nomeq from equipo where nomeq=?";
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			pstmt.setString(1, vnomeq);
			rs = pstmt.executeQuery();
			
			// Si el equipo existe (count distinto de 0), mostramos si es numeroso (más de 10) o pequeño.
			if(rs.first() && rs.getInt(1)!=0) {
				vcount = rs.getInt(1);
				vequipo = rs.getString(2);
				if(vcount>10) System.out.println("el equipo es: "+vequipo+" es numeroso"); 
				else System.out.println("el equipo: "+vequipo+" es pequeño");
			}else System.out.println("equipo no existente");
	
			// ---------- Ejemplo 4: número de ciclistas por equipo (todos) ----------
			System.out.println("ejemplo 4");
			
			sql = "Select count(*), nomeq from ciclista group by nomeq";
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();
			
			// Recorremos cada grupo (equipo) devuelto por la consulta.
			while(rs.next()) {
				vcount = rs.getInt(1);
				vequipo = rs.getString(2);
				if(vcount>10) System.out.println("el equipo es: "+vequipo+" es numeroso tiene: "+vcount); 
				else System.out.println("el equipo: "+vequipo+" es pequeño tiene: "+vcount);
			}
			
			// ---------- Ejercicio 1a y 1c: insertar un puerto con comprobaciones ----------
			System.out.println("\n--- EJERCICIO 1a y 1c: Insertar Puerto con comprobaciones ---");
			System.out.println("Introduce el nombre del puerto:");
			String nomPuerto = sc.nextLine();
			System.out.println("Introduce la altura del puerto:");
			int altura = sc.nextInt();
			sc.nextLine();
			System.out.println("Introduce la pendiente:");
			String pendiente = sc.nextLine();
			System.out.println("Introduce el numero de la etapa (netapa):");
			int netapa = sc.nextInt();
			sc.nextLine();

			// La categoría del puerto se calcula según la altura:
			// >1500 -> "E" (especial), 1000-1500 -> "1", 800-999 -> "2". Otras alturas quedan vacías.
			String categoria = "";
			if (altura > 1500) categoria = "E";
			else if (altura >= 1000 && altura <= 1500) categoria = "1";
			else if (altura >= 800 && altura < 1000) categoria = "2";
			
			System.out.println("Categoria calculada automáticamente: " + categoria);

			// Comprobamos que la etapa existe antes de insertar el puerto (clave foránea).
			sql = "SELECT COUNT(*) FROM etapa WHERE netapa = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, netapa);
			ResultSet rsEtapa = pstmt.executeQuery();
			rsEtapa.next();
			
			if (rsEtapa.getInt(1) == 0) {
				System.out.println("Error: La etapa " + netapa + " no existe. No se puede insertar el puerto.");
			} else {
				boolean insertarPermitido = true;
				
				// Regla de negocio: una etapa no puede tener más de 2 puertos de 1ª categoría.
				if (categoria.equals("1")) {
					sql = "SELECT COUNT(*) FROM puerto WHERE netapa = ? AND categoria = '1'";
					pstmt = con.prepareStatement(sql);
					pstmt.setInt(1, netapa);
					ResultSet rsCat = pstmt.executeQuery();
					rsCat.next();
					if (rsCat.getInt(1) >= 2) {
						System.out.println("Error: La etapa ya tiene 2 puertos de 1ª categoría.");
						insertarPermitido = false;
					}
					rsCat.close();
				}
				
				// Si pasa todas las comprobaciones, insertamos el puerto.
				if (insertarPermitido) {
					sql = "INSERT INTO puerto (nompuerto, altura, categoria, pendiente, netapa) VALUES (?, ?, ?, ?, ?)";
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, nomPuerto);
					pstmt.setInt(2, altura);
					pstmt.setString(3, categoria);
					pstmt.setString(4, pendiente);
					pstmt.setInt(5, netapa);
					pstmt.executeUpdate();
					System.out.println("Puerto insertado correctamente.");
				}
			}
			rsEtapa.close();


			// ---------- Ejercicio 1b: insertar una etapa con comprobación de km ----------
			System.out.println("\n--- EJERCICIO 1b: Insertar Etapa con comprobación de KM ---");
			System.out.println("Introduce el número de etapa nueva (netapa):");
			int nuevaNetapa = sc.nextInt();
			System.out.println("Introduce los KM de la etapa:");
			int kmEtapa = sc.nextInt();
			sc.nextLine();
			System.out.println("Introduce la salida:");
			String salida = sc.nextLine();
			System.out.println("Introduce la llegada:");
			String llegada = sc.nextLine();
			System.out.println("Introduce el dorsal del ganador:");
			int dorsalGanador = sc.nextInt();
			sc.nextLine();

			// Regla: los km de una etapa deben estar entre 100 y 300.
			if (kmEtapa < 100 || kmEtapa > 300) {
				System.out.println("Error: Los km de la etapa deben estar entre 100 y 300. Inserción cancelada.");
			} else {
				// Regla: el total de km de la vuelta no puede superar 1500.
				// SUM devuelve NULL si la tabla está vacía; getInt lo convierte a 0.
				sql = "SELECT SUM(km) FROM etapa";
				pstmt = con.prepareStatement(sql);
				ResultSet rsKm = pstmt.executeQuery();
				int totalKmExistentes = 0;
				if (rsKm.next()) {
					totalKmExistentes = rsKm.getInt(1);
				}
				rsKm.close();
				
				if ((totalKmExistentes + kmEtapa) > 1500) {
					System.out.println("Error: Con esta etapa la vuelta superaría los 1500 km (Total proyectado: " 
							+ (totalKmExistentes + kmEtapa) + " km). Inserción cancelada.");
				} else {
					sql = "INSERT INTO etapa (netapa, km, salida, llegada, dorsal) VALUES (?, ?, ?, ?, ?)";
					pstmt = con.prepareStatement(sql);
					pstmt.setInt(1, nuevaNetapa);
					pstmt.setInt(2, kmEtapa);
					pstmt.setString(3, salida);
					pstmt.setString(4, llegada);
					pstmt.setInt(5, dorsalGanador);
					pstmt.executeUpdate();
					System.out.println("Etapa insertada correctamente.");
				}
			}

			// ---------- Ejercicio 2: actualizar la tabla GANA con el premio total de cada ciclista ----------
			System.out.println("\n--- EJERCICIO 2: Actualizar tabla GANA con los premios de los maillots ---");
			// Sumamos los premios de los maillots que lleva cada ciclista (agrupado por dorsal).
			sql = "SELECT l.dorsal, SUM(m.premio) AS total_premio " +
				  "FROM llevar l INNER JOIN maillot m ON l.codigo = m.codigo " +
				  "GROUP BY l.dorsal";
			pstmt = con.prepareStatement(sql);
			ResultSet rsPremios = pstmt.executeQuery();
			
			// Preparamos dos sentencias: una para actualizar y otra para insertar si no existe.
			PreparedStatement pstmtUpdateGana = con.prepareStatement("UPDATE gana SET premio = ? WHERE dorsal = ?");
			PreparedStatement pstmtInsertGana = con.prepareStatement("INSERT INTO gana (dorsal, premio) VALUES (?, ?)");
			
			int ciclistasActualizados = 0;
			int ciclistasNuevos = 0;
			
			// Patrón "update o insert": si el UPDATE no afecta a ninguna fila, el ciclista no está en GANA y se inserta.
			while (rsPremios.next()) {
				int dorsalActual = rsPremios.getInt("dorsal");
				int premioTotal = rsPremios.getInt("total_premio");
				
				pstmtUpdateGana.setInt(1, premioTotal);
				pstmtUpdateGana.setInt(2, dorsalActual);
				// executeUpdate devuelve cuántas filas se han modificado.
				int filasAfectadas = pstmtUpdateGana.executeUpdate();
				
				if (filasAfectadas == 0) {
					pstmtInsertGana.setInt(1, dorsalActual);
					pstmtInsertGana.setInt(2, premioTotal);
					pstmtInsertGana.executeUpdate();
					ciclistasNuevos++;
				} else {
					ciclistasActualizados++;
				}
			}
			// Cerramos los recursos que ya no necesitamos.
			rsPremios.close();
			pstmtUpdateGana.close();
			pstmtInsertGana.close();
			
			System.out.println("Tabla 'gana' actualizada con éxito.");
			System.out.println("Registros actualizados: " + ciclistasActualizados + " | Registros nuevos insertados: " + ciclistasNuevos);
			
			
			// ---------- Ejemplo 5: insertar un equipo y un representante ----------
			System.out.println("ejemplo 5");
			sql = "insert into equipo (nomeq) values(?)";
			pstmt = con.prepareStatement(sql);
			// Nota: esta sentencia se prepara pero nunca se ejecuta; el equipo no se inserta.
			vnomeq = "equipillo";
			pstmt.setString(1, vnomeq);
			
			System.out.println("nombre");
			vnombre = sc.nextLine();
			System.out.println("id");
			vid = sc.nextInt();
			sc.nextLine();
			System.out.println("nombre equipo");
			vnomeq = sc.nextLine();
			// Insertamos el representante con los datos introducidos.
			sql = "insert into representante values(?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, vid);
			pstmt.setString(2, vnombre );
			pstmt.setString(3, vnomeq);
			pstmt.executeUpdate();
			// Confirmamos todos los cambios de la transacción y volvemos al modo autocommit.
			con.commit();
			con.setAutoCommit(true);
		}catch(SQLException e) {
			// Error de base de datos (SQL incorrecto, clave duplicada, etc.).
			e.printStackTrace();
		}catch(FileNotFoundException e) {
			// No se encuentra el fichero de propiedades.
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			// Pase lo que pase, cerramos la conexión si sigue abierta.
			try {
				if(con!=null && !con.isClosed()) con.close();
			}catch(SQLException e){
				e.printStackTrace();	
			}
		}

		System.out.println("Programa Finalizado");
	}
}
