package es.iesjandula.reaktor.events_server.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa un usuario dentro del sistema de calendario.
 * 
 * <p>Cada usuario puede tener múltiples eventos asociados.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")

public class Usuario
{
    /**
     * Correo electrónico del usuario.
     * Actúa como clave primaria de la entidad.
     * Longitud máxima: 150 caracteres.
     */
	@Id
	@Column(length = 150)
	private String email;

    /**
     * Nombre completo o visible del usuario.
     * Este campo no puede ser nulo.
     * Longitud máxima: 100 caracteres.
     */
	@Column(length = 100, nullable = false)
    private String nombre;

    /**
     * Lista de eventos asociados a este usuario.
     * 
     * <p>Representa una relación uno-a-muchos con la entidad Evento.
     * Cada evento referencia al usuario mediante su atributo usuario.</p>
     */
    @OneToMany(mappedBy = "usuario")
    private List<Evento> eventos ;
}
