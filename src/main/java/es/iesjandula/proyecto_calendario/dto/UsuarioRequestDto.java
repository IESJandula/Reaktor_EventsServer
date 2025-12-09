package es.iesjandula.proyecto_calendario.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO utilizado para recibir los datos necesarios para crear o actualizar
 * un usuario dentro del sistema.
 *
 * <p>Incluye información básica como el correo electrónico del usuario
 * y su nombre identificativo.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDto
{
    /**
     * Correo electrónico del usuario.
     * Actúa como identificador único dentro del sistema.
     */
	private String email;
	
    /**
     * Nombre del usuario.
     * Representa el nombre visible o descriptivo asociado al usuario.
     */
    private String nombre;
    
}
