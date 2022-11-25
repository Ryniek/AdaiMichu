package pl.rynski.adaimichal.service;

import static java.time.temporal.ChronoUnit.MINUTES;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.rynski.adaimichal.dao.dto.request.TaskDto;
import pl.rynski.adaimichal.dao.dto.response.TaskResponse;
import pl.rynski.adaimichal.dao.model.Task;
import pl.rynski.adaimichal.dao.model.User;
import pl.rynski.adaimichal.exception.NoTaskToDrawnException;
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
	private final NotificationService notificationService;
	
	@Value("${minutes.between.drawing}")
	private long minutesBetweenDrawing;
	
	public List<TaskResponse> getAllFinished() {
		List<Task> allFinishedTasks = taskRepository.shouldFindAllFinishedSortedByFinishDate();
		return allFinishedTasks.stream().map(task -> TaskResponse.toResponse(task)).toList();
	}
	
	public List<TaskResponse> getAllCurrentUserTasks() {
		User currentUser = userDetailsService.getLoggedUser();
		List<Task> allTasksByCreator = taskRepository.findAllByCreator(currentUser, Sort.by("creationDate").descending());
		return allTasksByCreator.stream().map(task -> TaskResponse.toResponse(task)).toList();
	}
	
	public List<TaskResponse> getAllDrawnUnfinishedTasks() {
		List<Task> allDrawnUnfinished = taskRepository.findAllByIsStartedTrueAndIsFinishedFalse(Sort.by("expirationDate").ascending());
		checkIfExpirationDatePassed(allDrawnUnfinished);
		return allDrawnUnfinished.stream().map(task -> TaskResponse.toResponse(task)).toList();
	}
	
	public TaskResponse createTask(TaskDto newTaskDto) {
		User currentUser = userDetailsService.getLoggedUser();
		Task task = TaskDto.taskFromDto(newTaskDto, currentUser);
		task.setCreationDate(DateUtils.getCurrentDateTime());
		return TaskResponse.toResponse(taskRepository.save(task));
	}
	
	public TaskResponse editTask(TaskDto taskDto, long id) {
		User currentUser = userDetailsService.getLoggedUser();
		Task task = taskRepository.findByIdAndCreatorAndIsStartedFalseAndIsFinishedFalse(id, currentUser)
				.orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
		return TaskResponse.toResponse(taskRepository.save(TaskDto.editTaskFromDto(task, taskDto)));
	}
	
	public TaskResponse drawTask() {
		User currentUser = userDetailsService.getLoggedUser();
		LocalDateTime currentTime = DateUtils.getCurrentDateTime();
		
		checkDifferenceBetweenTwoDays(currentUser.getLastDateOfDrawingTask(), currentTime);
		
		Task randomTask = fetchAndSelectRandomTask();
		randomTask.setDrawnUser(currentUser);
		randomTask.setExpirationDate(currentTime.plusDays(randomTask.getDaysToUse()));
		randomTask.setIsStarted(true);
		currentUser.setLastDateOfDrawingTask(currentTime);
		currentUser.setNotificationSend(false);
		
		notificationService.sendNewDrawnTaskNotification(currentUser.getAssignedUserId());
		
		return TaskResponse.toResponse(taskRepository.save(randomTask));
	}
	
	public TaskResponse finishTask(long id) {
		Task task = taskRepository.findByIdAndDrawnUser(id, userDetailsService.getLoggedUser())
				.orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
		task.setFinishDate(DateUtils.getCurrentDateTime());
		task.setIsFinished(true);
		task.setIsStarted(false);
		return TaskResponse.toResponse(taskRepository.save(task));
	}
	
	public TaskResponse toggleHidden(long taskId) {
		User currentUser = userDetailsService.getLoggedUser();
		Task task = taskRepository.findByIdAndCreatorAndIsStartedFalseAndIsFinishedFalse(taskId, currentUser)
				.orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));
		task.setIsHidden(!task.getIsHidden());
		taskRepository.save(task);
		return TaskResponse.toResponse(task);
	}
	
	public void deleteUnstartedTask(Long taskId) {
		User currentUser = userDetailsService.getLoggedUser();
		Task task = taskRepository.findByIdAndCreatorAndIsStartedFalseAndIsFinishedFalse(taskId, currentUser)
				.orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));
		taskRepository.delete(task);
	}
	
	private void checkIfExpirationDatePassed(List<Task> tasks) {
		LocalDateTime currentTime = DateUtils.getCurrentDateTime();
		tasks.stream().forEach(task -> {
			if(task.getExpirationDate().isBefore(currentTime)) {
				task.setIsFinished(true);
				task.setIsStarted(false);
			}
		});
		taskRepository.saveAll(tasks);
	}
	
	private void checkDifferenceBetweenTwoDays(LocalDateTime lastDateOfDrawing, LocalDateTime currentDate) {
		long difference = MINUTES.between(lastDateOfDrawing, currentDate);
		if(difference < minutesBetweenDrawing) throw new TooEarlyOperationException(currentDate.plusMinutes(minutesBetweenDrawing - difference));
	}
	
	private Task fetchAndSelectRandomTask() {
		List<Task> unfinishedNotHiddenTasks = taskRepository.findAllByIsStartedFalseAndIsHiddenFalseAndIsFinishedFalse();
		if(unfinishedNotHiddenTasks.isEmpty()) throw new NoTaskToDrawnException();
		return unfinishedNotHiddenTasks.get(new Random().nextInt(unfinishedNotHiddenTasks.size()));
	}
}
