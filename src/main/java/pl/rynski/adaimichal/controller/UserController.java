package pl.rynski.adaimichal.controller;

import javax.validation.Valid;
import javax.validation.constraints.Email;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import pl.rynski.adaimichal.dao.dto.request.PasswordDto;
import pl.rynski.adaimichal.service.UserService;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/users")
public class UserController {
	private final UserService userService;
	
	@Operation(summary = "Getting details of logged user")
	@GetMapping
	public  ResponseEntity<?> getUserDetails() {
		return ResponseEntity.status(HttpStatus.OK).body(userService.getUserDetails());
	}

	@Operation(summary = "Setting email of logged user")
	@PutMapping("/email")
	public ResponseEntity<?> setEmail(@RequestParam @Email(message = "Podaj adres email w poprawnej formie.") String address) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.setEmail(address));
	}
	
	@Operation(summary = "Setting password of logged user")
	@PutMapping("/password")
	public ResponseEntity<?> setPassword(@RequestBody @Valid PasswordDto passwordDto) {
		userService.setPassword(passwordDto);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
