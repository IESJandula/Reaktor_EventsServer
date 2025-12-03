package es.iesjandula.proyecto_calendario.models;

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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")

public class Usuario
{
	@Id
	@Column(length = 150)
	private String email;

	@Column(length = 100, nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "usuario")
    private List<Evento> eventos ;
}
