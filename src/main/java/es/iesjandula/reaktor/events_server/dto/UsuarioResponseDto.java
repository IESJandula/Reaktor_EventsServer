package es.iesjandula.reaktor.events_server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO utilizado para enviar la información de un usuario
 * desde el backend hacia el cliente.
 *
 * <p>Incluye datos esenciales como el correo electrónico y el nombre
 * del usuario.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDto 
{
    /**
     * Correo electrónico del usuario.
     * Representa el identificador único dentro del sistema.
     */
    private String email;
    
    /**
     * Nombre del usuario.
     * Corresponde al nombre visible o descriptivo asociado al usuario.
     */
    private String nombre;
}