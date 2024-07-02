package pe.creativity.Restfull.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pe.creativity.Restfull.entity.User;
import pe.creativity.Restfull.repository.UserRepository;

import static org.springframework.security.core.userdetails.User.withUsername;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        /*Permita que las personas inicien sesión con nombre de usuario o correo electróni
         */
        User user = userRepository.findByUsername(usernameOrEmail).orElseThrow(() ->
                new UsernameNotFoundException(String.format(
                        "User with name %usernameOrEmail does not exist", usernameOrEmail)));
        return withUsername(user.getUsername())
                .password(user.getPasswd())
                .authorities(user.getRole())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();

    }

}
