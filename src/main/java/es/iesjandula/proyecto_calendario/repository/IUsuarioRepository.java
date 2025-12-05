package es.iesjandula.proyecto_calendario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.iesjandula.proyecto_calendario.models.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, String>
{

}
