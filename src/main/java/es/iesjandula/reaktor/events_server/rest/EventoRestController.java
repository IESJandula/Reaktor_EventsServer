package es.iesjandula.reaktor.events_server.rest;

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

import es.iesjandula.reaktor.base.security.models.DtoUsuarioExtended;
import es.iesjandula.reaktor.base.utils.BaseConstants;
import es.iesjandula.reaktor.events_server.dto.EventoRequestDto;
import es.iesjandula.reaktor.events_server.dto.EventoResponseDto;
import es.iesjandula.reaktor.events_server.models.Categoria;
import es.iesjandula.reaktor.events_server.models.Evento;
import es.iesjandula.reaktor.events_server.models.Usuario;
import es.iesjandula.reaktor.events_server.models.ids.EventoId;
import es.iesjandula.reaktor.events_server.repository.ICategoriaRepository;
import es.iesjandula.reaktor.events_server.repository.IEventoRepository;
import es.iesjandula.reaktor.events_server.repository.IUsuarioRepository;
import es.iesjandula.reaktor.events_server.utils.EventsServerException;
import es.iesjandula.reaktor.events_server.utils.Constants;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para la gestión de eventos.
 * 
 * <p>Permite crear, eliminar y consultar eventos dentro del sistema.
 * Incluye seguridad basada en roles usando @PreAuthorize.</p>
 */
@Slf4j
@RequestMapping("/events/manager")
@RestController
public class EventoRestController
{
	//Repositorio para manejar la entidad Evento
    @Autowired
    private IEventoRepository eventoRepository;

    //Repositorio para manejar la entidad Usuario
    @Autowired
    private IUsuarioRepository usuarioRepository;

    //Repositorio para manejar la entidad Categoria
    @Autowired
    private ICategoriaRepository categoriaRepository;
    
