package pe.creativity.Restfull.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pe.creativity.Restfull.security.CustomUserDetailsService;
import pe.creativity.Restfull.security.JwtAutheticationTokenFilter;
import pe.creativity.Restfull.security.JwtAuthorizationTokenFilter;
import pe.creativity.Restfull.security.JwtProvider;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtProvider jwtProvider;

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/Home", "/swagger-ui/index.html", "/v2/api-docs", "/swagger-resources/configuration/ui").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/users/signin").permitAll()
                .antMatchers("/users/signout").permitAll()
                .antMatchers("/v1").authenticated()
                //no permitir a todos los demas
                .anyRequest().authenticated()
                //  .and()    retirar
                //   .formLogin()    retirar
                // .loginPage("/log-in")
                //      .permitAll()    retirar
                .and()
                .logout()
                .permitAll()
                .and()
                .addFilter(new JwtAutheticationTokenFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationTokenFilter(authenticationManager(), jwtProvider, secretKey));
        // Disable CSRF (cross site request forgery)
        http.csrf().disable();

    }

//    @Bean
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
//    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowedMethods(List.of("GET", "POST"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
