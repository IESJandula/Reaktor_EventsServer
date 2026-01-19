package es.iesjandula.reaktor.events_server.dto;

import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO utilizado para enviar información detallada de un evento
 * desde el backend hacia el cliente.
 *
 * <p>Incluye datos como el título del evento, sus fechas de inicio y fin,
 * el correo electrónico del usuario asociado y el nombre de la categoría
 * a la que pertenece.</p>
 */
@Getter
@Setter
@NoArgsConstructor
public class EventoResponseDto
{
    /**
     * Título del evento.
     * Describe brevemente del evento.
     */
	private String titulo;
	
    /**
     * Fecha y hora de inicio del evento.
     */
    private Long fechaInicio;
    
    /**
     * Fecha y hora de fin del evento.
     */
    private Long fechaFin;
    
    /**
    * Nombre de categoría a la que pertenece el evento.
    */
    private String nombre ;
    
    
    /**
     * Correo electrónico del usuario asociado al evento.
     * Permite identificar al propietario o creador del evento.
     */
    private String usuarioEmail ; 
    
    /**
     * Nombre del usuario asociado al evento.
     * Permite identificar al propietario o creador del evento.
     */
    private String usuarioNombre;
    
    /**
     * Apellidos del usuario asociado al evento.
     * Permite identificar al propietario o creador del evento.
     */
    private String usuarioApellidos;
    
    public EventoResponseDto(String titulo, Date fechaInicio, Date fechaFin, String nombre, String usuarioEmail, String usuarioNombre, String usuarioApellidos) {
        this.titulo = titulo;
        this.fechaInicio = fechaInicio != null ? fechaInicio.getTime() : null;
        this.fechaFin = fechaFin != null ? fechaFin.getTime() : null;
        this.nombre = nombre;
        this.usuarioNombre = usuarioNombre;
        this.usuarioEmail = usuarioEmail;
        this.usuarioApellidos= usuarioApellidos;
    }
    
    
}
