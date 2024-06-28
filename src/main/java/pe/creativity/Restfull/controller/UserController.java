package pe.creativity.Restfull.controller;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import pe.creativity.Restfull.dto.LoginDto;
import pe.creativity.Restfull.entity.User;
import pe.creativity.Restfull.service.UsuarioServiceImpl;
import pe.creativity.Restfull.util.InvalidCredentialsException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Log LOGGER = LogFactory.getLog(UserController.class);

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto) {
        try {
            String token = usuarioService.signin(loginDto.getUsername(), loginDto.getPassword()).orElseThrow(() ->
                    //new HttpServerErrorException(HttpStatus.FORBIDDEN, "Ingreso Fallido"));
                    new InvalidCredentialsException("Credenciales inválidas por username o password"));
            return ResponseEntity.ok(token);
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/signout")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public User signout(@RequestBody @Valid LoginDto loginDto) {
        return usuarioService.signout(loginDto.getUsername(), loginDto.getPassword(),
                loginDto.getFirstname(), loginDto.getLastname()).orElseThrow(() ->
                new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Usuario ya existe"));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getAllUsers(){
    return usuarioService.getAll();
    }

}
