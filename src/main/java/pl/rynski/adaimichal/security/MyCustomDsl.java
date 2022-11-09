package pl.rynski.adaimichal.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
	@Override
    public void configure(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        http.addFilter(new JWTAuthenticationFilter(authenticationManager));
        http.addFilter(new JWTAuthorizationFilter(authenticationManager));
    }

    public static MyCustomDsl customDsl() {
        return new MyCustomDsl();
    }
}
