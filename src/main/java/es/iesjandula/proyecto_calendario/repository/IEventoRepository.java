package es.iesjandula.proyecto_calendario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import es.iesjandula.proyecto_calendario.dto.EventoResponseDto;
import es.iesjandula.proyecto_calendario.models.Evento;

import es.iesjandula.proyecto_calendario.models.ids.EventoId;

public interface IEventoRepository extends JpaRepository<Evento, EventoId>
{
	@Query("SELECT new es.iesjandula.proyecto_calendario.dto.EventoResponseDto(" +
	           "e.id.titulo, e.id.fechaInicio, e.id.fechaFin, " +
	           "e.usuario.correoUsuario, " +
	           "e.categoria.nombreCategoria) " +
	           "FROM Evento e")
	List<EventoResponseDto> buscarEventos();
	
	@Query("SELECT new es.iesjandula.proyecto_calendario.dto.EventoResponseDto(" +
	           "e.id.titulo, e.id.fechaInicio, e.id.fechaFin, " +
	           "e.usuario.correoUsuario, " +
	           "e.categoria.nombreCategoria) " +
	           "FROM Evento e " +
		       "JOIN e.usuario u " +
		       "WHERE u.correoUsuario = :correoUsuario" )
		List<EventoResponseDto> buscarEventosPorUsuario();

	
}
