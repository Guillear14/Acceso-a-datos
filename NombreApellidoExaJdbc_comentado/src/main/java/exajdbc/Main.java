package exajdbc;

// ============================================================
// MAIN: el punto de entrada del programa.
// Cuando ejecutas el proyecto, Java empieza SIEMPRE por el método main.
// Aquí solo "arrancamos" el menú (Interfaz). Toda la lógica está en otras clases.
// ============================================================
import interfaz.Interfaz;

public class Main {
    public static void main(String[] args) {
        // Creamos la pantalla del menú y le decimos que se ponga en marcha.
        new Interfaz().ejecutar();
    }
}
