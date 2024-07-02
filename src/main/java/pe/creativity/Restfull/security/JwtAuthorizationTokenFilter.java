package pe.creativity.Restfull.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static pe.creativity.Restfull.util.Constants.HEADER_STRING;
import static pe.creativity.Restfull.util.Constants.TOKEN_PREFIX;

@Slf4j
public class JwtAuthorizationTokenFilter extends BasicAuthenticationFilter {
    /*Clase para la autorizacion de usuarios*/

    private static final Log Logger = LogFactory.getLog(JwtAuthorizationTokenFilter.class);

    private final JwtProvider jwtProvider;

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    public JwtAuthorizationTokenFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider, String secretKey) {
        super(authenticationManager);
        this.jwtProvider = jwtProvider;
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest rq, HttpServletResponse resp,
                                    FilterChain chain)
            throws IOException, ServletException {
        Logger.info("JWT authentication filter");
        String HeaderValue = rq.getHeader(HEADER_STRING);
        if (HeaderValue == null || !HeaderValue.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(rq, resp);
            return;
        }

        try {
            if (jwtProvider.validateJwtToken(HeaderValue)) {
                UsernamePasswordAuthenticationToken authentication = getAuthentication(rq);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException eje) {
            log.info("Excepcion de seguridad para el user {} - {}", eje.getClaims().getSubject(), eje.getMessage());
            ((HttpServletResponse) resp).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.debug("Excepcion " + eje.getMessage(), eje);
        }

        chain.doFilter(rq, resp);

        //restablece la autenticacion despues de la solicitud
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest rq) {
        String value = "";
        String token = rq.getHeader(HEADER_STRING);
        if (token != null) {
            // Se procesa el token y se recupera el usuario
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();
            String user = claims.getSubject();
            List<Map<String, String>> roles = (List<Map<String, String>>) claims.get("roles", List.class);
            for (Map<String, String> role : roles) {
                value = role.get("authority");
            }
            List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(value));
            if (user != null) {
                //return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                return new UsernamePasswordAuthenticationToken(user, null, authorities);
            }
            return null;
        }
        return null;
    }

}
