package pe.creativity.Restfull.service;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.creativity.Restfull.security.JwtProvider;
import pe.creativity.Restfull.entity.Role;
import pe.creativity.Restfull.entity.User;
import pe.creativity.Restfull.helper.UserAlreadyExistsException;
import pe.creativity.Restfull.repository.RoleRepository;
import pe.creativity.Restfull.repository.UserRepository;

import java.util.List;
import java.util.Optional;


@Service("UsuarioService")
public class UsuarioServiceImpl implements UsuarioService {

    private static final Log LOGGER = LogFactory.getLog(UsuarioServiceImpl.class);

    private UserRepository userRepository;

    private AuthenticationManager authenticationManager;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    private JwtProvider jwtProvider;


    @Autowired
    public UsuarioServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager,
                              RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }
    
    public Optional<String> signin(String username, String password) {
        LOGGER.info("Nuevo usuario que intenta ingresar ");
        Optional<String> token = Optional.empty();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
                token = Optional.of(jwtProvider.createToken(username, user.get().getRole()));
            } catch (AuthenticationException e) {
                LOGGER.info("Fallo el ingreso para el usuario {}", e);
                LOGGER.debug(username);
            }
        }
        return token;
    }

    public Optional<User> signout(String username, String password, String lastname, String firstname) {
        LOGGER.info("Nuevo usuario que se registra: Validando en la bd ");
        if (username.trim().isEmpty() || username == null) {
            throw new IllegalArgumentException("El username no puede estar vacio");
        }

        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            LOGGER.info("El usuario ya se encuentra " + existingUser.get().getUsername());
            throw new UserAlreadyExistsException("El usuario " + username + " ya existe");
        }

        try {
            LOGGER.info("El usuario no se encuentra en la bd se procede a crear ");
            Optional<Role> role = roleRepository.findByRoleName("ROLE_CSR");
            Optional<User> user = Optional.of(userRepository.save(new User(username,
                    passwordEncoder.encode(password),
                    role.get(),
                    firstname,
                    lastname)));
            return user;
        } catch (Exception e) {
            LOGGER.error("Error al registrar el usuario {} " + username, e);
            throw new RuntimeException("Error al registrar al usuario ", e);
        }
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

}
