package es.iesjandula.proyecto_calendario.models;

import es.iesjandula.proyecto_calendario.models.ids.EventoId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "eventos")

public class Evento
{
	@EmbeddedId
    private EventoId id;
	
	@ManyToOne
    @JoinColumn(name = "email", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "nombre")
    private Categoria categoria;
}
