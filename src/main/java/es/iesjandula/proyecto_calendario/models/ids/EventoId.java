package es.iesjandula.proyecto_calendario.models.ids;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa la clave primaria compuesta de la entidad Evento.
 * 
 * <p>Esta clase se utiliza para identificar de manera única un evento mediante
 * el título y las fechas de inicio y fin.</p>
 * 
 * <p>Al implementar Serializable, puede ser utilizada como clave embebida en JPA.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EventoId implements Serializable 
{

    /**
     * Serial version UID para garantizar la compatibilidad durante la serialización.
     */
	private static final long serialVersionUID = 7375364774028493903L;
	
    /**
     * Título del evento.
     * Forma parte de la clave primaria compuesta.
     */
	private String titulo;
	
    /**
     * Fecha de inicio del evento.
     * Forma parte de la clave primaria compuesta.
     */
    private Date fechaInicio;
    
    /**
     * Fecha de fin del evento.
     * Forma parte de la clave primaria compuesta.
     */
    private Date fechaFin;

}