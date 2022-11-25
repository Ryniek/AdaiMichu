package pl.rynski.adaimichal.controller;

import static org.mockito.Mockito.when;
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

import pl.rynski.adaimichal.dao.dto.request.PasswordDto;
import pl.rynski.adaimichal.dao.dto.response.UserResponse;
import pl.rynski.adaimichal.service.UserService;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
	
	@MockBean
	private UserService userService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void shouldGetUserDetails() throws Exception {
		mockMvc.perform(get("/users"))
			.andExpect(status().isOk());
	}

	@Test
	void shouldSetEmail() throws Exception {
		String email = "testemail@test.com";
		when(userService.setEmail(email)).thenReturn(new UserResponse());
		
		mockMvc.perform(put("/users/email")
				.param("address", email))
				.andExpect(status().isCreated());
	}
	
	@Test
	void shouldNotAcceptWrongEmail() throws Exception {
		String email = "testemail";
		
		mockMvc.perform(put("/users/email")
				.param("address", email))
				.andExpect(status().isNotAcceptable());
	}

	@Test
	void shouldSetPassword() throws Exception {
		PasswordDto passwordDto = new PasswordDto();
		passwordDto.setOldPassword("12345678");
		passwordDto.setNewPassword("123456789");
		ObjectMapper objectMapper = new ObjectMapper();
		String body = objectMapper.writeValueAsString(passwordDto);
		
		mockMvc.perform(put("/users/password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
				.andExpect(status().isOk());
	}

	@Test
	void shouldNotAcceptTooShortNewPassword() throws Exception {
		PasswordDto passwordDto = new PasswordDto();
		passwordDto.setOldPassword("12345678");
		passwordDto.setNewPassword("12");
		ObjectMapper objectMapper = new ObjectMapper();
		String body = objectMapper.writeValueAsString(passwordDto);
		
		mockMvc.perform(put("/users/password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void shouldNotAcceptNullableOldPassword() throws Exception {
		PasswordDto passwordDto = new PasswordDto();
		passwordDto.setNewPassword("123456789");
		ObjectMapper objectMapper = new ObjectMapper();
		String body = objectMapper.writeValueAsString(passwordDto);
		
		mockMvc.perform(put("/users/password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void shouldNotAcceptBlankOldPassword() throws Exception {
		PasswordDto passwordDto = new PasswordDto();
		passwordDto.setOldPassword("");
		passwordDto.setNewPassword("123456789");
		ObjectMapper objectMapper = new ObjectMapper();
		String body = objectMapper.writeValueAsString(passwordDto);
		
		mockMvc.perform(put("/users/password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
				.andExpect(status().isBadRequest());
	}
}
