package pe.creativity.Restfull.Security;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static pe.creativity.Restfull.util.Constants.HEADER_STRING;
import static pe.creativity.Restfull.util.Constants.TOKEN_PREFIX;
import static pe.creativity.Restfull.util.Constants.SECRET_KEY;

public class JwtAuthorizationTokenFilter extends BasicAuthenticationFilter {
    /*Clase para la autorizacion de usuarios*/

    private long validityInMilliseconds;
    private String secretKey;

    public JwtAuthorizationTokenFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest rq, HttpServletResponse resp,
                                    FilterChain chain)
            throws IOException, ServletException {

        String HeaderValue = rq.getHeader(HEADER_STRING);
        if (HeaderValue == null || !HeaderValue.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(rq, resp);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(rq);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(rq, resp);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest rq) {

        String token = rq.getHeader(HEADER_STRING);
        if (token != null) {
            // Se procesa el token y se recupera el usuario
            String user = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();
            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }


}
