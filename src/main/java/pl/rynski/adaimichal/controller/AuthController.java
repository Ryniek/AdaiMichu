package pl.rynski.adaimichal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import pl.rynski.adaimichal.dao.dto.request.UserDto;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Operation(summary = "Login. JWT token in response")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        return ResponseEntity.ok().build();
    }

}
