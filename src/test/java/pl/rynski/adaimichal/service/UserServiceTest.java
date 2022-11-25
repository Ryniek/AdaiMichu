package pl.rynski.adaimichal.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import pl.rynski.adaimichal.dao.dto.request.PasswordDto;
import pl.rynski.adaimichal.dao.dto.response.UserResponse;
import pl.rynski.adaimichal.dao.model.User;
import pl.rynski.adaimichal.exception.WrongPasswordException;
import pl.rynski.adaimichal.repository.UserRepository;
import pl.rynski.adaimichal.security.CustomUserDetailsService;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	
	@InjectMocks private UserService userService;
	@Mock private UserRepository userRepository;
	@Mock private PasswordEncoder passwordEncoder;
	@Mock private CustomUserDetailsService userDetailsService;
	
	@Test
	void shouldReturnUser() {
		//given
		User user = new User();
		user.setName("Test");
		
		when(userDetailsService.getLoggedUser()).thenReturn(user);
		//when
		UserResponse response = userService.getUserDetails();
		//then
		assertEquals(user.getName(), response.getName());
	}

	@Test
	void shouldSetEmail() {
		//given
		User user = new User();
		
		when(userDetailsService.getLoggedUser()).thenReturn(user);
		when(userRepository.save(Mockito.any())).thenReturn(user);
		//when
		UserResponse response = userService.setEmail("test");
		//then
		verify(userRepository).save(user);
		assertEquals("test", response.getEmail());
	}

	@Test
	void shouldSetPassword() {
		PasswordDto passwordDto = new PasswordDto();
		passwordDto.setNewPassword("12345");
		User user = new User();
		user.setPassword("1234");
		
		when(userDetailsService.getLoggedUser()).thenReturn(user);
		when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);
		
		userService.setPassword(passwordDto);
		
		verify(userRepository).save(user);
	}

	@Test
	void shouldThrowWrongOldPasswordException() {
		PasswordDto passwordDto = new PasswordDto();
		User user = new User();
		
		when(userDetailsService.getLoggedUser()).thenReturn(user);
		when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(false);
		
		assertThatThrownBy(() -> userService.setPassword(passwordDto))
			.isInstanceOf(WrongPasswordException.class);
	}
}
