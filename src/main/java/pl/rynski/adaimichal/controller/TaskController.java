package pl.rynski.adaimichal.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import pl.rynski.adaimichal.dao.dto.request.TaskDto;
import pl.rynski.adaimichal.service.TaskService;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
	
	private final TaskService taskService;
	
	@GetMapping("/finished")
	public ResponseEntity<?> getAllFinishedTasks() {
		return ResponseEntity.status(HttpStatus.OK).body(taskService.getAllFinished());
	}
	
	@Operation(description = "Returning all tasks creater buy logged user sorted ascending by creation date")
	@GetMapping("/owned")
	public ResponseEntity<?> getAllCurrentUserTasks() {
		return ResponseEntity.status(HttpStatus.OK).body(taskService.getAllCurrentUserTasks());
	}
	
	@GetMapping("/drawn/unfinished")
	public ResponseEntity<?> getAllDrawnUnfinishedTasks() {
		return ResponseEntity.status(HttpStatus.OK).body(taskService.getAllDrawnUnfinishedTasks());
	}
	
	@PostMapping
	public ResponseEntity<?> createTask(@Valid @RequestBody TaskDto newTaskDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(newTaskDto));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> editTask(@Valid @RequestBody TaskDto taskDto, @PathVariable long id) {
		return ResponseEntity.status(HttpStatus.OK).body(taskService.editTask(taskDto, id));
	}
	
	@PostMapping("/draw")
	public ResponseEntity<?> drawTask() {
		return ResponseEntity.status(HttpStatus.CREATED).body(taskService.drawTask());
	}
	
	@PostMapping("/finish/{id}")
	public ResponseEntity<?> finishTask(@PathVariable long id) {
		return ResponseEntity.status(HttpStatus.CREATED).body(taskService.finishTask(id));
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
