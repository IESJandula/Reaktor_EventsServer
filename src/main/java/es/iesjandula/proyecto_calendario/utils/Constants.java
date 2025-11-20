package es.iesjandula.proyecto_calendario.utils;

/**
 * Clase de constantes usadas en el proyecto Calendario de Eventos.
 */
public class Constants
{
    // --- Mensajes generales ---
    public static final String ELEMENTO_AGREGADO = "Elemento agregado correctamente.";
    public static final String ELEMENTO_MODIFICADO = "Elemento modificado correctamente.";
    public static final String ELEMENTO_ELIMINADO = "Elemento eliminado correctamente.";
    public static final String ELEMENTO_MOSTRADO = "Elemento mostrado correctamente.";

    // --- Errores de Usuario ---
    public static final String ERR_USUARIO_CODE = "USUARIO_ERROR";
    public static final String ERR_USUARIO_CORREO_NULO_VACIO = "El correo del usuario no puede ser nulo ni vacío.";
    public static final String ERR_USUARIO_EXISTE = "El usuario ya existe en el sistema.";
    public static final String ERR_USUARIO_NO_EXISTE = "El usuario no existe en el sistema.";

    // --- Errores de Categoría ---
    public static final String ERR_CATEGORIA_CODE = "CATEGORIA_ERROR";
    public static final String ERR_CATEGORIA_NOMBRE_NULO_VACIO = "El nombre de la categoría no puede ser nulo ni vacío.";
    public static final String ERR_CATEGORIA_EXISTE = "La categoría ya existe en el sistema.";
    public static final String ERR_CATEGORIA_NO_EXISTE = "La categoría no existe en el sistema.";

    // --- Errores de Evento ---
    public static final String ERR_EVENTO_CODE = "EVENTO_ERROR";
    public static final String ERR_EVENTO_TITULO_NULO_VACIO = "El título del evento no puede ser nulo ni vacío.";
    public static final String ERR_EVENTO_FECHAS_INVALIDAS = "La fecha de fin no puede ser anterior a la fecha de inicio.";
    public static final String ERR_EVENTO_EXISTE = "El evento ya existe en el sistema.";
    public static final String ERR_EVENTO_NO_EXISTE = "El evento no existe en el sistema.";

    // --- Errores de Recordatorio ---
    public static final String ERR_RECORDATORIO_CODE = "RECORDATORIO_ERROR";
    public static final String ERR_RECORDATORIO_FECHA_NULA = "La fecha del recordatorio no puede ser nula.";
    public static final String ERR_RECORDATORIO_EVENTO_NULO = "El recordatorio debe estar asociado a un evento.";
    public static final String ERR_RECORDATORIO_EXISTE = "El recordatorio ya existe en el sistema.";
    public static final String ERR_RECORDATORIO_NO_EXISTE = "El recordatorio no existe en el sistema.";
}
