package interfaz;

import controlador.ConsultaOcupacionGrupos;
import controlador.PresentarPractica;
import excepciones.ExcepcionPresentacion;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

// ============================================================
// INTERFAZ = la PANTALLA del programa: lo que ve y escribe el usuario.
//
// Su trabajo es SOLO:
//   1. Preguntar cosas por teclado.
//   2. Pasárselas a los controladores.
//   3. Mostrar resultados o errores.
// No calcula nada ni toca la base de datos: eso es trabajo de otras capas (MVC).
// ============================================================
public class Interfaz {

    // Scanner = el "oído" del programa: lee lo que escribe el usuario por teclado.
    private final Scanner sc = new Scanner(System.in);

    // Los controladores que hacen el trabajo de verdad.
    private final ConsultaOcupacionGrupos consultaOcupacion = new ConsultaOcupacionGrupos();
    private final PresentarPractica presentarPractica = new PresentarPractica();

    // ---------- MENÚ PRINCIPAL ----------
    public void ejecutar() {
        int opcion;
        do {  // "do ... while" = repite el menú hasta que el usuario elija 0 (salir)
            System.out.println("\n1. Consultar ocupación de grupos");
            System.out.println("2. Presentar una práctica");
            System.out.println("0. Salir");
            opcion = leerEntero("Elige una opción: ");

            try {  // "try" = intenta esto; si falla, salta al "catch" correspondiente
                switch (opcion) {          // según el número elegido...
                    case 1: consultarOcupacion(); break;  // ...hacemos una cosa
                    case 2: presentar(); break;           // ...o la otra
                    case 0: System.out.println("Hasta luego."); break;
                    default: System.out.println("Opción no válida."); // cualquier otro número
                }
            } catch (SQLException e) {
                // "catch" = recoge el error si algo falló en la base de datos.
                System.out.println("Error de base de datos: " + e.getMessage());
            } catch (ExcepcionPresentacion e) {
                // Recoge los avisos de reglas incumplidas (fuera de plazo, nota, etc.).
                System.out.println("No se ha podido presentar la práctica: " + e.getMessage());
            }
        } while (opcion != 0);  // mientras no sea 0, sigue en el menú
        sc.close();             // cerramos el "oído" al salir
    }

    // ---------- OPCIÓN 1: consulta de ocupación ----------
    private void consultarOcupacion() throws SQLException {
        System.out.print("Código de la asignatura: ");   // "print" = escribir sin salto de línea
        String codigo = sc.nextLine().trim();            // leer la línea y quitar espacios sobrantes

        List<String> lineas = consultaOcupacion.obtenerOcupacion(codigo);
        if (lineas.isEmpty()) {                          // la lista está vacía = no hay resultados
            System.out.println("No se han encontrado matrículas en esa asignatura");
        } else {
            lineas.forEach(System.out::println);         // imprimir cada línea una por una
        }
    }

    // ---------- OPCIÓN 2: presentar una práctica ----------
    private void presentar() throws SQLException, ExcepcionPresentacion {
        int idPractica = leerEntero("Código de la práctica: ");
        System.out.print("Nombre del alumno: ");
        String nombre = sc.nextLine().trim();
        Date fecha = leerFecha("Fecha de presentación (dd/MM/yyyy): ");
        double nota = leerDecimal("Nota: ");

        // Si alguna regla falla, aquí saltará la excepción y la captura "ejecutar()".
        presentarPractica.presentar(idPractica, nombre, fecha, nota);
        System.out.println("Práctica presentada correctamente.");
    }

    // ---------- AYUDANTES: leer datos y no dejar que el usuario se equivoque ----------

    // Pide un número entero. Si escribe letras, vuelve a preguntar.
    private int leerEntero(String mensaje) {
        while (true) {                       // bucle infinito que solo se rompe con "return"
            System.out.print(mensaje);
            try {
                return Integer.parseInt(sc.nextLine().trim()); // convertir texto -> número
            } catch (NumberFormatException e) {                // no era un número válido
                System.out.println("Introduce un número entero.");
            }
        }
    }

    // Pide un número decimal. Acepta coma o punto (7,5 o 7.5).
    private double leerDecimal(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                // replace(',', '.') cambia la coma por punto para que Java lo entienda.
                return Double.parseDouble(sc.nextLine().trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número válido.");
            }
        }
    }

    // Pide una fecha en formato dd/MM/yyyy. Si no es válida, vuelve a preguntar.
    private Date leerFecha(String mensaje) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); // el "molde" de la fecha
        formato.setLenient(false);  // false = no aceptar fechas raras como 31/02/2025
        while (true) {
            System.out.print(mensaje);
            try {
                return formato.parse(sc.nextLine().trim()); // texto -> fecha
            } catch (ParseException e) {
                System.out.println("Formato de fecha incorrecto.");
            }
        }
    }
}
