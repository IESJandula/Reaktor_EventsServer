package es.iesjandula.proyecto_calendario.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.iesjandula.proyecto_calendario.dto.CategoriaRequestDto;
import es.iesjandula.proyecto_calendario.dto.CategoriaResponseDto;
import es.iesjandula.proyecto_calendario.models.Categoria;
import es.iesjandula.proyecto_calendario.repository.ICategoriaRepository;
import es.iesjandula.proyecto_calendario.utils.CalendarioException;
import es.iesjandula.proyecto_calendario.utils.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/categoria")
@RestController
public class CategoriaRestController
{
	@Autowired
	private ICategoriaRepository categoriaRepository;

	@PostMapping(value = "/", consumes = "application/json")
	public ResponseEntity<?> crearCategoria(@RequestBody CategoriaRequestDto categoriaRequestDto)
	{
		try
		{
			if (categoriaRequestDto.getNombre() == null || categoriaRequestDto.getNombre().isEmpty())
			{
				log.error(Constants.ERR_CATEGORIA_NOMBRE_NULO_VACIO);
				throw new CalendarioException(Constants.ERR_CATEGORIA_CODE, Constants.ERR_CATEGORIA_NOMBRE_NULO_VACIO);
			}

			if (categoriaRepository.existsById(categoriaRequestDto.getNombre()))
			{
				log.error(Constants.ERR_CATEGORIA_EXISTE);
				throw new CalendarioException(Constants.ERR_CATEGORIA_CODE, Constants.ERR_CATEGORIA_EXISTE);
			}

			Categoria categoria = new Categoria();
			categoria.setNombre(categoriaRequestDto.getNombre());
			categoria.setColor(categoriaRequestDto.getColor());

			categoriaRepository.saveAndFlush(categoria);
			log.info(Constants.ELEMENTO_AGREGADO);
			return ResponseEntity.ok().body(Constants.ELEMENTO_AGREGADO);
		} 
		catch (CalendarioException exception)
		{
			return ResponseEntity.badRequest().body(exception.getBodyExceptionMessage());
		}
   	 	catch (Exception exception)
        {
    		CalendarioException calendarioException= new CalendarioException(Constants.ERR_SERVIDOR_CODE,Constants.ERR_SERVIDOR);
            return ResponseEntity.status(500).body(calendarioException.getBodyExceptionMessage());
    		
        }
	}

	@PutMapping(value = "/", consumes = "application/json")
	public ResponseEntity<?> modificarCategoria(@RequestBody CategoriaRequestDto categoriaRequestDto)
	{
		try
		{
			Optional<Categoria> optionalCategoria = categoriaRepository
					.findById(categoriaRequestDto.getNombre());
			if (!optionalCategoria.isPresent())
			{
				log.error(Constants.ERR_CATEGORIA_NO_EXISTE);
				throw new CalendarioException(Constants.ERR_CATEGORIA_CODE, Constants.ERR_CATEGORIA_NO_EXISTE);
			}

			Categoria categoria = optionalCategoria.get();
			categoria.setNombre(categoriaRequestDto.getNombre());
			categoria.setColor(categoriaRequestDto.getColor());

			categoriaRepository.saveAndFlush(categoria);
			log.info(Constants.ELEMENTO_MODIFICADO);
			return ResponseEntity.ok().body(Constants.ELEMENTO_MODIFICADO);
		} 
		catch (CalendarioException exception)
		{
			return ResponseEntity.badRequest().body(exception.getBodyExceptionMessage());
		}
   	 	catch (Exception exception)
        {
    		CalendarioException calendarioException= new CalendarioException(Constants.ERR_SERVIDOR_CODE,Constants.ERR_SERVIDOR);
            return ResponseEntity.status(500).body(calendarioException.getBodyExceptionMessage());
    		
        }
	}

	@DeleteMapping(value = "/{nombre}")
	public ResponseEntity<?> eliminarCategoria(@PathVariable String nombre)
	{
		try
		{
			if (!categoriaRepository.existsById(nombre))
			{
				log.error(Constants.ERR_CATEGORIA_NO_EXISTE);
				throw new CalendarioException(Constants.ERR_CATEGORIA_CODE, Constants.ERR_CATEGORIA_NO_EXISTE);
			}

			categoriaRepository.deleteById(nombre);
			log.info(Constants.ELEMENTO_ELIMINADO);
			return ResponseEntity.ok().body(Constants.ELEMENTO_ELIMINADO);
		} 
		catch (CalendarioException exception)
		{
			return ResponseEntity.badRequest().body(exception.getBodyExceptionMessage());
		}
   	 	catch (Exception exception)
        {
    		CalendarioException calendarioException= new CalendarioException(Constants.ERR_SERVIDOR_CODE,Constants.ERR_SERVIDOR);
            return ResponseEntity.status(500).body(calendarioException.getBodyExceptionMessage());
    		
        }
	}

	@GetMapping(value = "/")
	public ResponseEntity<?> obtenerCategorias()
	{
		List<CategoriaResponseDto> categorias = categoriaRepository.buscarCategorias();
		return ResponseEntity.ok(categorias);
	}

	@GetMapping(value = "/{nombre}")
	public ResponseEntity<?> obtenerCategoria(@PathVariable String nombre)
	{
		try
		{
			if (nombre == null || nombre.trim().isEmpty())
			{
				log.error(Constants.ERR_CATEGORIA_NOMBRE_NULO_VACIO);
				throw new CalendarioException(Constants.ERR_CATEGORIA_CODE, Constants.ERR_CATEGORIA_NOMBRE_NULO_VACIO);
			}

			Optional<Categoria> categoriaOpt = categoriaRepository.findById(nombre.trim());
			if (!categoriaOpt.isPresent())
			{
				log.error(Constants.ERR_CATEGORIA_NO_EXISTE);
				throw new CalendarioException(Constants.ERR_CATEGORIA_CODE, Constants.ERR_CATEGORIA_NO_EXISTE);
			}

			Categoria categoria = categoriaOpt.get();
			CategoriaResponseDto dto = new CategoriaResponseDto(categoria.getNombre(),
					categoria.getColor());

			log.info(Constants.ELEMENTO_MOSTRADO);
			return ResponseEntity.ok(dto);

		} 
		catch (CalendarioException exception)
		{
			return ResponseEntity.badRequest().body(exception.getBodyExceptionMessage());
		}
   	 	catch (Exception exception)
        {
    		CalendarioException calendarioException= new CalendarioException(Constants.ERR_SERVIDOR_CODE,Constants.ERR_SERVIDOR);
            return ResponseEntity.status(500).body(calendarioException.getBodyExceptionMessage());
    		
        }
	}
}
