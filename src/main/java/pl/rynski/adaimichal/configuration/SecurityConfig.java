package pl.rynski.adaimichal.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import pl.rynski.adaimichal.security.CustomUserDetailsService;
import pl.rynski.adaimichal.security.MyCustomDsl;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final CustomUserDetailsService customUserDetailsService;
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		.apply(MyCustomDsl.customDsl()).and()
		.cors().and()
		.csrf().disable()
		.authorizeHttpRequests((autz) -> autz
			.antMatchers("/auth/login", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/users/password/reset", "/users/password/reset/set").permitAll()
			.anyRequest().authenticated())
		.authorizeHttpRequests().and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		return http.build();
	}
	
	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
			      .userDetailsService(customUserDetailsService)
			      .passwordEncoder(getPasswordEncoder())
			      .and()
			      .build();
	}
}
