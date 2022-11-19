package pl.rynski.adaimichal.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .exposedHeaders("If-Match")
                .allowedOriginPatterns("*")
                .maxAge(3600);
    }
}
