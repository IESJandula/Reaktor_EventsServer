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

import es.iesjandula.proyecto_calendario.dto.UsuarioRequestDto;
import es.iesjandula.proyecto_calendario.dto.UsuarioResponseDto;
import es.iesjandula.proyecto_calendario.models.Usuario;
import es.iesjandula.proyecto_calendario.repository.IUsuarioRepository;
import es.iesjandula.proyecto_calendario.utils.CalendarioException;
import es.iesjandula.proyecto_calendario.utils.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/usuario")
@RestController
public class UsuarioRestController
{
    @Autowired
    private IUsuarioRepository usuarioRepository;

    @PostMapping(value = "/", consumes = "application/json")
    public ResponseEntity<?> crearUsuario(@RequestBody UsuarioRequestDto usuarioRequestDto) {
        try {
            if (usuarioRequestDto.getEmail() == null || usuarioRequestDto.getEmail().isEmpty()) {
                log.error(Constants.ERR_USUARIO_CORREO_NULO_VACIO);
                throw new CalendarioException(Constants.ERR_USUARIO_CODE, Constants.ERR_USUARIO_CORREO_NULO_VACIO);
            }

            if (usuarioRepository.existsById(usuarioRequestDto.getEmail())) {
                log.error(Constants.ERR_USUARIO_EXISTE);
                throw new CalendarioException(Constants.ERR_USUARIO_CODE, Constants.ERR_USUARIO_EXISTE);
            }

            Usuario usuario = new Usuario();
            usuario.setNombre(usuarioRequestDto.getNombre());
            usuario.setEmail(usuarioRequestDto.getEmail());

            usuarioRepository.saveAndFlush(usuario);
            log.info(Constants.ELEMENTO_AGREGADO);
            return ResponseEntity.ok().build();
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
    public ResponseEntity<?> modificarUsuario(@RequestBody UsuarioRequestDto usuarioRequestDto)
    {
        try
        {
            if (usuarioRequestDto.getEmail() == null || usuarioRequestDto.getEmail().isEmpty())
            {
                log.error(Constants.ERR_USUARIO_CORREO_NULO_VACIO);
                throw new CalendarioException(Constants.ERR_USUARIO_CODE, Constants.ERR_USUARIO_CORREO_NULO_VACIO);
            }

            Optional<Usuario> optionalUsuario = usuarioRepository.findById(usuarioRequestDto.getEmail());
            if (!optionalUsuario.isPresent())
            {
                log.error(Constants.ERR_USUARIO_NO_EXISTE);
                throw new CalendarioException(Constants.ERR_USUARIO_CODE, Constants.ERR_USUARIO_NO_EXISTE);
            }

            Usuario usuario = optionalUsuario.get();
            usuario.setEmail(usuarioRequestDto.getEmail());
            usuario.setNombre(usuarioRequestDto.getNombre());

            usuarioRepository.saveAndFlush(usuario);
            log.info(Constants.ELEMENTO_MODIFICADO);
            return ResponseEntity.ok().build();
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
    @DeleteMapping(value="/{id}")
    public ResponseEntity<?> borrarUsuario(@PathVariable("id") String id)
    {
        try
        {
            if (!usuarioRepository.existsById(id))
            {
                log.error(Constants.ERR_USUARIO_NO_EXISTE);
                throw new CalendarioException(Constants.ERR_USUARIO_CODE, Constants.ERR_USUARIO_NO_EXISTE);
            }

            usuarioRepository.deleteById(id);
            log.info(Constants.ELEMENTO_ELIMINADO);
            return ResponseEntity.ok().build();
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

    @GetMapping(value="/")
    public ResponseEntity<?> obtenerUsuarios()
    {
    	List<UsuarioResponseDto> usuarios = usuarioRepository.buscarUsuarios();
        return ResponseEntity.ok(usuarios);
    }
}

