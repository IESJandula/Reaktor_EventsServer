package es.iesjandula.reaktor.events_server.models;

import java.util.Date;

import es.iesjandula.reaktor.events_server.models.ids.EventoId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa un evento dentro del sistema de calendario.
 * 
 * <p>Cada evento está asociado a un usuario y puede pertenecer a una categoría.
 * La identidad del evento se define mediante una clave compuesta EventoId.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "evento")

public class Evento
{
    /**
     * Clave primaria compuesta del evento.
     * Contiene información como el título y las fechas del evento.
     */
	@EmbeddedId
    private EventoId eventoId;
	
    /**
     * Fecha de fin del evento.
     * Forma parte de la clave primaria compuesta.
     */
	@Column
    private Date fechaFin;

    /**
     * Nombre del usuario.
     */
	@Column(length = 100)
    private String usuarioNombre;
	
    /**
     * Apellidos del usuario.
     */
	@Column(length = 100)
    private String usuarioApellidos;
    /**
     * Categoría a la que pertenece el evento.
     * Relación muchos-a-uno con la entidad Categoria.
     * La columna asociada en la base de datos es nombre.
     */
    @ManyToOne
    @JoinColumn(name = "nombre")
    private Categoria categoria;
}
