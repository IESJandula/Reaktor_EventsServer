package es.iesjandula.proyecto_calendario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.iesjandula.proyecto_calendario.dto.UsuarioResponseDto;
import es.iesjandula.proyecto_calendario.models.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, String>
{
	@Query("SELECT new es.iesjandula.proyecto_calendario.dto.UsuarioResponseDto(u.email, u.nombre) " +
		       "FROM Usuario u")
	List<UsuarioResponseDto> buscarUsuarios();
}
