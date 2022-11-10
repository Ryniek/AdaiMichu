package pl.rynski.adaimichal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.rynski.adaimichal.dao.dto.request.NewTaskDto;
import pl.rynski.adaimichal.dao.dto.response.TaskResponse;
import pl.rynski.adaimichal.dao.model.Task;
import pl.rynski.adaimichal.dao.model.User;
import pl.rynski.adaimichal.exception.ResourceNotFoundException;
import pl.rynski.adaimichal.repository.TaskRepository;
import pl.rynski.adaimichal.security.CustomUserDetailsService;
import pl.rynski.adaimichal.utils.DateUtils;

@Service
@RequiredArgsConstructor
public class TaskService {
	
	private final TaskRepository taskRepository;
	private final CustomUserDetailsService userDetailsService;
	
	public List<TaskResponse> getAllCurrentUserTasks() {
		User currentUser = userDetailsService.getLoggedUser();
		List<Task> allTasksByCreator = taskRepository.findAllByCreator(currentUser);
		return allTasksByCreator.stream().map(task -> TaskResponse.toResponse(task)).toList();
	}
	
	public List<TaskResponse> getAllDrownUnfinishedTasks() {
		List<Task> allDrownUnfinished = taskRepository.findAllByIsStartedTrueAndIsFinishedFalse();
		return allDrownUnfinished.stream().map(task -> TaskResponse.toResponse(task)).toList();
	}
	
	public TaskResponse createTask(NewTaskDto newTaskDto) {
		User currentUser = userDetailsService.getLoggedUser();
		Task task = NewTaskDto.taskFromDto(newTaskDto, currentUser);
		task.setCreationDate(DateUtils.getCurrentDateTime());
		taskRepository.save(task);
		return TaskResponse.toResponse(task);
	}
	
	public TaskResponse toggleHidden(long taskId) {
		User currentUser = userDetailsService.getLoggedUser();
		Task task = taskRepository.findByIdAndCreatorAndIsStartedFalse(taskId, currentUser).orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));
		task.setIsHidden(!task.getIsHidden());
		taskRepository.save(task);
		return TaskResponse.toResponse(task);
	}
	
	public void deleteUnstartedTask(Long taskId) {
		User currentUser = userDetailsService.getLoggedUser();
		Task task = taskRepository.findByIdAndCreatorAndIsStartedFalse(taskId, currentUser).orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));
		taskRepository.delete(task);
	}
}
