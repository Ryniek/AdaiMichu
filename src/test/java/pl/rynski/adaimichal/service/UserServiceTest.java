package pl.rynski.adaimichal.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import pl.rynski.adaimichal.dao.dto.request.PasswordDto;
import pl.rynski.adaimichal.dao.dto.request.ResetPasswordDto;
import pl.rynski.adaimichal.dao.dto.response.UserResponse;
import pl.rynski.adaimichal.dao.model.GlobalSettings;
import pl.rynski.adaimichal.dao.model.PasswordResetToken;
import pl.rynski.adaimichal.dao.model.User;
import pl.rynski.adaimichal.exception.ResourceNotFoundException;
import pl.rynski.adaimichal.exception.TooLateOperationException;
import pl.rynski.adaimichal.exception.WrongPasswordException;
import pl.rynski.adaimichal.repository.GlobalSettingsRepository;
import pl.rynski.adaimichal.repository.PasswordResetTokenRepository;
import pl.rynski.adaimichal.repository.UserRepository;
import pl.rynski.adaimichal.security.CustomUserDetailsService;
import pl.rynski.adaimichal.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	
	@InjectMocks private UserService userService;
	@Mock private UserRepository userRepository;
	@Mock private PasswordEncoder passwordEncoder;
	@Mock private CustomUserDetailsService userDetailsService;
	@Mock private PasswordResetTokenRepository passwordResetTokenRepository;
	@Mock private JavaMailSender javaMailSender;
	@Mock private NotificationService notificationService;
	@Mock private GlobalSettingsRepository globalSettingsRepository;
	
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
	
	@Test
	void shouldCreateResetPasswordToken() {
		User user = new User();
		String email = "email@test.pl";
		GlobalSettings globalSettings = new GlobalSettings();
		globalSettings.setResetPasswordTokenValidity(600L);
		when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
		when(globalSettingsRepository.getReferenceById(Mockito.anyLong())).thenReturn(globalSettings);
		
		userService.createResetPasswordToken(email);
		
		verify(passwordResetTokenRepository, times(1)).save(Mockito.any());
	}
	
	@Test
	void shouldNotFindUserWhenCreateResetPasswordToken() {
		String email = "email@test.pl";
		when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> userService.createResetPasswordToken(email))
			.isInstanceOf(ResourceNotFoundException.class);
		verify(passwordResetTokenRepository, times(0)).save(Mockito.any());
	}
	
	@Test
	void shouldValidateTokenAndSetPassword() {
		ResetPasswordDto dto = new ResetPasswordDto();
		dto.setToken("token");
		PasswordResetToken passwordResetToken = new PasswordResetToken();
		passwordResetToken.setUser(new User());
		passwordResetToken.setExpirationDate(DateUtils.getCurrentDateTime().plusDays(1));
		when(passwordResetTokenRepository.findByToken(dto.getToken())).thenReturn(Optional.of(passwordResetToken));
		
		userService.validateTokenAndSetPassword(dto);
		
		verify(userRepository, times(1)).save(Mockito.any());
		verify(passwordResetTokenRepository, times(1)).delete(Mockito.any());
	}
	
	@Test
	void shouldNotFindTokenWhenValidateTokenAndSetPassword() {
		ResetPasswordDto dto = new ResetPasswordDto();
		dto.setToken("token");
		when(passwordResetTokenRepository.findByToken(dto.getToken())).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> userService.validateTokenAndSetPassword(dto))
			.isInstanceOf(ResourceNotFoundException.class);
		verify(userRepository, times(0)).save(Mockito.any());
		verify(passwordResetTokenRepository, times(0)).delete(Mockito.any());
	}
	
	@Test
	void shouldThrowExpirationDatePassedWhenValidateTokenAndSetPassword() {
		ResetPasswordDto dto = new ResetPasswordDto();
		dto.setToken("token");
		PasswordResetToken passwordResetToken = new PasswordResetToken();
		passwordResetToken.setUser(new User());
		passwordResetToken.setExpirationDate(DateUtils.getCurrentDateTime().minusDays(1));
		when(passwordResetTokenRepository.findByToken(dto.getToken())).thenReturn(Optional.of(passwordResetToken));
		
		assertThatThrownBy(() -> userService.validateTokenAndSetPassword(dto))
			.isInstanceOf(TooLateOperationException.class);
		verify(userRepository, times(0)).save(Mockito.any());
		verify(passwordResetTokenRepository, times(0)).delete(Mockito.any());
	}
}
