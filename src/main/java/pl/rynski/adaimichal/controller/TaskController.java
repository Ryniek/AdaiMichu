package pl.rynski.adaimichal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.rynski.adaimichal.dao.dto.request.NewTaskDto;
import pl.rynski.adaimichal.service.TaskService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskController {
	
	private final TaskService taskService;
	
	@GetMapping("/owned")
	public ResponseEntity<?> getAllCurrentUserTasks() {
		return ResponseEntity.status(HttpStatus.OK).body(taskService.getAllCurrentUserTasks());
	}
	
	@PostMapping
	public ResponseEntity<?> createTask(@RequestBody NewTaskDto newTaskDto) {
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
