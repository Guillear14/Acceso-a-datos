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

public class Conectar_bd {
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		
		Connection con = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DataSource ds;
		
		int vid, result, vcount ;
		String vnombre, vnomeq, sql, vequipo;
		Properties propiedades = new Properties();
		try {
			propiedades.load(new FileInputStream("configuracion\\propiedadesCiclismo"));
			ds = BasicDataSourceFactory.createDataSource(propiedades);
			con = ds.getConnection();
			con.setAutoCommit(false);
			
			System.out.println("ejemplo 1 ");
			System.out.println(" nombre directivo");
			vnombre = sc.nextLine();
			
			System.out.println("nombre equipo");
			vnomeq = sc.nextLine();
			
			System.out.println("id");
			vid = sc.nextInt();
			sc.nextLine(); // Limpieza del buffer
			
			sql = "insert into representante values(?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, vid);
			pstmt.setString(2, vnombre);
			pstmt.setString(3, vnomeq);
			pstmt.executeUpdate();
			
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
			
			System.out.println("ejemplo 3");
			System.out.println("nombre equipo");
			vnomeq = sc.nextLine();
			
			sql = "Select count(*), nomeq from equipo where nomeq=?";
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			pstmt.setString(1, vnomeq);
			rs = pstmt.executeQuery();
			
			if(rs.first() && rs.getInt(1)!=0) {
				vcount = rs.getInt(1);
				vequipo = rs.getString(2);
				if(vcount>10) System.out.println("el equipo es: "+vequipo+" es numeroso"); 
				else System.out.println("el equipo: "+vequipo+" es pequeño");
			}else System.out.println("equipo no existente");
	
			System.out.println("ejemplo 4");
			
			sql = "Select count(*), nomeq from ciclista group by nomeq";
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				vcount = rs.getInt(1);
				vequipo = rs.getString(2);
				if(vcount>10) System.out.println("el equipo es: "+vequipo+" es numeroso tiene: "+vcount); 
				else System.out.println("el equipo: "+vequipo+" es pequeño tiene: "+vcount);
			}
			
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

			String categoria = "";
			if (altura > 1500) categoria = "E";
			else if (altura >= 1000 && altura <= 1500) categoria = "1";
			else if (altura >= 800 && altura < 1000) categoria = "2";
			
			System.out.println("Categoria calculada automáticamente: " + categoria);

			sql = "SELECT COUNT(*) FROM etapa WHERE netapa = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, netapa);
			ResultSet rsEtapa = pstmt.executeQuery();
			rsEtapa.next();
			
			if (rsEtapa.getInt(1) == 0) {
				System.out.println("Error: La etapa " + netapa + " no existe. No se puede insertar el puerto.");
			} else {
				boolean insertarPermitido = true;
				
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

			if (kmEtapa < 100 || kmEtapa > 300) {
				System.out.println("Error: Los km de la etapa deben estar entre 100 y 300. Inserción cancelada.");
			} else {
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

			System.out.println("\n--- EJERCICIO 2: Actualizar tabla GANA con los premios de los maillots ---");
			sql = "SELECT l.dorsal, SUM(m.premio) AS total_premio " +
				  "FROM llevar l INNER JOIN maillot m ON l.codigo = m.codigo " +
				  "GROUP BY l.dorsal";
			pstmt = con.prepareStatement(sql);
			ResultSet rsPremios = pstmt.executeQuery();
			
			PreparedStatement pstmtUpdateGana = con.prepareStatement("UPDATE gana SET premio = ? WHERE dorsal = ?");
			PreparedStatement pstmtInsertGana = con.prepareStatement("INSERT INTO gana (dorsal, premio) VALUES (?, ?)");
			
			int ciclistasActualizados = 0;
			int ciclistasNuevos = 0;
			
			while (rsPremios.next()) {
				int dorsalActual = rsPremios.getInt("dorsal");
				int premioTotal = rsPremios.getInt("total_premio");
				
				pstmtUpdateGana.setInt(1, premioTotal);
				pstmtUpdateGana.setInt(2, dorsalActual);
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
			rsPremios.close();
			pstmtUpdateGana.close();
			pstmtInsertGana.close();
			
			System.out.println("Tabla 'gana' actualizada con éxito.");
			System.out.println("Registros actualizados: " + ciclistasActualizados + " | Registros nuevos insertados: " + ciclistasNuevos);
			
			
			
			System.out.println("ejemplo 5");
			sql = "insert into equipo (nomeq) values(?)";
			pstmt = con.prepareStatement(sql);
			vnomeq = "equipillo";
			pstmt.setString(1, vnomeq);
			
			System.out.println("nombre");
			vnombre = sc.nextLine();
			System.out.println("id");
			vid = sc.nextInt();
			sc.nextLine();
			System.out.println("nombre equipo");
			vnomeq = sc.nextLine();
			sql = "insert into representante values(?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, vid);
			pstmt.setString(2, vnombre );
			pstmt.setString(3, vnomeq);
			pstmt.executeUpdate();
			con.commit();
			con.setAutoCommit(true);
		}catch(SQLException e) {
			e.printStackTrace();
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(con!=null && !con.isClosed()) con.close();
			}catch(SQLException e){
				e.printStackTrace();	
			}
		}

		System.out.println("Programa Finalizado");
	}
}