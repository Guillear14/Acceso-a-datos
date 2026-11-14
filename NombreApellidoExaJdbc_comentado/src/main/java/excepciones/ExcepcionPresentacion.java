package excepciones;

// ============================================================
// EXCEPCIÓN PROPIA: un "aviso de error" que inventamos nosotros.
// Piensa en ella como un policía que para el proceso cuando algo no cumple las reglas,
// por ejemplo: "entregas tarde" o "nota demasiado alta".
// Extiende de Exception, así que Java OBLIGA a capturarla o a avisar de que puede pasar.
// ============================================================
public class ExcepcionPresentacion extends Exception {

    // El mensaje es el texto que verá el usuario (por ejemplo "estás fuera del límite de entrega").
    public ExcepcionPresentacion(String mensaje) {
        super(mensaje); // "super" pasa el mensaje a la clase Exception que tenemos por dentro
    }
}
