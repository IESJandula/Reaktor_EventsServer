package es.iesjandula.proyecto_calendario.rest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.iesjandula.proyecto_calendario.dto.EventoRequestDto;
import es.iesjandula.proyecto_calendario.dto.EventoResponseDto;
import es.iesjandula.proyecto_calendario.models.Categoria;
import es.iesjandula.proyecto_calendario.models.Evento;
import es.iesjandula.proyecto_calendario.models.Usuario;
import es.iesjandula.proyecto_calendario.models.ids.EventoId;
import es.iesjandula.proyecto_calendario.repository.ICategoriaRepository;
import es.iesjandula.proyecto_calendario.repository.IEventoRepository;
import es.iesjandula.proyecto_calendario.repository.IUsuarioRepository;
import es.iesjandula.proyecto_calendario.utils.CalendarioException;
import es.iesjandula.proyecto_calendario.utils.Constants;
import es.iesjandula.reaktor.base.security.models.DtoUsuarioExtended;
import es.iesjandula.reaktor.base.utils.BaseConstants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/events/manager")
@RestController
public class EventoRestController
{
    @Autowired
    private IEventoRepository eventoRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private ICategoriaRepository categoriaRepository;
    
    @PreAuthorize("hasAnyRole('" + BaseConstants.ROLE_PROFESOR + "')")
    @PostMapping(value = "/", consumes = "application/json")
    public ResponseEntity<?> crearEvento(@AuthenticationPrincipal DtoUsuarioExtended usuario, @RequestBody EventoRequestDto eventoRequestDto)
    {
        try
        {
 
            if (eventoRequestDto.getTitulo() == null || eventoRequestDto.getTitulo().isEmpty())
            {
                log.error(Constants.ERR_EVENTO_TITULO_NULO_VACIO);
                throw new CalendarioException(Constants.ERR_EVENTO_CODE, Constants.ERR_EVENTO_TITULO_NULO_VACIO);
            }
            
        	Date fechaInicio = toDate(eventoRequestDto.getFechaInicio());
            Date fechaFin = toDate(eventoRequestDto.getFechaFin()) ;
            
            EventoId eventoId = new EventoId(eventoRequestDto.getTitulo(), fechaInicio,fechaFin);

            if (eventoRepository.existsById(eventoId))
            {
                log.error(Constants.ERR_EVENTO_EXISTE);
                throw new CalendarioException(Constants.ERR_EVENTO_CODE, Constants.ERR_EVENTO_EXISTE);
            }
         // Creamos variable de usuario
    		Usuario usuarioDatabase = null;

    		// Buscamos si existe el usuario, sino lo creamos
    		Optional<Usuario> usuarioDatabaseOptional = this.usuarioRepository.findById(usuario.getEmail()) ;

    		// Si no existe el usuario ...
    		if (usuarioDatabaseOptional.isEmpty())
    		{
    			// ... creamos una nueva instancia de usuario
    			usuarioDatabase = new Usuario() ;

    			// Seteamos los atributos del usuario
    			usuarioDatabase.setEmail(usuario.getEmail());
    			usuarioDatabase.setNombre(usuario.getNombre());


    			// Guardamos el usuario en la base de datos
    			this.usuarioRepository.saveAndFlush(usuarioDatabase);
    		}
    		else
    		{
    			// ... obtenemos el usuario de la base de datos
    			usuarioDatabase = usuarioDatabaseOptional.get();
    		}


            Optional<Categoria> categoriaOpt = this.categoriaRepository.findById(eventoRequestDto.getNombre());
            if (!categoriaOpt.isPresent())
            {
                log.error(Constants.ERR_CATEGORIA_NO_EXISTE);
                throw new CalendarioException(Constants.ERR_CATEGORIA_CODE, Constants.ERR_CATEGORIA_NO_EXISTE);
            }
            Categoria categoria = categoriaOpt.get();

            Evento evento = new Evento();
            evento.setEventoId(eventoId);
            evento.setUsuario(usuarioDatabase);
            evento.setCategoria(categoriaOpt.get());

            eventoRepository.saveAndFlush(evento);
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

    @PreAuthorize("hasAnyRole('" + BaseConstants.ROLE_PROFESOR + "')")
    @DeleteMapping(value="/")
    public ResponseEntity<?> eliminarEvento(@RequestHeader String titulo,@RequestHeader Long fechaInicio,@RequestHeader Long fechaFin)
    {
        try
        {
        	Date fechInicio = toDate(fechaInicio);
            Date fechFin = toDate(fechaFin) ;
            
            EventoId eventoId = new EventoId(titulo,fechInicio,fechFin);
            
            if (!eventoRepository.existsById(eventoId))
            {
                log.error(Constants.ERR_EVENTO_NO_EXISTE);
                throw new CalendarioException(Constants.ERR_EVENTO_CODE, Constants.ERR_EVENTO_NO_EXISTE);
            }

            eventoRepository.deleteById(eventoId);
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

    @PreAuthorize("hasAnyRole('" + BaseConstants.ROLE_PROFESOR + "')")
    @GetMapping(value="/")
    public ResponseEntity<?> obtenerEventos()
    {
    	List<EventoResponseDto> eventos = eventoRepository.buscarEventos();
        return ResponseEntity.ok(eventos);
    }
    /**
     * endpoint para obtener un evento a traves de su id
     */
    @PreAuthorize("hasAnyRole('" + BaseConstants.ROLE_ADMINISTRADOR + "+"+BaseConstants.ROLE_DIRECCION +"')"+ "+"+BaseConstants.ROLE_PROFESOR+"')")
    @GetMapping("/filtro")
    public ResponseEntity<?> obtenerEventoPorId(@RequestHeader String titulo,@RequestHeader Long fechaInicio,@RequestHeader Long fechaFin)
    {
    	
        try
        {
        	Date fechInicio = toDate(fechaInicio);
            Date fechFin = toDate(fechaFin) ;
            
            EventoId eventoId = new EventoId(titulo,fechInicio,fechFin);
            Optional<Evento> eventoOpt = this.eventoRepository.findById(eventoId);

            if (!eventoOpt.isPresent())
            {
                log.error(Constants.ERR_EVENTO_NO_EXISTE);
                throw new CalendarioException(Constants.ERR_EVENTO_CODE, Constants.ERR_EVENTO_NO_EXISTE);
            }
       	 

            Evento evento = eventoOpt.get(); 
            
            EventoResponseDto eventoResponseDto = new EventoResponseDto();
            eventoResponseDto.setTitulo(evento.getEventoId().getTitulo());
            eventoResponseDto.setFechaInicio(evento.getEventoId().getFechaInicio());
            eventoResponseDto.setFechaFin(evento.getEventoId().getFechaFin());
            return ResponseEntity.ok(eventoResponseDto);

        } catch (CalendarioException exception) {
            
        	return ResponseEntity.badRequest().body(exception.getBodyExceptionMessage());
        }
   	 	catch (Exception exception)
        {
    		CalendarioException calendarioException= new CalendarioException(Constants.ERR_SERVIDOR_CODE,Constants.ERR_SERVIDOR);
            return ResponseEntity.status(500).body(calendarioException.getBodyExceptionMessage());
    		
        }
    }
    @PreAuthorize("hasAnyRole('" + BaseConstants.ROLE_PROFESOR + "')")
    @GetMapping("/{correoUsuario}")
    public ResponseEntity<?> obtenerEventosPorUsuario(@AuthenticationPrincipal DtoUsuarioExtended usuario)
    {
    	try
        {
    		Usuario usuarioDatabase = null;

    		// Buscamos si existe el usuario, sino lo creamos
    		Optional<Usuario> usuarioDatabaseOptional = this.usuarioRepository.findById(usuario.getEmail()) ;

            if (usuario.getEmail() == null || usuario.getEmail().isEmpty())
            {
            	usuarioDatabase = new Usuario() ;

    			// Seteamos los atributos del usuario
    			usuarioDatabase.setEmail(usuario.getEmail());
    			usuarioDatabase.setNombre(usuario.getNombre());


    			// Guardamos el usuario en la base de datos
    			this.usuarioRepository.saveAndFlush(usuarioDatabase);
    		}
    		else
    		{
    			// ... obtenemos el usuario de la base de datos
    			usuarioDatabase = usuarioDatabaseOptional.get();
    		}
            
            List<EventoResponseDto> eventosDto = this.eventoRepository.buscarEventosPorUsuario(usuario.getEmail());
            
            if (eventosDto == null || eventosDto.isEmpty())
            {
                log.error(Constants.ERR_EVENTO_NO_EXISTE);
                throw new CalendarioException(Constants.ERR_EVENTO_CODE, Constants.ERR_EVENTO_NO_EXISTE);
            }

            return ResponseEntity.ok(eventosDto);
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
    
    private Date toDate(Long fecha) throws CalendarioException
    {
    	if(fecha == null || fecha <= 0)
    	{
    		log.error(Constants.ERR_EVENTO_FECHAS_INVALIDAS);
            throw new CalendarioException(Constants.ERR_EVENTO_FECHAS_INVALIDAS_CODE, Constants.ERR_EVENTO_FECHAS_INVALIDAS);
    	}
    	return new Date(fecha);
    }

}




