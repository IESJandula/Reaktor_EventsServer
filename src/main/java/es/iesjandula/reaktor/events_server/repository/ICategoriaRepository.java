package es.iesjandula.reaktor.events_server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.iesjandula.reaktor.events_server.dto.CategoriaResponseDto;
import es.iesjandula.reaktor.events_server.models.Categoria;

/**
 * Repositorio JPA para la entidad Categoria.
 * 
 * <p>Proporciona operaciones CRUD básicas gracias a JpaRepository y
 * define consultas personalizadas según las necesidades del proyecto.</p>
 */
public interface ICategoriaRepository extends JpaRepository<Categoria, String>
{
	/**
    * Recupera todas las categorías del sistema y las transforma
    * en objetos CategoriaResponseDto.
    * 
    * <p>Utiliza una consulta JPQL para seleccionar únicamente los campos
    * necesarios (nombre y color), optimizando el rendimiento frente a
    * recuperar la entidad completa.</p>
    * 
    * @return Lista de CategoriaResponseDto con el nombre y color de cada categoría.
    */
	@Query("SELECT new es.iesjandula.proyecto_calendario.dto.CategoriaResponseDto(c.nombre, c.color) " +
		       "FROM Categoria c")
	List<CategoriaResponseDto> buscarCategorias();
}
