package pe.creativity.Restfull.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import pe.creativity.Restfull.entity.Role;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    private final String ROLES_KEY = "roles";

    private JwtParser parser;

    private String secretKey;

    private long validityInMilliseconds;


    public String createToken(String username, List<Role> roles) {
        //agregando usuario al payload
        Claims claims = Jwts.claims().setSubject(username);
        //convertir roles a spring security SimpleGrantedAuthority objects
        //añadir spring security SimpleGrantedAuthority al claims(reclamo)
        claims.put(ROLES_KEY, roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));

        //compilando token

        Date date = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime()+ validityInMilliseconds))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();

    }


}
