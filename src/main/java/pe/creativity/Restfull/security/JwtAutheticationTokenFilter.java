package pe.creativity.Restfull.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pe.creativity.Restfull.entity.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;


import static pe.creativity.Restfull.util.Constants.HEADER_STRING;
import static pe.creativity.Restfull.util.Constants.TOKEN_PREFIX;
import static pe.creativity.Restfull.util.Constants.SECRET_KEY;

public class JwtAutheticationTokenFilter extends UsernamePasswordAuthenticationFilter {
    /*Clase para la autenticacion de usuarios*/


    private AuthenticationManager authenticationManager;

    public JwtAutheticationTokenFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            User credencial = new ObjectMapper().readValue(request.getInputStream(), User.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    credencial.getUsername(),
                    credencial.getPasswd(),
                    new ArrayList<>()
            ));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    protected void successfulAuthentication(HttpServletResponse response,
                                            HttpServletRequest request,
                                            FilterChain chain, Authentication auth)
            throws IOException, ServletException {

        JwtProvider jProvider = new JwtProvider();
        User user = new User();
        String token = jProvider.createToken(user.getUsername(), user.getRole());
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);

    }

}
