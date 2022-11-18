package pl.rynski.adaimichal.controller;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import pl.rynski.adaimichal.service.TaskService;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {
	
	@MockBean
	private TaskService taskService;
	
	@Autowired
	private MockMvc mockMvc;

	@Test
	void testGetAllFinishedTasks() {
		
	}

	@Test
	void testGetAllCurrentUserTasks() {
		fail("Not yet implemented");
	}

	@Test
	void testGetAllDrawnUnfinishedTasks() {
		fail("Not yet implemented");
	}

	@Test
	void testCreateTask() {
		fail("Not yet implemented");
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
	void testDeleteUnusedTask() {
		fail("Not yet implemented");
	}

}
