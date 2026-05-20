package es.iesjandula.reaktor.events_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoHuelgaRequestDto
{
	/**
     * Título de la huelga.
     */
    private String titulo;
    
    /**
     * Fecha inicio de inscripciones.
     */
    private Long fechaInicio;

    /**
     * Fecha fin de inscripciiones.
     */
    private Long fechaFin;
}
