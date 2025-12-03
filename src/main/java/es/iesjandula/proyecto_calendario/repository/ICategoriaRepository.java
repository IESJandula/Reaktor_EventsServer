package es.iesjandula.proyecto_calendario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.iesjandula.proyecto_calendario.dto.CategoriaResponseDto;
import es.iesjandula.proyecto_calendario.models.Categoria;

public interface ICategoriaRepository extends JpaRepository<Categoria, String>
{
	@Query("SELECT new es.iesjandula.proyecto_calendario.dto.CategoriaResponseDto(c.nombre, c.color) " +
		       "FROM Categoria c")
	List<CategoriaResponseDto> buscarCategorias();
}
