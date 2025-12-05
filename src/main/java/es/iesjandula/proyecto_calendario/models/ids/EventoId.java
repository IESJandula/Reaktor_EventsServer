package es.iesjandula.proyecto_calendario.models.ids;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EventoId implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7375364774028493903L;
	private String titulo;
    private Date fechaInicio;
    private Date fechaFin;

}