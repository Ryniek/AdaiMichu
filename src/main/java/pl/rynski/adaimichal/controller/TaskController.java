package pl.rynski.adaimichal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.rynski.adaimichal.dao.dto.request.NewTaskDto;
import pl.rynski.adaimichal.service.TaskService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
	
	private final TaskService taskService;
	
	@GetMapping("/owned")
	public ResponseEntity<?> getAllCurrentUserTasks() {
		return ResponseEntity.status(HttpStatus.OK).body(taskService.getAllCurrentUserTasks());
	}
	
	@GetMapping("/drawned/unfinished")
	public ResponseEntity<?> getAllDrownUnfinishedTasks() {
		return ResponseEntity.status(HttpStatus.OK).body(taskService.getAllDrownUnfinishedTasks());
	}
	
	@PostMapping
	public ResponseEntity<?> createTask(@RequestBody NewTaskDto newTaskDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(newTaskDto));
	}
	
	@PutMapping("/toggle/hidden/{id}")
	public ResponseEntity<?> toggleHidden(@PathVariable long id) {
		return ResponseEntity.status(HttpStatus.OK).body(taskService.toggleHidden(id));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUnusedTask(@PathVariable long id) {
		taskService.deleteUnstartedTask(id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
