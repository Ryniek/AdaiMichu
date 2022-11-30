package pl.rynski.adaimichal.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import pl.rynski.adaimichal.dao.dto.request.GlobalSettingsDto;
import pl.rynski.adaimichal.service.AdminService;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {
	
	@MockBean
	private AdminService adminService;
	
	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldGetAllGlobalSettings() throws Exception {
		mockMvc.perform(get("/admin/settings"))
			.andExpect(status().isOk());
	}
	
	@Test
	void shouldSetGlobalSettings() throws Exception {
		GlobalSettingsDto dto = new GlobalSettingsDto();
		dto.setMinutesBetweenDrawing(500L);
		dto.setResetPasswordTokenValidity(500L);
		ObjectMapper objectMapper = new ObjectMapper();
		String body = objectMapper.writeValueAsString(dto);
		
		mockMvc.perform(put("/admin/settings")
			.contentType(MediaType.APPLICATION_JSON)
			.content(body))
			.andExpect(status().isOk());
	}
	
	@Test
	void shouldNotAcceptNullWhenSetGlobalSettings() throws Exception {
		GlobalSettingsDto dto = new GlobalSettingsDto();
		dto.setResetPasswordTokenValidity(500L);
		ObjectMapper objectMapper = new ObjectMapper();
		String body = objectMapper.writeValueAsString(dto);
		
		mockMvc.perform(put("/admin/settings")
			.contentType(MediaType.APPLICATION_JSON)
			.content(body))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	void shouldNotAcceptExceedMaxWhenSetGlobalSettings() throws Exception {
		GlobalSettingsDto dto = new GlobalSettingsDto();
		dto.setMinutesBetweenDrawing(500000L);
		dto.setResetPasswordTokenValidity(500L);
		ObjectMapper objectMapper = new ObjectMapper();
		String body = objectMapper.writeValueAsString(dto);
		
		mockMvc.perform(put("/admin/settings")
			.contentType(MediaType.APPLICATION_JSON)
			.content(body))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	void shouldDeleteFinishedTask() throws Exception {
		mockMvc.perform(delete("/admin/tasks/{id}", 1L))
			.andExpect(status().isOk());
	}
}
