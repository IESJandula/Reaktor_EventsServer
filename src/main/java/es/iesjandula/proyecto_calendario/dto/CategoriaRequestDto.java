package es.iesjandula.proyecto_calendario.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO utilizado para recibir y transportar la información necesaria
 * para crear o actualizar una categoría dentro del sistema.
 *
 * <p>Contiene únicamente los datos básicos requeridos:
 * nombre de la categoría y su color representativo.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaRequestDto
{
    /**
     * Nombre de la categoría.
     * Representa el identificador principal que describe la categoría.
     * No debe ser nulo ni vacío.
     */
	 private String nombre;
	 
    /**
     * Color asignado a la categoría.
     * Puede representar un color en formato texto o código hexadecimal.
     */
	 private String color;
}
