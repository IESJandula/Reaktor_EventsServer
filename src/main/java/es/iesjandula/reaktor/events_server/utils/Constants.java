package es.iesjandula.reaktor.events_server.utils;


/**
 * Clase de constantes utilizadas en el proyecto Calendario de Eventos.
 * 
 * <p>Contiene mensajes de éxito y códigos/mensajes de error para Categoría, Evento,
 * Recordatorio y errores generales del servidor.</p>
 * 
 * <p>Estas constantes se utilizan en controladores, servicios y excepciones para
 * mantener consistencia en los mensajes y códigos de error en todo el proyecto.</p>
 */
public class Constants
{
    // --- Mensajes generales ---
	/** Mensaje al agregar un elemento correctamente */
    public static final String ELEMENTO_AGREGADO = "Elemento agregado correctamente." ;
    
    /** Mensaje al modificar un elemento correctamente */
    public static final String ELEMENTO_MODIFICADO = "Elemento modificado correctamente." ;
    
    /** Mensaje al eliminar un elemento correctamente */
    public static final String ELEMENTO_ELIMINADO = "Elemento eliminado correctamente." ;
    
    /** Mensaje al mostrar un elemento correctamente */
    public static final String ELEMENTO_MOSTRADO = "Elemento mostrado correctamente." ;


    // --- Errores de Categoría ---
    /** Código de error genérico para Categoría */
    public static final Integer ERR_CATEGORIA_CODE = 5 ;
    public static final String ERR_CATEGORIA = "CATEGORIA_ERROR" ;
    
    /** Código y mensaje cuando el nombre de la categoría es nulo o vacío */
    public static final Integer ERR_CATEGORIA_NOMBRE_NULO_VACIO_CODE = 6 ;
    public static final String ERR_CATEGORIA_NOMBRE_NULO_VACIO = "El nombre de la categoría no puede ser nulo ni vacío." ;
    
    /** Código y mensaje cuando la categoría ya existe */
    public static final Integer ERR_CATEGORIA_EXISTE_CODE = 7 ;
    public static final String ERR_CATEGORIA_EXISTE = "La categoría ya existe en el sistema." ;
    
    /** Código y mensaje cuando la categoría no existe */
    public static final Integer ERR_CATEGORIA_NO_EXISTE_CODE = 8 ;
    public static final String ERR_CATEGORIA_NO_EXISTE = "La categoría no existe en el sistema." ;

    // --- Errores de Evento ---
    /** Código de error genérico para Evento */
    public static final Integer ERR_EVENTO_CODE = 9 ;
    public static final String ERR_EVENTO = "EVENTO_ERROR" ;
    
    /** Código y mensaje cuando el título del evento es nulo o vacío */
    public static final Integer ERR_EVENTO_TITULO_NULO_VACIO_CODE = 10 ;
    public static final String ERR_EVENTO_TITULO_NULO_VACIO = "El título del evento no puede ser nulo ni vacío." ;
    
    /** Código y mensaje cuando las fechas son inválidas */
    public static final Integer ERR_EVENTO_FECHAS_INVALIDAS_CODE = 11 ;
    public static final String ERR_EVENTO_FECHAS_INVALIDAS = "La fecha de fin no puede ser anterior a la fecha de inicio." ;
    
    /** Código y mensaje cuando el evento ya existe */
    public static final Integer ERR_EVENTO_EXISTE_CODE = 12 ;
    public static final String ERR_EVENTO_EXISTE = "El evento ya existe en el sistema." ;
    
    /** Código y mensaje cuando el evento no existe */
    public static final Integer ERR_EVENTO_NO_EXISTE_CODE = 13 ;
    public static final String ERR_EVENTO_NO_EXISTE = "El evento no existe en el sistema." ;
    
    /** Código y mensaje cuando el usuario no tiene el acceso permitido */
    public static final Integer ERR_EVENTO_USUARIO_NO_PERMITIDO_CODE = 14 ; 
    public static final String ERR_EVENTO_USUARIO_NO_PERMITIDO_DESC = "El usuario no tiene el acceso permitido" ;
    // --- Errores de Recordatorio ---
    /** Código de error genérico para Recordatorio */
    public static final Integer ERR_RECORDATORIO_CODE = 15 ;
    public static final String ERR_RECORDATORIO = "RECORDATORIO_ERROR" ;
    
    /** Código y mensaje cuando la fecha del recordatorio es nula */
    public static final Integer ERR_RECORDATORIO_FECHA_NULA_CODE = 16 ;
    public static final String ERR_RECORDATORIO_FECHA_NULA = "La fecha del recordatorio no puede ser nula." ;
    
    /** Código y mensaje cuando el recordatorio no está asociado a un evento */
    public static final Integer ERR_RECORDATORIO_EVENTO_NULO_CODE = 17 ;
    public static final String ERR_RECORDATORIO_EVENTO_NULO = "El recordatorio debe estar asociado a un evento." ;
    
    /** Código y mensaje cuando el recordatorio ya existe */
    public static final Integer ERR_RECORDATORIO_EXISTE_CODE = 18 ;
    public static final String ERR_RECORDATORIO_EXISTE = "El recordatorio ya existe en el sistema." ;
    
    /** Código y mensaje cuando el recordatorio no existe */
    public static final Integer ERR_RECORDATORIO_NO_EXISTE_CODE = 19 ;
    public static final String ERR_RECORDATORIO_NO_EXISTE = "El recordatorio no existe en el sistema." ;
	
 	// --- Error de Servidor---
    /** Código y mensaje para errores generales del servidor */
    public static final Integer ERR_SERVIDOR_CODE = 20 ;
    public static final String ERR_SERVIDOR = "Error de servidor." ;
}
