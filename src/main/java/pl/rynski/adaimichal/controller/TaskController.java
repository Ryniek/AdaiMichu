package pl.rynski.adaimichal.controller;

import javax.validation.Valid;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import pl.rynski.adaimichal.dao.dto.request.TaskDto;
import pl.rynski.adaimichal.service.TaskService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
	
	private final TaskService taskService;
	
	@Operation(summary = "Returning all finished tasks sorted by finish date descending")
	@GetMapping("/finished")
	public ResponseEntity<?> getAllFinishedTasks() {
		return ResponseEntity.status(HttpStatus.OK).body(taskService.getAllFinished());
	}
	
	@Operation(summary = "Returning all tasks created by logged user sorted ascending by creation date")
	@GetMapping("/owned")
	public ResponseEntity<?> getAllCurrentUserTasks() {
		return ResponseEntity.status(HttpStatus.OK).body(taskService.getAllCurrentUserTasks());
	}
	
	@Operation(summary = "Returning all drawn tasks sorted ascending by expiration date")
	@GetMapping("/drawn/unfinished")
	public ResponseEntity<?> getAllDrawnUnfinishedTasks() {
		return ResponseEntity.status(HttpStatus.OK).body(taskService.getAllDrawnUnfinishedTasks());
	}
	
	@Operation(summary = "Creating new task")
	@PostMapping
	public ResponseEntity<?> createTask(@Valid @RequestBody TaskDto newTaskDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(newTaskDto));
	}
	
	@Operation(summary = "Editing existing task created by logged user")
	@PutMapping("/{id}")
	public ResponseEntity<?> editTask(@Valid @RequestBody TaskDto taskDto, @Parameter(description = "id of task to be edited") @PathVariable long id) {
		return ResponseEntity.status(HttpStatus.OK).body(taskService.editTask(taskDto, id));
	}
	
	@Operation(summary = "Drawing new task by logged user. Sending email notification if succeeded draw")
	@PostMapping("/draw")
	public ResponseEntity<?> drawTask() {
		return ResponseEntity.status(HttpStatus.CREATED).body(taskService.drawTask());
	}
	
	@Operation(summary = "Finishing task drawn by logged user")
	@PostMapping("/finish/{id}")
	public ResponseEntity<?> finishTask(@Parameter(description = "id of task to be finished") @PathVariable long id) {
		return ResponseEntity.status(HttpStatus.CREATED).body(taskService.finishTask(id));
	}
	
	@Operation(summary = "Toggle hidden option of task created by logged user")
	@PutMapping("/toggle/hidden/{id}")
	public ResponseEntity<?> toggleHidden(@Parameter(description = "id of task to toggle visibility") @PathVariable long id) {
		return ResponseEntity.status(HttpStatus.OK).body(taskService.toggleHidden(id));
	}
	
	@Operation(summary = "Deleting unstarted task created by logged user")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUnusedTask(@Parameter(description = "id of task to be deleted") @PathVariable long id) {
		taskService.deleteUnstartedTask(id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
