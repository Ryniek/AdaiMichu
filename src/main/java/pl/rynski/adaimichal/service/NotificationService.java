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
import pl.rynski.adaimichal.repository.GlobalSettingsRepository;
import pl.rynski.adaimichal.repository.UserRepository;
import pl.rynski.adaimichal.utils.DateUtils;

@Service
@RequiredArgsConstructor
public class NotificationService {
	
	private final JavaMailSender emailSender;
	private final UserRepository userRepository;
	private final GlobalSettingsRepository globalSettingsRepository;
	
    @Value("${spring.mail.username}")
    private String sourceEmail;
    @Value("${front.url.address}")
    private String frontUrl;

	public void sendNewDrawnTaskNotification(Long id) {
		Optional<User> userToNotify = userRepository.findById(id);
		userToNotify.ifPresent(user -> {
			if(user.getEmail() != null && user.getEmail().length() != 0) {
				sendEmail(user.getEmail(), NotificationType.NEW_TASK, null);
			}
		});
	}
	
	@Scheduled(cron = "0 0 8,18 * * *")
	public void sendNewDrawnPossibilityNotification() {
		List<User> allUsers = userRepository.findAll();
		LocalDateTime currentTime = DateUtils.getCurrentDateTime();
		for(User user: allUsers) {
			if(user.getEmail() != null 
					&& user.getEmail().length() != 0 
					&& user.getLastDateOfDrawingTask().plusMinutes(globalSettingsRepository.getReferenceById(1L).getMinutesBetweenDrawing()).isBefore(currentTime) 
					&& user.getNotificationSend() == false) {
				sendEmail(user.getEmail(), NotificationType.DRAW_TIME, null);
				user.setNotificationSend(true);
			}
		}
		userRepository.saveAll(allUsers);
	}
	
	public void sendEmail(String email, NotificationType notificationType, String resetToken) {
		SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sourceEmail);
        message.setTo(email);
		switch (notificationType) {
		case DRAW_TIME -> {
	        message.setSubject("ADA I MICHU - Wylosuj nowe zadanie!");
	        message.setText("To ju?? czas, wylosuj nowe zadanie i ciesz si?? mo??liwo??ci?? zagrania asa z r??kawa! :D");
	        emailSender.send(message);
		}
		case NEW_TASK -> {
	        message.setSubject("Tw??j partner wylosowa?? nowe zadanie!");
	        message.setText("Zaloguj si?? do aplikacji aby zobaczy?? co takiego tym razem na Ciebie ma! :D");
	        emailSender.send(message);
		}
		case RESET_PASSWORD -> {
	        message.setSubject("Zresetuj has??o - Adaimichu");
	        message.setText("Aby zresetowa?? has??o wejd?? w link: " + frontUrl + "reset?token=" + resetToken);
	        emailSender.send(message);
		}
		}
	}
}
