package ejemplo_test;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// EJERCICIO 9
// Pedir los datos de un nuevo producto al usuario y añadirlo a productos.json.
public class ejercicio9 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        JSONParser parse = new JSONParser();
        JSONArray productos = null;

        try {
            // 1. Leer los productos que ya existen en el fichero.
            try (FileReader lector = new FileReader("ficheros/productos.json")) {
                productos = (JSONArray) parse.parse(lector);
            }

            // 2. Pedir los datos del nuevo producto.
            System.out.print("Nombre del producto: ");
            String nombre = sc.nextLine().trim();

            System.out.print("Precio (ej: 19.99): ");
            double precio = Double.parseDouble(sc.nextLine().trim().replace(',', '.'));

            System.out.print("Categorías (separadas por comas): ");
            JSONArray categorias = new JSONArray();
            for (String c : sc.nextLine().split(",")) {
                if (!c.trim().isEmpty()) {
                    categorias.add(c.trim());
                }
            }

            System.out.print("Stock: ");
            long stock = Long.parseLong(sc.nextLine().trim());

            System.out.print("Almacén (ej: Almacén 1): ");
            String almacen = sc.nextLine().trim();

            System.out.print("Estantería (ej: Estantería A): ");
            String estanteria = sc.nextLine().trim();

            System.out.print("Pasillo (ej: Pasillo 1): ");
            String pasillo = sc.nextLine().trim();

            System.out.print("Tipo de producto (ej: Electrónica): ");
            String tipo = sc.nextLine().trim();

            // 3. "ubicacion" es un objeto dentro del producto, igual que en el fichero.
            JSONObject ubicacion = new JSONObject();
            ubicacion.put("almacen", almacen);
            ubicacion.put("estanteria", estanteria);
            ubicacion.put("pasillo", pasillo);

            // 4. Crear el producto con las mismas claves que el resto del fichero.
            JSONObject nuevo = new JSONObject();
            nuevo.put("nombre_producto", nombre);
            nuevo.put("precio", precio);
            nuevo.put("categorias", categorias);
            nuevo.put("stock", stock);
            nuevo.put("ubicacion", ubicacion);
            nuevo.put("tipo_producto", tipo);

            // 5. Añadir el producto a la lista.
            productos.add(nuevo);

            // 6. Guardar la lista completa en el fichero (sobrescribe el archivo).
            try (FileWriter escritor = new FileWriter("ficheros/productos.json")) {
                escritor.write(productos.toJSONString());
            }

            System.out.println("Producto añadido correctamente. Total de productos: " + productos.size());

        } catch (NumberFormatException e) {
            // Precio o stock no son números válidos.
            System.out.println("Error: el precio y el stock deben ser números.");
        } catch (Exception e) {
            // Cualquier otro error (fichero no encontrado, JSON no válido...).
            System.out.println("Error: " + e.getMessage());
        } finally {
            sc.close();
        }
    }
}
