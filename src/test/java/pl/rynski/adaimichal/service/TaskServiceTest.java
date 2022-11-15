package pl.rynski.adaimichal.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import pl.rynski.adaimichal.dao.dto.response.TaskResponse;
import pl.rynski.adaimichal.dao.model.Task;
import pl.rynski.adaimichal.dao.model.User;
import pl.rynski.adaimichal.repository.TaskRepository;
import pl.rynski.adaimichal.security.CustomUserDetailsService;
import pl.rynski.adaimichal.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
	
	@InjectMocks TaskService taskService;
	@Mock TaskRepository taskRepository;
	@Mock CustomUserDetailsService userDetailsService;
	
	@Test
	void testGetAllFinished() {
		taskService.getAllFinished();
		
		verify(taskRepository).shouldFindAllFinishedSortedByFinishDate();
	}

	@Test
	void testGetAllCurrentUserTasks() {
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
	void testCreateTask() {
		
		
		
	}

	@Test
	void testEditTask() {
		fail("Not yet implemented");
	}

	@Test
	void testDrawTask() {
		fail("Not yet implemented");
	}

	@Test
	void testFinishTask() {
		fail("Not yet implemented");
	}

	@Test
	void testToggleHidden() {
		fail("Not yet implemented");
	}

	@Test
	void testDeleteUnstartedTask() {
		fail("Not yet implemented");
	}

}
