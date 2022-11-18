package pl.rynski.adaimichal.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.rynski.adaimichal.dao.dto.request.TaskDto;
import pl.rynski.adaimichal.service.TaskService;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {
	
	@MockBean
	private TaskService taskService;
	
	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldGetAllFinishedTasks() throws Exception {
		
		mockMvc.perform(get("/tasks/finished"))
			.andExpect(status().isOk());
	}

	@Test
	void shouldGetAllCurrentUserTasks() throws Exception {
		mockMvc.perform(get("/tasks/owned"))
			.andExpect(status().isOk());
	}

	@Test
	void shouldGetAllDrawnUnfinishedTasks() throws Exception {
		mockMvc.perform(get("/tasks/drawn/unfinished"))
			.andExpect(status().isOk());
	}

	@Test
	void shouldCreateTask() throws Exception {
		TaskDto taskDto = new TaskDto();
		taskDto.setName("Test");
		taskDto.setDaysToUse(4L);
		ObjectMapper objectMapper = new ObjectMapper();
		String body = objectMapper.writeValueAsString(taskDto);
		
		mockMvc.perform(post("/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
			.andExpect(status().isCreated());
	}
	
	@Test
	void shouldNotAcceptBlankNameWhenCreateTask() throws Exception {
		TaskDto taskDto = new TaskDto();
		taskDto.setName("");
		taskDto.setDaysToUse(4L);
		ObjectMapper objectMapper = new ObjectMapper();
		String body = objectMapper.writeValueAsString(taskDto);
		
		mockMvc.perform(post("/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	void shouldNotAcceptNullDaysWhenCreateTask() throws Exception {
		TaskDto taskDto = new TaskDto();
		taskDto.setName("Test");
		ObjectMapper objectMapper = new ObjectMapper();
		String body = objectMapper.writeValueAsString(taskDto);
		
		mockMvc.perform(post("/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
			.andExpect(status().isBadRequest());
	}

	@Test
	void shouldNotAcceptNegativeDaysWhenCreateTask() throws Exception {
		TaskDto taskDto = new TaskDto();
		taskDto.setName("Test");
		taskDto.setDaysToUse(-4L);
		ObjectMapper objectMapper = new ObjectMapper();
		String body = objectMapper.writeValueAsString(taskDto);
		
		mockMvc.perform(post("/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	void shouldEditTask() throws Exception {
		TaskDto taskDto = new TaskDto();
		taskDto.setName("Test");
		taskDto.setDaysToUse(4L);
		ObjectMapper objectMapper = new ObjectMapper();
		String body = objectMapper.writeValueAsString(taskDto);
		
		mockMvc.perform(put("/tasks/{id}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
			.andExpect(status().isOk());
	}

	@Test
	void shouldDrawTask() throws Exception {
		mockMvc.perform(post("/tasks/draw"))
			.andExpect(status().isCreated());
	}

	@Test
	void shouldFinishTask() throws Exception {
		mockMvc.perform(post("/tasks/finish/{id}", 1L))
			.andExpect(status().isCreated());
	}

	@Test
	void shouldToggleHidden() throws Exception {
		mockMvc.perform(put("/tasks/toggle/hidden/{id}", 1L))
			.andExpect(status().isOk());
	}

	@Test
	void shouldDeleteUnusedTask() throws Exception {
		mockMvc.perform(delete("/tasks/{id}", 1L))
			.andExpect(status().isOk());
	}

}
