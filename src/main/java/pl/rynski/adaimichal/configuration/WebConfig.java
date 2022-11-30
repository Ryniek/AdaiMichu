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
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PUT","OPTIONS","PATCH", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true)
                .exposedHeaders("If-Match", "Authorization")
                .allowedOriginPatterns("http://localhost:3000", "http://localhost:3001", "https://www.adaimichu.com")
                .maxAge(3600);
    }
}
