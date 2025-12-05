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


    // --- Errores de Categoría ---
    public static final Integer ERR_CATEGORIA_CODE = 5;
    public static final String ERR_CATEGORIA = "CATEGORIA_ERROR";
    public static final Integer ERR_CATEGORIA_NOMBRE_NULO_VACIO_CODE = 6;
    public static final String ERR_CATEGORIA_NOMBRE_NULO_VACIO = "El nombre de la categoría no puede ser nulo ni vacío.";
    public static final Integer ERR_CATEGORIA_EXISTE_CODE = 7;
    public static final String ERR_CATEGORIA_EXISTE = "La categoría ya existe en el sistema.";
    public static final Integer ERR_CATEGORIA_NO_EXISTE_CODE = 8;
    public static final String ERR_CATEGORIA_NO_EXISTE = "La categoría no existe en el sistema.";

    // --- Errores de Evento ---
    public static final Integer ERR_EVENTO_CODE = 9;
    public static final String ERR_EVENTO = "EVENTO_ERROR";
    public static final Integer ERR_EVENTO_TITULO_NULO_VACIO_CODE = 10;
    public static final String ERR_EVENTO_TITULO_NULO_VACIO = "El título del evento no puede ser nulo ni vacío.";
    public static final Integer ERR_EVENTO_FECHAS_INVALIDAS_CODE = 11;
    public static final String ERR_EVENTO_FECHAS_INVALIDAS = "La fecha de fin no puede ser anterior a la fecha de inicio.";
    public static final Integer ERR_EVENTO_EXISTE_CODE = 12;
    public static final String ERR_EVENTO_EXISTE = "El evento ya existe en el sistema.";
    public static final Integer ERR_EVENTO_NO_EXISTE_CODE = 13;
    public static final String ERR_EVENTO_NO_EXISTE = "El evento no existe en el sistema.";

    // --- Errores de Recordatorio ---
    public static final Integer ERR_RECORDATORIO_CODE = 14;
    public static final String ERR_RECORDATORIO = "RECORDATORIO_ERROR";
    public static final Integer ERR_RECORDATORIO_FECHA_NULA_CODE = 15;
    public static final String ERR_RECORDATORIO_FECHA_NULA = "La fecha del recordatorio no puede ser nula.";
    public static final Integer ERR_RECORDATORIO_EVENTO_NULO_CODE = 16;
    public static final String ERR_RECORDATORIO_EVENTO_NULO = "El recordatorio debe estar asociado a un evento.";
    public static final Integer ERR_RECORDATORIO_EXISTE_CODE = 17;
    public static final String ERR_RECORDATORIO_EXISTE = "El recordatorio ya existe en el sistema.";
    public static final Integer ERR_RECORDATORIO_NO_EXISTE_CODE = 18;
    public static final String ERR_RECORDATORIO_NO_EXISTE = "El recordatorio no existe en el sistema.";
	
 	// --- Error de Servidor---
    public static final Integer ERR_SERVIDOR_CODE = 20;
    public static final String ERR_SERVIDOR = "Error de servidor.";
}
