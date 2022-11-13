package pl.rynski.adaimichal.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.rynski.adaimichal.dao.model.User;
import pl.rynski.adaimichal.dao.model.enums.NotificationType;
import pl.rynski.adaimichal.repository.UserRepository;
import pl.rynski.adaimichal.utils.DateUtils;

@Service
@RequiredArgsConstructor
public class NotificationService {
	
	private final JavaMailSender emailSender;
	private final UserRepository userRepository;
	
	@Value("${minutes.between.drawing}")
	private long minutesBetweenDrawing;
    @Value("${spring.mail.username}")
    private String sourceEmail;

	public void sendNewDrawnTaskNotification(Long id) {
		Optional<User> userToNotify = userRepository.findById(id);
		userToNotify.ifPresent(user -> {
			if(user.getEmail() != null && user.getEmail().length() != 0) {
				sendEmail(user.getEmail(), NotificationType.NEW_TASK);
			}
		});
	}
	
	@Scheduled(cron = "0 0 8,18 * * *")
	public void sendNewDrawnPossibilityNotification() {
		List<User> allUsers = userRepository.findAll();
		LocalDateTime currentTime = DateUtils.getCurrentDateTime();
		for(User user: allUsers) {
			if(user.getEmail() != null && user.getEmail().length() != 0 && user.getLastDateOfDrawingTask().plusMinutes(minutesBetweenDrawing).isBefore(currentTime) && user.getNotificationSend() == false) {
				sendEmail(user.getEmail(), NotificationType.DRAW_TIME);
				user.setNotificationSend(true);
			}
		}
		userRepository.saveAll(allUsers);
	}
	
	private void sendEmail(String email, NotificationType notificationType) {
		SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sourceEmail);
        message.setTo(email);
		switch (notificationType) {
		case DRAW_TIME -> {
	        message.setSubject("ADA I MICHU - Wylosuj nowe zadanie!");
	        message.setText("To już czas, wylosuj nowe zadanie i ciesz się możliwością zagrania asa z rękawa! :D");
	        emailSender.send(message);
		}
		case NEW_TASK -> {
	        message.setSubject("Twój partner wylosował nowe zadanie!");
	        message.setText("Zaloguj się do aplikacji aby zobaczyć co takiego tym razem na Ciebie ma! :D");
	        emailSender.send(message);
		}
		}
	}
}