    /**
     * Endpoint para crear un nuevo evento.
     * 
     * <p>Verifica que el título no sea nulo ni vacío y que no exista un evento con la misma clave compuesta.
     * También crea el usuario si no existe y valida que la categoría exista.</p>
     * 
     * @param usuario Usuario autenticado (obtenido desde Spring Security)
     * @param eventoRequestDto DTO con los datos del evento
     * @return ResponseEntity con mensaje de éxito o error
     */
    @PreAuthorize("hasAnyRole('" + BaseConstants.ROLE_PROFESOR + "')")
    @PostMapping(value = "/", consumes = "application/json")
    public ResponseEntity<?> crearEvento(@AuthenticationPrincipal DtoUsuarioExtended usuario, @RequestBody EventoRequestDto eventoRequestDto)
    {
        try
        {
        	 //Validamos que el titulo no venga nulo o vacio
            if (eventoRequestDto.getTitulo() == null || eventoRequestDto.getTitulo().isEmpty())
            {
                log.error(Constants.ERR_EVENTO_TITULO_NULO_VACIO);
                throw new EventsServerException(Constants.ERR_EVENTO_CODE, Constants.ERR_EVENTO_TITULO_NULO_VACIO);
            }
            
            // Hacemos la conversión de Long a Date para su registro.
        	Date fechaInicio = toDate(eventoRequestDto.getFechaInicio());
            Date fechaFin = toDate(eventoRequestDto.getFechaFin()) ;
            
            //Recogemos los atributos principales del Evento
            EventoId eventoId = new EventoId(eventoRequestDto.getTitulo(), fechaInicio,fechaFin);

            // Comprobamos si ya existe un evento con el mismo ID compuesto en la base de datos
            if (this.eventoRepository.existsById(eventoId))
            {
                log.error(Constants.ERR_EVENTO_EXISTE);
                throw new EventsServerException(Constants.ERR_EVENTO_CODE, Constants.ERR_EVENTO_EXISTE);
            }
            // Creamos variable de usuario
    		Usuario usuarioDatabase = null;

    		// Buscamos si existe el usuario, sino lo creamos
    		Optional<Usuario> usuarioDatabaseOptional = this.usuarioRepository.findById(usuario.getEmail()) ;

            // Comprobamos si el usuario no existe en la base de datos
    		if (usuarioDatabaseOptional.isEmpty())
    		{
    			// Creamos una nueva instancia de usuario
    			usuarioDatabase = new Usuario() ;

    			// Seteamos los atributos del usuario
    			usuarioDatabase.setEmail(usuario.getEmail());
    			usuarioDatabase.setNombre(usuario.getNombre());


    			// Guardamos el usuario en la base de datos
    			this.usuarioRepository.saveAndFlush(usuarioDatabase);
    		}
    		else
    		{
                // Si ya existe, recuperamos el usuario de la base de datos
    			usuarioDatabase = usuarioDatabaseOptional.get();
    		}

    		
            Optional<Categoria> categoriaOpt = this.categoriaRepository.findById(eventoRequestDto.getNombre());

            // Comprobamos si la categoría asociada al evento existe en la base de datos
            if (!categoriaOpt.isPresent())
            {
                log.error(Constants.ERR_CATEGORIA_NO_EXISTE);
                throw new EventsServerException(Constants.ERR_CATEGORIA_CODE, Constants.ERR_CATEGORIA_NO_EXISTE);
            }


            Evento evento = new Evento();
            evento.setEventoId(eventoId);
            evento.setUsuario(usuarioDatabase);
            evento.setCategoria(categoriaOpt.get());

            this.eventoRepository.saveAndFlush(evento);
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
     * Endpoint para eliminar un evento por su ID compuesto (título y fechas).
     * 
     * @param titulo Título del evento
     * @param fechaInicio Fecha de inicio en milisegundos
     * @param fechaFin Fecha de fin en milisegundos
     * @return ResponseEntity con mensaje de éxito o error
     */
    @PreAuthorize("hasAnyRole('" + BaseConstants.ROLE_PROFESOR + "')")
    @DeleteMapping(value="/")
    public ResponseEntity<?> eliminarEvento(@RequestHeader String titulo,@RequestHeader Long fechaInicio,@RequestHeader Long fechaFin)
    {
        try
        {
        	Date fechInicio = toDate(fechaInicio);
            Date fechFin = toDate(fechaFin) ;
            
            EventoId eventoId = new EventoId(titulo,fechInicio,fechFin);

            // Comprobamos si el evento que se desea eliminar NO existe en la base de datos
            if (!this.eventoRepository.existsById(eventoId))
            {
                log.error(Constants.ERR_EVENTO_NO_EXISTE);
                throw new EventsServerException(Constants.ERR_EVENTO_CODE, Constants.ERR_EVENTO_NO_EXISTE);
            }

            this.eventoRepository.deleteById(eventoId);
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
     * Endpoint para obtener todos los eventos.
     * 
     * @return ResponseEntity con la lista de eventos
     */
    @PreAuthorize("hasAnyRole('" + BaseConstants.ROLE_PROFESOR + "')")
    @GetMapping(value="/")
    public ResponseEntity<?> obtenerEventos()
    {
    	List<EventoResponseDto> eventos = this.eventoRepository.buscarEventos();
        return ResponseEntity.ok(eventos);
    }
    /**
     * Endpoint para obtener un evento específico por su ID compuesto.
     * 
     * @param titulo Título del evento
     * @param fechaInicio Fecha de inicio en milisegundos
     * @param fechaFin Fecha de fin en milisegundos
     * @return ResponseEntity con el evento encontrado
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

            // Comprobamos si el evento buscado no existe en la base de datos
            if (!eventoOpt.isPresent())
            {
                log.error(Constants.ERR_EVENTO_NO_EXISTE);
                throw new EventsServerException(Constants.ERR_EVENTO_CODE, Constants.ERR_EVENTO_NO_EXISTE);
            }
       	 

            Evento evento = eventoOpt.get(); 
            
            EventoResponseDto eventoResponseDto = new EventoResponseDto();
            eventoResponseDto.setTitulo(evento.getEventoId().getTitulo());
            eventoResponseDto.setFechaInicio(evento.getEventoId().getFechaInicio());
            eventoResponseDto.setFechaFin(evento.getEventoId().getFechaFin());
            return ResponseEntity.ok(eventoResponseDto);

        } catch (EventsServerException exception) {
            
        	return ResponseEntity.badRequest().body(exception.getBodyExceptionMessage());
        }
   	 	catch (Exception exception)
        {
    		EventsServerException calendarioException= new EventsServerException(Constants.ERR_SERVIDOR_CODE,Constants.ERR_SERVIDOR);
            return ResponseEntity.status(500).body(calendarioException.getBodyExceptionMessage());
    		
        }
    }
    
    /**
     * Endpoint para obtener todos los eventos de un usuario específico.
     * 
     * @param usuario Usuario autenticado
     * @return ResponseEntity con la lista de eventos del usuario
     */
    @PreAuthorize("hasAnyRole('" + BaseConstants.ROLE_PROFESOR + "')")
    @GetMapping("/{correoUsuario}")
    public ResponseEntity<?> obtenerEventosPorUsuario(@AuthenticationPrincipal DtoUsuarioExtended usuario)
    {
    	try
        {
    		Usuario usuarioDatabase = null;

    		// Buscamos si existe el usuario, sino lo creamos
    		Optional<Usuario> usuarioDatabaseOptional = this.usuarioRepository.findById(usuario.getEmail()) ;

            // Comprobamos si el usuario introducido por parámetro es nulo o no existe
            if (usuario.getEmail() == null || usuario.getEmail().isEmpty())
            {
                //Si no existe procedemos a crearlo
            	usuarioDatabase = new Usuario() ;

    			// Seteamos los atributos del usuario
    			usuarioDatabase.setEmail(usuario.getEmail());
    			usuarioDatabase.setNombre(usuario.getNombre());


    			// Guardamos el usuario en la base de datos
    			this.usuarioRepository.saveAndFlush(usuarioDatabase);
    		}
    		else
    		{
    			// Obtenemos el usuario de la base de datos
    			usuarioDatabase = usuarioDatabaseOptional.get();
    		}
            
            List<EventoResponseDto> eventosDto = this.eventoRepository.buscarEventosPorUsuario(usuario.getEmail());

            // Comprobamos si el usuario no tiene eventos asociados
            if (eventosDto == null || eventosDto.isEmpty())
            {
                log.error(Constants.ERR_EVENTO_NO_EXISTE);
                throw new EventsServerException(Constants.ERR_EVENTO_CODE, Constants.ERR_EVENTO_NO_EXISTE);
            }

            return ResponseEntity.ok(eventosDto);
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
     * Método auxiliar para convertir un Long (milisegundos) a Date.
     * 
     * @param fecha Fecha en milisegundos
     * @return Objeto Date correspondiente
     * @throws EventsServerException Si la fecha es nula o menor/igual a 0
     */
    private Date toDate(Long fecha) throws EventsServerException
    {
        // Comprobamos que la fecha no sea nula ni menor o igual que 0
    	if(fecha == null || fecha <= 0)
    	{
    		log.error(Constants.ERR_EVENTO_FECHAS_INVALIDAS);
            throw new EventsServerException(Constants.ERR_EVENTO_FECHAS_INVALIDAS_CODE, Constants.ERR_EVENTO_FECHAS_INVALIDAS);
    	}
    	return new Date(fecha);
    }

}




