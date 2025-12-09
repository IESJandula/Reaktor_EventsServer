package es.iesjandula.proyecto_calendario.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
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
    private Date fechaInicio;
    
    /**
     * Fecha y hora de fin del evento.
     */
    private Date fechaFin;
    
    /**
     * Correo electrónico del usuario asociado al evento.
     * Permite identificar al propietario o creador del evento.
     */
    private String email;
    
    /**
     * Nombre del usuario asociado al evento.
     * Permite identificar al propietario o creador del evento.
     */
    private String nombre;
}
