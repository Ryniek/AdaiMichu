package pl.rynski.adaimichal.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import pl.rynski.adaimichal.dao.dto.request.TaskDto;
import pl.rynski.adaimichal.dao.dto.response.TaskResponse;
import pl.rynski.adaimichal.dao.model.Task;
import pl.rynski.adaimichal.dao.model.User;
import pl.rynski.adaimichal.exception.NoTaskToDrawnException;
import pl.rynski.adaimichal.exception.ResourceNotFoundException;
import pl.rynski.adaimichal.repository.TaskRepository;
import pl.rynski.adaimichal.security.CustomUserDetailsService;
import pl.rynski.adaimichal.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
	
	@InjectMocks private TaskService taskService;
	@Mock private TaskRepository taskRepository;
	@Mock private CustomUserDetailsService userDetailsService;
	@Mock private NotificationService notificationService;
	
	@Test
	void testGetAllFinished() {
		taskService.getAllFinished();
		
		verify(taskRepository).shouldFindAllFinishedSortedByFinishDate();
	}

	@Test
	void shouldGetAllCurrentUserTasks() {
		User user = new User();
		when(userDetailsService.getLoggedUser()).thenReturn(user);
		
		taskService.getAllCurrentUserTasks();
		
		verify(taskRepository).findAllByCreator(user, Sort.by("creationDate").descending());
	}

	@Test
	void shouldGetAllDrawnUnfinishedTasks() {
		taskService.getAllDrawnUnfinishedTasks();
		
		verify(taskRepository).findAllByIsStartedTrueAndIsFinishedFalse(Sort.by("expirationDate").ascending());
		verify(taskRepository).saveAll(Collections.EMPTY_LIST);
	}
	
	@Test
	void shouldSetToFinishedWhenExpirationTimePassed() {
		List<Task> tasks = new ArrayList<>();
		Task task = new Task();
		task.setExpirationDate(DateUtils.getCurrentDateTime().minusDays(1));
		tasks.add(task);
		when(taskRepository.findAllByIsStartedTrueAndIsFinishedFalse(Sort.by("expirationDate").ascending())).thenReturn(tasks);
		
		List<TaskResponse> allDrawnUnfinishedTasks = taskService.getAllDrawnUnfinishedTasks();
		
		verify(taskRepository).findAllByIsStartedTrueAndIsFinishedFalse(Sort.by("expirationDate").ascending());
		assertTrue(allDrawnUnfinishedTasks.get(0).isFinished());
		verify(taskRepository).saveAll(tasks);
	}
	
	@Test
	void shouldNotSetToFinishedWhenExpirationTimeNotPassed() {
		List<Task> tasks = new ArrayList<>();
		Task task = new Task();
		task.setExpirationDate(DateUtils.getCurrentDateTime().plusDays(1));
		tasks.add(task);
		when(taskRepository.findAllByIsStartedTrueAndIsFinishedFalse(Sort.by("expirationDate").ascending())).thenReturn(tasks);
		
		List<TaskResponse> allDrawnUnfinishedTasks = taskService.getAllDrawnUnfinishedTasks();
		
		verify(taskRepository).findAllByIsStartedTrueAndIsFinishedFalse(Sort.by("expirationDate").ascending());
		assertFalse(allDrawnUnfinishedTasks.get(0).isFinished());
		verify(taskRepository).saveAll(tasks);
	}

	@Test
	void shouldCreateTask() {
		User user = new User();
		user.setId(1L);
		TaskDto taskDto = new TaskDto();
		taskDto.setName("Test");
		when(userDetailsService.getLoggedUser()).thenReturn(user);
		when(taskRepository.save(Mockito.any())).thenReturn(TaskDto.taskFromDto(taskDto, user));
		
		TaskResponse response = taskService.createTask(taskDto);
		
		assertEquals(taskDto.getName(), response.getName());
	}

	@Test
	void shouldEditTask() {
		User user = new User();
		when(userDetailsService.getLoggedUser()).thenReturn(user);
		TaskDto taskDto = new TaskDto();
		taskDto.setName("1234");
		Task oldTask = new Task();
		oldTask.setName("12345");
		Task editedTask = new Task();
		editedTask.setName("1234");
		
		when(taskRepository.findByIdAndCreatorAndIsStartedFalseAndIsFinishedFalse(1L, user)).thenReturn(Optional.of(oldTask));
		when(taskRepository.save(Mockito.any())).thenReturn(editedTask);
		
		TaskResponse response = taskService.editTask(taskDto, 1L);
		
		assertEquals(editedTask.getName(), response.getName());
	}
	
	@Test
	void shouldNotFindTaskToEdit() {
		User user = new User();
		when(userDetailsService.getLoggedUser()).thenReturn(user);
		
		when(taskRepository.findByIdAndCreatorAndIsStartedFalseAndIsFinishedFalse(1L, user)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> taskService.editTask(new TaskDto(), 1L))
			.isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	void shouldDrawTask() {
		User user = new User();
		user.setAssignedUserId(2L);
		user.setLastDateOfDrawingTask(DateUtils.getCurrentDateTime().minusDays(100));
		Task task = new Task();
		task.setName("test");
		task.setDaysToUse(4L);
		
		when(userDetailsService.getLoggedUser()).thenReturn(user);
		when(taskRepository.findAllByIsStartedFalseAndIsHiddenFalse()).thenReturn(Arrays.asList(task));
		Mockito.doNothing().when(notificationService).sendNewDrawnTaskNotification(ArgumentMatchers.anyLong());
		when(taskRepository.save(Mockito.any())).thenReturn(task);
		
		TaskResponse drawTask = taskService.drawTask();
		
		assertEquals(task.getName(), drawTask.getName());
		assertTrue(task.getIsStarted());
	}
	
	@Test
	void shouldThrowNoTaskToDrawnExceptionWhenDrawingTask() {
		User user = new User();
		user.setLastDateOfDrawingTask(DateUtils.getCurrentDateTime().minusDays(100));
		
		when(userDetailsService.getLoggedUser()).thenReturn(user);
		when(taskRepository.findAllByIsStartedFalseAndIsHiddenFalse()).thenReturn(Collections.EMPTY_LIST);
		
		verify(taskRepository, never()).save(Mockito.any());
		assertThatThrownBy(() -> taskService.drawTask())
			.isInstanceOf(NoTaskToDrawnException.class);
	}

	@Test
	void shouldFinishTask() {
		User user = new User();
		Optional<Task> task = Optional.of(new Task());
		
		when(userDetailsService.getLoggedUser()).thenReturn(user);
		when(taskRepository.findByIdAndDrawnUser(1L, user)).thenReturn(task);
		when(taskRepository.save(Mockito.any())).thenReturn(task.get());
		
		TaskResponse result = taskService.finishTask(1L);

		assertTrue(result.isFinished());
		assertFalse(result.isStarted());
		assertNotNull(result.getFinishDate());
	}
	
	@Test
	void shouldThrowResourceNotFoundExceptionWhenFinishTask() {
		User user = new User();
		
		when(userDetailsService.getLoggedUser()).thenReturn(user);
		when(taskRepository.findByIdAndDrawnUser(1L, user)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> taskService.finishTask(1L))
			.isInstanceOf(ResourceNotFoundException.class);
		verify(taskRepository, never()).save(Mockito.any());
	}

	@Test
	void shouldToggleHidden() {
		User user = new User();
		when(userDetailsService.getLoggedUser()).thenReturn(user);
		Task newTask = new Task();
		newTask.setIsHidden(false);
		Optional<Task> task = Optional.of(newTask);
		
		when(taskRepository.findByIdAndCreatorAndIsStartedFalseAndIsFinishedFalse(1L, user)).thenReturn(task);

		TaskResponse response = taskService.toggleHidden(1L);
		
		verify(taskRepository).save(Mockito.any(Task.class));
		assertTrue(response.isHidden());
	}
	
	@Test
	void shouldThrowResourceNotFoundWhenToggleHidden() {
		User user = new User();
		when(userDetailsService.getLoggedUser()).thenReturn(user);
		
		when(taskRepository.findByIdAndCreatorAndIsStartedFalseAndIsFinishedFalse(1L, user)).thenReturn(Optional.empty());

		verify(taskRepository, never()).save(Mockito.any());
		assertThatThrownBy(() -> taskService.toggleHidden(1L))
			.isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	void shouldDeleteUnstartedTask() {
		User user = new User();
		when(userDetailsService.getLoggedUser()).thenReturn(user);
		Optional<Task> task = Optional.of(new Task());
		when(taskRepository.findByIdAndCreatorAndIsStartedFalseAndIsFinishedFalse(1L, user)).thenReturn(task);
		
		taskService.deleteUnstartedTask(1L);
		
		verify(taskRepository, times(1)).delete(Mockito.any(Task.class));
	}

	@Test
	void testThrowResourceNotFoundExceptionsWhenDeleteUnstartedTask() {
		User user = new User();
		when(userDetailsService.getLoggedUser()).thenReturn(user);
		when(taskRepository.findByIdAndCreatorAndIsStartedFalseAndIsFinishedFalse(1L, user)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> taskService.deleteUnstartedTask(1L))
			.isInstanceOf(ResourceNotFoundException.class);
		verify(taskRepository, times(0)).delete(Mockito.any(Task.class));
	}
}
