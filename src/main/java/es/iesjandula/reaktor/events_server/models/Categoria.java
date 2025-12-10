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
 * Representa una categoría dentro del sistema, utilizada para agrupar eventos
 * en función de un nombre y un color identificativo. 
 *
 * <p>Cada categoría puede tener múltiples eventos asociados, formando una relación
 * uno-a-muchos con la entidad.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categorias")

public class Categoria
{
    /**
     * Nombre único de la categoría.
     * Actúa como clave primaria dentro de la tabla categorias.
     * Longitud máxima permitida: 100 caracteres.
     */
    @Id
    @Column(length = 100)
    private String nombre;

    /**
     * Color representativo asociado a la categoría.
     * Puede ser un código textual o hexadecimal.
     * Longitud máxima permitida: 10 caracteres.
     */
    @Column(length = 10)
    private String color;

    /**
     * Lista de eventos incluidos dentro de esta categoría.
     *
     * <p>Relación uno-a-muchos gestionada por el atributo
     * en la entidad Evento. Esto indica que la categoría no almacena
     * claves foráneas, sino que son los eventos los que referencian a la categoría.</p>
     */
    @OneToMany(mappedBy = "categoria")
    private List<Evento> eventos;
}
