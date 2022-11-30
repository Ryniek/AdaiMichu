package pl.rynski.adaimichal.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import pl.rynski.adaimichal.dao.dto.request.GlobalSettingsDto;
import pl.rynski.adaimichal.service.AdminService;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
	
	private final AdminService adminService;
	
	@Secured("ROLE_ADMIN")	
	@Operation(summary = "Fetching current global settings for app by admin")
	@GetMapping("/settings")
	public ResponseEntity<?> fetchGlobalSettings() {
		return ResponseEntity.status(HttpStatus.OK).body(adminService.fetchGlobalSettings());
	}
	
	@Secured("ROLE_ADMIN")	
	@Operation(summary = "Setting current global settings for app by admin")
	@PutMapping("/settings")
	public ResponseEntity<?> setGlobalSettings(@Valid @RequestBody GlobalSettingsDto globalSettingsDto) {
		return ResponseEntity.status(HttpStatus.OK).body(adminService.setGlobalSettings(globalSettingsDto));
	}
	
	@Secured("ROLE_ADMIN")
	@Operation(summary = "Deleting finished task by admin")
	@DeleteMapping("/tasks/{id}")
	public ResponseEntity<?> deleteFinishedTask(@Parameter(description = "id of task to be deleted") @PathVariable long id) {
		adminService.deleteFinishedTask(id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	
}
