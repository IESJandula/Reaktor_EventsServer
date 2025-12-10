package es.iesjandula.reaktor.events_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.iesjandula.reaktor.events_server.models.Usuario;

/**
 * Repositorio JPA para la entidad Usuario.
 * 
 * <p>Proporciona operaciones CRUD básicas gracias a JpaRepository.
 * No define consultas personalizadas, ya que las operaciones estándar son suficientes
 * para manejar los usuarios del sistema.</p>
 */
public interface IUsuarioRepository extends JpaRepository<Usuario, String>
{

}

