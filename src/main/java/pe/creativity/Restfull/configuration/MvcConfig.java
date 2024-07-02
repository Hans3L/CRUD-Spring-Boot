package pe.creativity.Restfull.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/").setViewName("Home");
        registry.addViewController("/login").setViewName("sigin.html");
        registry.addViewController("/swagger-ui").setViewName("SWAGGER");
    }
}
