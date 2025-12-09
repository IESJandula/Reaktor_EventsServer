package es.iesjandula.proyecto_calendario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.iesjandula.proyecto_calendario.models.Usuario;

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

