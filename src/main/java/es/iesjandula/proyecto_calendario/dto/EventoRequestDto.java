package es.iesjandula.proyecto_calendario.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * DTO utilizado para recibir los datos necesarios para crear o actualizar un evento
 * dentro del sistema de calendario.
 *
 * <p>Incluye información básica como el título del evento, fechas de inicio y fin,
 * el correo electrónico del usuario asociado y el nombre de la categoría.</p>
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventoRequestDto
{
    /**
     * Título descriptivo del evento.
     * No debe ser nulo ni vacío.
     */
    private String titulo;
    
    /**
     * Fecha y hora de inicio del evento, representada como un valor en milisegundos
     * desde la época (Unix timestamp).
     */
    private Long fechaInicio;
    
    /**
     * Fecha y hora de fin del evento, representada como un valor en milisegundos
     * desde la época.
     */
    private Long fechaFin;
    
    /**
     * Correo electrónico del usuario al que pertenece el evento.
     * Se utiliza para asociar el evento a un propietario.
     */
    private String email;
    
    /**
     * Nombre de la categoría a la que se asigna el evento.
     * Debe coincidir con una categoría existente en el sistema.
     */
    private String nombre;
}
