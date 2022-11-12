package pl.rynski.adaimichal.service;

import static java.time.temporal.ChronoUnit.MINUTES;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.rynski.adaimichal.dao.dto.request.NewTaskDto;
import pl.rynski.adaimichal.dao.dto.response.TaskResponse;
import pl.rynski.adaimichal.dao.model.Task;
import pl.rynski.adaimichal.dao.model.User;
import pl.rynski.adaimichal.exception.ResourceNotFoundException;
import pl.rynski.adaimichal.exception.TooEarlyOperationException;
import pl.rynski.adaimichal.repository.TaskRepository;
import pl.rynski.adaimichal.security.CustomUserDetailsService;
import pl.rynski.adaimichal.utils.DateUtils;

@Service
@RequiredArgsConstructor
public class TaskService {
	
	private final TaskRepository taskRepository;
	private final CustomUserDetailsService userDetailsService;
	@Value("${minutes.between.drawing}")
	private long minutesBetweenDrawing;
	
	public List<TaskResponse> getAllCurrentUserTasks() {
		User currentUser = userDetailsService.getLoggedUser();
		List<Task> allTasksByCreator = taskRepository.findAllByCreator(currentUser);
		return allTasksByCreator.stream().map(task -> TaskResponse.toResponse(task)).toList();
	}
	
	public List<TaskResponse> getAllDrawnUnfinishedTasks() {
		List<Task> allDrawnUnfinished = taskRepository.findAllByIsStartedTrueAndIsFinishedFalse();
		return allDrawnUnfinished.stream().map(task -> TaskResponse.toResponse(task)).toList();
	}
	
	public TaskResponse createTask(NewTaskDto newTaskDto) {
		User currentUser = userDetailsService.getLoggedUser();
		Task task = NewTaskDto.taskFromDto(newTaskDto, currentUser);
		task.setCreationDate(DateUtils.getCurrentDateTime());
		taskRepository.save(task);
		return TaskResponse.toResponse(task);
	}
	
	public TaskResponse drawTask() {
		User currentUser = userDetailsService.getLoggedUser();
		LocalDateTime currentTime = DateUtils.getCurrentDateTime();
		
		checkDifferenceBetweenTwoDays(currentUser.getLastDateOfDrawingTask(), currentTime);
		
		Task randomTask = fetchAndSelectRandomTask();
		randomTask.setDrawnUser(currentUser);
		randomTask.setExpirationDate(currentTime.plusDays(randomTask.getDaysToUse()));
		randomTask.setIsStarted(true);
		
		return TaskResponse.toResponse(taskRepository.save(randomTask));
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
	
	private void checkDifferenceBetweenTwoDays(LocalDateTime lastDateOfDrawing, LocalDateTime currentDate) {
		long difference = MINUTES.between(lastDateOfDrawing, currentDate);
		if(difference < minutesBetweenDrawing) throw new TooEarlyOperationException(currentDate.plusMinutes(difference));
	}
	
	private Task fetchAndSelectRandomTask() {
		List<Task> unfinishedNotHiddenTasks = taskRepository.findAllByIsStartedFalseAndIsHiddenFalse();
		if(unfinishedNotHiddenTasks.isEmpty()) throw new ArrayIndexOutOfBoundsException();
		return unfinishedNotHiddenTasks.get(new Random().nextInt(unfinishedNotHiddenTasks.size()));
	}
}
