package es.iesjandula.proyecto_calendario.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventoResponseDto
{
	private String titulo;
    private Date fechaInicio;
    private Date fechaFin;
    private String email;
    private String nombre;
}
