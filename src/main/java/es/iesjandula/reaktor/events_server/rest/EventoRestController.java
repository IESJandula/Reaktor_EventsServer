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
import es.iesjandula.reaktor.events_server.models.ids.EventoId;
import es.iesjandula.reaktor.events_server.repository.ICategoriaRepository;
import es.iesjandula.reaktor.events_server.repository.IEventoRepository;
import es.iesjandula.reaktor.events_server.utils.Constants;
import es.iesjandula.reaktor.events_server.utils.EventsServerException;
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
    private IEventoRepository eventoRepository ;

    //Repositorio para manejar la entidad Categoria
    @Autowired
    private ICategoriaRepository categoriaRepository ;
    
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
        	// Validamos los datos de entrada
            validarCrearEvento( eventoRequestDto.getTitulo(), eventoRequestDto.getFechaInicio(), eventoRequestDto.getFechaFin(), eventoRequestDto.getNombre()) ;

            
            // Hacemos la conversión de Long a Date para su registro.
        	Date fechaInicio = toDate(eventoRequestDto.getFechaInicio()) ;
            Date fechaFin = toDate(eventoRequestDto.getFechaFin()) ;
            
            
            //Recogemos los atributos principales del Evento
            EventoId eventoId = new EventoId(eventoRequestDto.getTitulo(), fechaInicio,fechaFin) ;

            // Comprobamos si ya existe un evento con el mismo ID compuesto en la base de datos
            if (this.eventoRepository.existsById(eventoId))
            {
                log.error(Constants.ERR_EVENTO_EXISTE) ;
                throw new EventsServerException(Constants.ERR_EVENTO_CODE, Constants.ERR_EVENTO_EXISTE) ;
            }
           
            Optional<Categoria> categoriaOpt = this.categoriaRepository.findById(eventoRequestDto.getNombre()) ;

            // Comprobamos si la categoría asociada al evento existe en la base de datos
            if (!categoriaOpt.isPresent())
            {
                log.error(Constants.ERR_CATEGORIA_NO_EXISTE);
                throw new EventsServerException(Constants.ERR_CATEGORIA_CODE, Constants.ERR_CATEGORIA_NO_EXISTE) ;
            }

            Evento evento = new Evento() ;
            evento.setEventoId(eventoId) ;
            evento.setCategoria(categoriaOpt.get()) ;

            this.eventoRepository.saveAndFlush(evento) ;
            log.info(Constants.ELEMENTO_AGREGADO) ;
            return ResponseEntity.ok().body(Constants.ELEMENTO_AGREGADO) ;
        }
        catch (EventsServerException exception)
        {
            return ResponseEntity.badRequest().body(exception.getBodyExceptionMessage()) ;
        }
   	 	catch (Exception exception)
        {
    		EventsServerException calendarioException= new EventsServerException(Constants.ERR_SERVIDOR_CODE,Constants.ERR_SERVIDOR) ;
            return ResponseEntity.status(500).body(calendarioException.getBodyExceptionMessage()) ;
    		
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
    public ResponseEntity<?> eliminarEvento(@AuthenticationPrincipal DtoUsuarioExtended usuario,@RequestHeader String titulo,@RequestHeader Long fechaInicio,@RequestHeader Long fechaFin)
    {
        try
        {
        	validarEliminarEvento(titulo, fechaInicio, fechaFin);

        	// Hacemos la conversión de Long a Date para su registro.
        	Date fechaInicioDate = toDate(fechaInicio) ;
            Date fechaFinDate = toDate(fechaFin) ;
            
            // Montamos el evento 
            EventoId eventoId = new EventoId(titulo, fechaInicioDate, fechaFinDate) ;
            
            //Buscar evento
            Optional<Evento> optionalEvento = eventoRepository.findById(eventoId);
            // Comprobamos si el evento que se desea eliminar NO existe en la base de datos
            if (!optionalEvento.isPresent())
            {
                log.error(Constants.ERR_EVENTO_NO_EXISTE);
                throw new EventsServerException(Constants.ERR_EVENTO_CODE, Constants.ERR_EVENTO_NO_EXISTE);
            }
            Evento evento = optionalEvento.get();

            // Control de permisos
            // ADMIN puede borrar cualquier evento
            // PROFESOR solo los suyos
            if (!usuario.getRoles().contains(BaseConstants.ROLE_ADMINISTRADOR)
                    && !usuario.getEmail().equals(evento.getUsuario().getEmail()))
            {
                log.error(Constants.ERR_EVENTO_USUARIO_NO_PERMITIDO_DESC);
                throw new EventsServerException( Constants.ERR_EVENTO_USUARIO_NO_PERMITIDO_CODE, Constants.ERR_EVENTO_USUARIO_NO_PERMITIDO_DESC) ;
            }

            //Eliminar evento
            eventoRepository.delete(evento);

            log.info(Constants.ELEMENTO_ELIMINADO, eventoId);
            return ResponseEntity.ok().build() ;
        }
        catch (EventsServerException exception)
        {
            return ResponseEntity.badRequest().body(exception.getBodyExceptionMessage()) ;
        }
   	 	catch (Exception exception)
        {
    		EventsServerException calendarioException= new EventsServerException(Constants.ERR_SERVIDOR_CODE,Constants.ERR_SERVIDOR) ;
            return ResponseEntity.status(500).body(calendarioException.getBodyExceptionMessage()) ;
    		
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
    	try
    	{
    	List<EventoResponseDto> eventos = this.eventoRepository.buscarEventos() ;
        return ResponseEntity.ok(eventos) ;
    	}
	 	catch (Exception exception)
    	{
		EventsServerException calendarioException= new EventsServerException(Constants.ERR_SERVIDOR_CODE,Constants.ERR_SERVIDOR) ;
        return ResponseEntity.status(500).body(calendarioException.getBodyExceptionMessage()) ;	
    	}
    }
    /**
     * Endpoint para obtener un evento específico por su ID compuesto.
     * 
     * @param titulo Título del evento
     * @param fechaInicio Fecha de inicio en milisegundos
     * @param fechaFin Fecha de fin en milisegundos
     * @return ResponseEntity con el evento encontrado
     */
    @PreAuthorize("hasAnyRole('"+BaseConstants.ROLE_PROFESOR+"')")
    @GetMapping("/filtro")
    public ResponseEntity<?> obtenerEventoPorId(@AuthenticationPrincipal DtoUsuarioExtended usuario, @RequestHeader String titulo,@RequestHeader Long fechaInicio,@RequestHeader Long fechaFin)
    {
    	
        try
        {
        	// Validaciones básicas
            validarObtenerEvento(titulo, fechaInicio, fechaFin);
            
        	// Hacemos la conversión de Long a Date para su registro.
        	Date fechaInicioDate = toDate(fechaInicio) ;
            Date fechaFinDate = toDate(fechaFin) ;
            
            // Montamos el evento
            EventoId eventoId = new EventoId( titulo, fechaInicioDate, fechaFinDate) ;
            
            //Buscamos el evento
            Optional<Evento> optionalEvento = eventoRepository.findById(eventoId) ;

            // Comprobamos si el evento buscado no existe en la base de datos
            if (!optionalEvento.isPresent())
            {
                log.error(Constants.ERR_EVENTO_NO_EXISTE) ;
                throw new EventsServerException(Constants.ERR_EVENTO_CODE, Constants.ERR_EVENTO_NO_EXISTE) ;
            }

            Evento evento = optionalEvento.get() ; 
            
            //Control de permisos
            // ADMIN y DIRECCIÓN → cualquier evento
            // PROFESOR → solo los suyos
            if ( usuario.getRoles().contains(BaseConstants.ROLE_PROFESOR) && !usuario.getRoles().contains(BaseConstants.ROLE_ADMINISTRADOR)
                && !usuario.getRoles().contains(BaseConstants.ROLE_DIRECCION) && !usuario.getEmail().equals(evento.getUsuario().getEmail()))
            {
                log.error(Constants.ERR_EVENTO_USUARIO_NO_PERMITIDO_DESC);
                throw new EventsServerException( Constants.ERR_EVENTO_USUARIO_NO_PERMITIDO_CODE, Constants.ERR_EVENTO_USUARIO_NO_PERMITIDO_DESC) ;
            }
            
            //Conversion del ResponseDto (Date → Long)
            EventoResponseDto eventoResponseDto = new EventoResponseDto() ;
            eventoResponseDto.setTitulo(evento.getEventoId().getTitulo()) ;
            eventoResponseDto.setFechaInicio(evento.getEventoId().getFechaInicio().getTime()) ;
            eventoResponseDto.setFechaFin(evento.getEventoId().getFechaFin().getTime()) ;
            return ResponseEntity.ok(eventoResponseDto) ;
        } 
        catch (EventsServerException exception)  
        { 
        	return ResponseEntity.badRequest().body(exception.getBodyExceptionMessage()) ;
        }
   	 	catch (Exception exception)
        {
    		EventsServerException calendarioException= new EventsServerException(Constants.ERR_SERVIDOR_CODE,Constants.ERR_SERVIDOR) ;
            return ResponseEntity.status(500).body(calendarioException.getBodyExceptionMessage()) ;
        }
    }
    
    /**
     * Endpoint para obtener todos los eventos de un usuario específico.
     * 
     * @param usuario Usuario autenticado
     * @return ResponseEntity con la lista de eventos del usuario
     */
    @PreAuthorize("hasAnyRole('" + BaseConstants.ROLE_PROFESOR + "')")
    @GetMapping("/{email}")
    public ResponseEntity<?> obtenerEventosPorUsuario(@AuthenticationPrincipal DtoUsuarioExtended usuario)
    {
    	try
        {
    		 // Obtenemos los eventos filtrados según el rol
            List<EventoResponseDto> eventosDto = obtenerEventosSegunRol(usuario) ;

            return ResponseEntity.ok(eventosDto) ;
        }
        catch (EventsServerException exception)
        {
            return ResponseEntity.badRequest().body(exception.getBodyExceptionMessage()) ;
        }
    	 catch (Exception exception)
        {
    		EventsServerException calendarioException= new EventsServerException(Constants.ERR_SERVIDOR_CODE,Constants.ERR_SERVIDOR) ;
            return ResponseEntity.status(500).body(calendarioException.getBodyExceptionMessage()) ;
    		
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
    		log.error(Constants.ERR_EVENTO_FECHAS_INVALIDAS) ;
            throw new EventsServerException(Constants.ERR_EVENTO_FECHAS_INVALIDAS_CODE, Constants.ERR_EVENTO_FECHAS_INVALIDAS) ;
    	}
    	return new Date(fecha) ;
    } 
    
    /**
     * Valida los datos necesarios para crear un evento.
     *
     * @param titulo        Título del evento
     * @param fechaInicio   Fecha de inicio en milisegundos
     * @param fechaFin      Fecha de fin en milisegundos
     * @param nombreCategoria Nombre de la categoría
     * @throws EventsServerException si los datos no son válidos
     */
    private void validarCrearEvento(String titulo, Long fechaInicio, Long fechaFin, String nombreCategoria) throws EventsServerException
    {
        // Validamos el título
        if (titulo == null || titulo.isEmpty())
        {
            log.error(Constants.ERR_EVENTO_TITULO_NULO_VACIO);
            throw new EventsServerException(Constants.ERR_EVENTO_TITULO_NULO_VACIO_CODE, Constants.ERR_EVENTO_TITULO_NULO_VACIO) ;
        }

        // Validamos fechas
        if (fechaInicio == null || fechaInicio <= 0 || fechaFin == null || fechaFin <= 0)
        {
            log.error(Constants.ERR_EVENTO_FECHAS_INVALIDAS);
            throw new EventsServerException( Constants.ERR_EVENTO_FECHAS_INVALIDAS_CODE, Constants.ERR_EVENTO_FECHAS_INVALIDAS ) ;
        }

        // Validamos orden de fechas
        if (fechaFin < fechaInicio)
        {
            log.error(Constants.ERR_EVENTO_FECHAS_INVALIDAS) ;
            throw new EventsServerException( Constants.ERR_EVENTO_FECHAS_INVALIDAS_CODE, Constants.ERR_EVENTO_FECHAS_INVALIDAS ) ;
        }
        // Validamos categoría
        if (nombreCategoria == null || nombreCategoria.isEmpty())
        {
            log.error(Constants.ERR_CATEGORIA_NO_EXISTE) ;
            throw new EventsServerException( Constants.ERR_CATEGORIA_CODE, Constants.ERR_CATEGORIA_NO_EXISTE) ;
        }
    }
    
    /**
     * Valida los datos necesarios para la eliminación de un evento.
     * <p>
     * Este método comprueba que el título del evento no sea nulo ni vacío y que las
     * fechas de inicio y fin sean válidas. Las fechas se reciben como valores
     * {@link Long} que representan milisegundos desde epoch y se convierten
     * internamente a {@link Date} para poder realizar las validaciones necesarias.
     * </p>
     * <p>
     * También valida que la fecha de fin no sea anterior a la fecha de inicio.
     * </p>
     * @param titulo      Título del evento a eliminar.
     * @param fechaInicio Fecha de inicio del evento en milisegundos.
     * @param fechaFin    Fecha de fin del evento en milisegundos.
     *
     * @throws EventsServerException si el título es nulo o vacío, si alguna de las fechas es nula o inválida, o si la fecha de fin
     * es anterior a la fecha de inicio.
     */
    private void validarEliminarEvento(String titulo, Long fechaInicio, Long fechaFin) throws EventsServerException
    {
        if (titulo == null || titulo.isEmpty())
        {
            log.error(Constants.ERR_EVENTO_TITULO_NULO_VACIO);
            throw new EventsServerException( Constants.ERR_EVENTO_CODE, Constants.ERR_EVENTO_TITULO_NULO_VACIO) ;
        }

        Date fechaInicioDate = toDate(fechaInicio) ;
        Date fechaFinDate = toDate(fechaFin) ;

        if (fechaFinDate.before(fechaInicioDate))
        {
            log.error(Constants.ERR_EVENTO_FECHAS_INVALIDAS);
            throw new EventsServerException( Constants.ERR_EVENTO_FECHAS_INVALIDAS_CODE, Constants.ERR_EVENTO_FECHAS_INVALIDAS) ;
        }
    }
    
    /**
     * Valida los datos necesarios para obtener un evento.
     *
     * <p>
     * Este método comprueba que:
     * <ul>
     *   <li>El título del evento no sea nulo ni vacío.</li>
     *   <li>Las fechas de inicio y fin no sean nulas ni inválidas.</li>
     *   <li>La fecha de fin no sea anterior a la fecha de inicio.</li>
     * </ul>
     * </p>
     *
     * <p>
     * Las fechas se reciben como {@link Long} (milisegundos desde epoch) y se
     * convierten internamente a {@link Date} para realizar las validaciones
     * correspondientes.
     * </p>
     *
     * @param titulo      Título del evento.
     * @param fechaInicio Fecha de inicio del evento en milisegundos.
     * @param fechaFin    Fecha de fin del evento en milisegundos.
     *
     * @throws EventsServerException si alguno de los datos es nulo, vacío
     *                               o si las fechas no son válidas.
     */
    private void validarObtenerEvento(String titulo, Long fechaInicio, Long fechaFin) throws EventsServerException
    {
        if (titulo == null || titulo.isEmpty())
        {
            log.error(Constants.ERR_EVENTO_TITULO_NULO_VACIO) ;
            throw new EventsServerException( Constants.ERR_EVENTO_CODE, Constants.ERR_EVENTO_TITULO_NULO_VACIO) ;
        }

        Date inicio = toDate(fechaInicio) ;
        Date fin    = toDate(fechaFin) ;

        if (fin.before(inicio))
        {
            log.error(Constants.ERR_EVENTO_FECHAS_INVALIDAS) ;
            throw new EventsServerException( Constants.ERR_EVENTO_FECHAS_INVALIDAS_CODE, Constants.ERR_EVENTO_FECHAS_INVALIDAS) ;
        }
    }
    
    /**
     * Método privado que decide qué eventos devolver según el rol del usuario.
     * <p>
     * Si el usuario es administrador, devuelve todos los eventos.
     * Si no, devuelve solo los eventos asociados al email del usuario.
     * </p>
     *
     * @param usuario Usuario autenticado.
     * @return Lista de eventos en formato DTO.
     * @throws EventsServerException si no existen eventos.
     */
    private List<EventoResponseDto> obtenerEventosSegunRol(DtoUsuarioExtended usuario) throws EventsServerException
    {
        List<EventoResponseDto> eventosDto ;

        if (usuario.getRoles().contains(BaseConstants.ROLE_ADMINISTRADOR))
        {
            eventosDto = this.eventoRepository.buscarEventos() ;
        }
        else
        {
            eventosDto = this.eventoRepository.buscarEventosPorUsuario(usuario.getEmail()) ;
        }

        if (eventosDto == null || eventosDto.isEmpty())
        {
            log.error(Constants.ERR_EVENTO_NO_EXISTE) ;
            throw new EventsServerException(Constants.ERR_EVENTO_CODE, Constants.ERR_EVENTO_NO_EXISTE) ;
        }

        return eventosDto;
    }
    
}




