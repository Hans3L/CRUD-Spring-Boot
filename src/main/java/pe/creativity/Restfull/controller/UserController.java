package pe.creativity.Restfull.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.creativity.Restfull.security.JwtProvider;
import pe.creativity.Restfull.dto.LoginDto;
import pe.creativity.Restfull.dto.RegisterDto;
import pe.creativity.Restfull.entity.User;
import pe.creativity.Restfull.helper.UserAlreadyExistsException;
import pe.creativity.Restfull.response.LoginResponse;
import pe.creativity.Restfull.service.UsuarioServiceImpl;
import pe.creativity.Restfull.util.InvalidCredentialsException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static pe.creativity.Restfull.util.Constants.TOKEN_PREFIX;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UsuarioServiceImpl usuarioService;
    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/signin")

    public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto) {
        try {
            String token = usuarioService.signin(loginDto.getUsername(), loginDto.getPassword()).orElseThrow(() ->
                    new InvalidCredentialsException("Credenciales inválidas por username o password"));
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(token);
            loginResponse.setExpires_in(jwtProvider.getExpirationTime());
            loginResponse.setToken_type(TOKEN_PREFIX);
            return ResponseEntity.ok(loginResponse);
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/signout")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> signout(@RequestBody @Valid RegisterDto registersDto) {
        try {
            Optional<User> user = usuarioService.signout(registersDto.getUsername(), registersDto.getPassword(),
                    registersDto.getFirstname(), registersDto.getLastname());
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar usuario: " + e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getAllUsers() {
        return usuarioService.getAll();
    }
}
