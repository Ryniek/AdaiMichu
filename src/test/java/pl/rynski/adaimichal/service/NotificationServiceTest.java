package pl.rynski.adaimichal.service;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import pl.rynski.adaimichal.dao.model.User;
import pl.rynski.adaimichal.repository.UserRepository;
import pl.rynski.adaimichal.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
	
	@Mock private JavaMailSender javaMailSender;
	@Mock private UserRepository userRepository;
	@InjectMocks private NotificationService notificationService;
	
	@Test
	void shouldSendNewDrawnTaskNotification() {
		User user = new User();
		user.setEmail("Test");
		when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
		
		notificationService.sendNewDrawnTaskNotification(1L);

		verify(javaMailSender).send(Mockito.any(SimpleMailMessage.class));
	}
	
	@Test
	void shouldNotSendNewDrawnTaskNotification() {
		User user = new User();
		when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
		
		notificationService.sendNewDrawnTaskNotification(1L);

		verify(javaMailSender, never()).send(Mockito.any(SimpleMailMessage.class));
	}

	@Test
	void shouldSendNewDrawnPossibilityNotification() {
		List<User> users = new ArrayList<>();
		User user = new User();
		user.setEmail("Test");
		user.setNotificationSend(false);
		user.setLastDateOfDrawingTask(DateUtils.getCurrentDateTime());
		users.add(user);
		when(userRepository.findAll()).thenReturn(users);
		
		notificationService.sendNewDrawnPossibilityNotification();

		verify(javaMailSender).send(Mockito.any(SimpleMailMessage.class));
		verify(userRepository).saveAll(Mockito.anyIterable());
	}

	@Test
	void shouldNotSendNewDrawnPossibilityNotification() {
		List<User> users = new ArrayList<>();
		User user = new User();
		user.setNotificationSend(false);
		user.setLastDateOfDrawingTask(DateUtils.getCurrentDateTime());
		users.add(user);
		when(userRepository.findAll()).thenReturn(users);
		
		notificationService.sendNewDrawnPossibilityNotification();

		verify(javaMailSender, never()).send(Mockito.any(SimpleMailMessage.class));
		verify(userRepository).saveAll(Mockito.anyIterable());
	}
}
