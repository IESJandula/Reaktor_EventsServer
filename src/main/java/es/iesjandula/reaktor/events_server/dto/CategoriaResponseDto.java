package es.iesjandula.reaktor.events_server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * DTO utilizado para enviar información de una categoría
 * desde el backend hacia el cliente.
 *
 * <p>Proporciona los datos principales de la categoría,
 * como su nombre y color representativo.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaResponseDto 
{
    /**
     * Nombre de la categoría.
     * Identifica de forma única la categoría enviada en la respuesta.
     */
    private String nombre;
    
    /**
     * Color asociado a la categoría.
     * Representa visualmente el tipo o grupo al que pertenece.
     */
    private String color;
}

