package es.iesjandula.proyecto_calendario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.iesjandula.proyecto_calendario.dto.EventoResponseDto;
import es.iesjandula.proyecto_calendario.models.Evento;
import es.iesjandula.proyecto_calendario.models.ids.EventoId;

/**
 * Repositorio JPA para la entidad Evento.
 * 
 * <p>Proporciona operaciones CRUD básicas gracias a JpaRepository y
 * define consultas personalizadas para obtener eventos según diferentes criterios.</p>
 */
public interface IEventoRepository extends JpaRepository<Evento, EventoId>
{
    /**
     * Recupera todos los eventos del sistema y los transforma
     * en objetos EventoResponseDto.
     * 
     * <p>La consulta selecciona únicamente los campos necesarios:
     * título, fecha de inicio, fecha de fin, correo del usuario y nombre de la categoría.</p>
     * 
     * @return Lista de EventoResponseDto con los datos de cada evento.
     */
	@Query("SELECT new es.iesjandula.proyecto_calendario.dto.EventoResponseDto(" +
	           "e.eventoId.titulo, e.eventoId.fechaInicio, e.eventoId.fechaFin, " +
	           "e.usuario.email, " +
	           "e.categoria.nombre) " +
	           "FROM Evento e")
	List<EventoResponseDto> buscarEventos();
	
	   /**
     * Recupera todos los eventos asociados a un usuario específico
     * identificado por su correo electrónico y los transforma
     * en objetos EventoResponseDto.
     * 
     * <p>Se realiza un JOIN con la entidad Usuario
     * para filtrar por el correo electrónico proporcionado.</p>
     * 
     * @param email Correo electrónico del usuario cuyos eventos se desean recuperar.
     * @return Lista de EventoResponseDto con los eventos del usuario.
     */
	@Query("SELECT new es.iesjandula.proyecto_calendario.dto.EventoResponseDto(" +
	           "e.eventoId.titulo, e.eventoId.fechaInicio, e.eventoId.fechaFin, " +
	           "e.usuario.email, e.categoria.nombre) " +
	           "FROM Evento e " +
		       "JOIN e.usuario u " +
		       "WHERE u.email = :email" )
	List<EventoResponseDto> buscarEventosPorUsuario(@Param("email") String email);

	
}

