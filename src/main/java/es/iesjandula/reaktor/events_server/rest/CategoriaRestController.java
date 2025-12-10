package es.iesjandula.reaktor.events_server.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.iesjandula.reaktor.base.utils.BaseConstants;
import es.iesjandula.reaktor.events_server.dto.CategoriaRequestDto;
import es.iesjandula.reaktor.events_server.dto.CategoriaResponseDto;
import es.iesjandula.reaktor.events_server.models.Categoria;
import es.iesjandula.reaktor.events_server.repository.ICategoriaRepository;
import es.iesjandula.reaktor.events_server.utils.EventsServerException;
import es.iesjandula.reaktor.events_server.utils.Constants;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para la gestión de categorías de eventos.
 *
 * <p>Proporciona endpoints para crear, eliminar y listar categorías.
 * La mayoría de las operaciones requieren rol de profesor para su ejecución.</p>
 */
@Slf4j
@RequestMapping("/events/categories")
@RestController
public class CategoriaRestController
{
	@Autowired
	private ICategoriaRepository categoriaRepository;

    /**
     * Crea una nueva categoría o intenta modificar una existente.
     *
     * <p>Verifica que el nombre no sea nulo o vacío y que la categoría no exista previamente.
     * Si ocurre algún error de validación, se devuelve un ResponseEntity con código 400.
     * Para errores del servidor se devuelve código 500.</p>
     *
     * @param categoriaRequestDto DTO con los datos de la categoría a crear/modificar.
     * @return ResponseEntity indicando éxito o error de la operación.
     */
    @PreAuthorize("hasAnyRole('" + BaseConstants.ROLE_PROFESOR + "')")
	@PostMapping(value = "/", consumes = "application/json")
	public ResponseEntity<?> crearModificarCategoria(@RequestBody CategoriaRequestDto categoriaRequestDto)
	{
		try
		{
			//Comprueba que el nombre de la categoría no sea nulo ni esté vacío antes de continuar
			if (categoriaRequestDto.getNombre() == null || categoriaRequestDto.getNombre().isEmpty())
			{
				log.error(Constants.ERR_CATEGORIA_NOMBRE_NULO_VACIO);
				throw new EventsServerException(Constants.ERR_CATEGORIA_CODE, Constants.ERR_CATEGORIA_NOMBRE_NULO_VACIO);
			}
			//Comprueba si ya existe una categoría con el mismo nombre en la base de datos
			if (this.categoriaRepository.existsById(categoriaRequestDto.getNombre()))
			{
				log.error(Constants.ERR_CATEGORIA_EXISTE);
				throw new EventsServerException(Constants.ERR_CATEGORIA_CODE, Constants.ERR_CATEGORIA_EXISTE);
			}

			Categoria categoria = new Categoria();
			categoria.setNombre(categoriaRequestDto.getNombre());
			categoria.setColor(categoriaRequestDto.getColor());

			this.categoriaRepository.saveAndFlush(categoria);
			log.info(Constants.ELEMENTO_AGREGADO);
			return ResponseEntity.ok().body(Constants.ELEMENTO_AGREGADO);
		} 
		catch (EventsServerException exception)
		{
			return ResponseEntity.badRequest().body(exception.getBodyExceptionMessage());
		}
   	 	catch (Exception exception)
        {
    		EventsServerException calendarioException= new EventsServerException(Constants.ERR_SERVIDOR_CODE,Constants.ERR_SERVIDOR);
            return ResponseEntity.status(500).body(calendarioException.getBodyExceptionMessage());
    		
        }
	}

    
    /**
     * Elimina una categoría existente por su nombre.
     *
     * <p>Si la categoría no existe, devuelve un error 400. En caso de errores del servidor,
     * devuelve código 500.</p>
     *
     * @param nombre Nombre de la categoría a eliminar.
     * @return ResponseEntity indicando éxito o error de la operación.
     */
    @PreAuthorize("hasAnyRole('" + BaseConstants.ROLE_PROFESOR + "')")
	@DeleteMapping(value = "/{nombre}")
	public ResponseEntity<?> eliminarCategoria(@PathVariable String nombre)
	{
		try
		{
			//Comprueba si la categoría a eliminar NO existe en la base de datos
			if (!this.categoriaRepository.existsById(nombre))
			{
				log.error(Constants.ERR_CATEGORIA_NO_EXISTE);
				throw new EventsServerException(Constants.ERR_CATEGORIA_CODE, Constants.ERR_CATEGORIA_NO_EXISTE);
			}

			this.categoriaRepository.deleteById(nombre);
			log.info(Constants.ELEMENTO_ELIMINADO);
			return ResponseEntity.ok().body(Constants.ELEMENTO_ELIMINADO);
		} 
		catch (EventsServerException exception)
		{
			return ResponseEntity.badRequest().body(exception.getBodyExceptionMessage());
		}
   	 	catch (Exception exception)
        {
    		EventsServerException calendarioException= new EventsServerException(Constants.ERR_SERVIDOR_CODE,Constants.ERR_SERVIDOR);
            return ResponseEntity.status(500).body(calendarioException.getBodyExceptionMessage());
    		
        }
	}

    /**
     * Obtiene la lista de todas las categorías disponibles.
     *
     * <p>Devuelve un listado de CategoriaResponseDto con nombre y color de cada categoría.
     * Este endpoint requiere rol de profesor para su ejecución.</p>
     *
     * @return ResponseEntity con la lista de categorías.
     */
    @PreAuthorize("hasRole('" + BaseConstants.ROLE_PROFESOR + "')")
	@GetMapping(value = "/")
	public ResponseEntity<?> obtenerCategorias()
	{
		List<CategoriaResponseDto> categorias = this.categoriaRepository.buscarCategorias();
		return ResponseEntity.ok(categorias);
	}

    
}
