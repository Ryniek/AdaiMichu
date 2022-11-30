package pl.rynski.adaimichal.security;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
	
    private String token;
    private String name;
    private List<String> roles;
}
