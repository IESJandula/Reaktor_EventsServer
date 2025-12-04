package es.iesjandula.proyecto_calendario.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventoRequestDto
{
    private String titulo;
    private Long fechaInicio;
    private Long fechaFin;
    private String email;
    private String nombre;
}
