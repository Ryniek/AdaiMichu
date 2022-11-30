package pl.rynski.adaimichal.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.rynski.adaimichal.dao.dto.request.PasswordDto;
import pl.rynski.adaimichal.dao.dto.request.ResetPasswordDto;
import pl.rynski.adaimichal.dao.dto.response.UserResponse;
import pl.rynski.adaimichal.dao.model.PasswordResetToken;
import pl.rynski.adaimichal.dao.model.User;
import pl.rynski.adaimichal.dao.model.enums.NotificationType;
import pl.rynski.adaimichal.exception.ResourceNotFoundException;
import pl.rynski.adaimichal.exception.TooLateOperationException;
import pl.rynski.adaimichal.exception.WrongPasswordException;
import pl.rynski.adaimichal.repository.GlobalSettingsRepository;
import pl.rynski.adaimichal.repository.PasswordResetTokenRepository;
import pl.rynski.adaimichal.repository.UserRepository;
import pl.rynski.adaimichal.security.CustomUserDetailsService;
import pl.rynski.adaimichal.utils.DateUtils;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	private final CustomUserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;
	private final PasswordResetTokenRepository passwordResetTokenRepository;
	private final NotificationService notificationService;
	private final GlobalSettingsRepository globalSettingsRepository;
	
	public UserResponse getUserDetails() {
		User currentUser = userDetailsService.getLoggedUser();
		return UserResponse.toResponse(currentUser);
	}
	
	public UserResponse setEmail(String email) {
		User currentUser = userDetailsService.getLoggedUser();
		currentUser.setEmail(email);
		return UserResponse.toResponse(userRepository.save(currentUser));
	}
	
	public void setPassword(PasswordDto passwordDto) {
		User currentUser = userDetailsService.getLoggedUser();
		if(!passwordEncoder.matches(passwordDto.getOldPassword(), currentUser.getPassword())) throw new WrongPasswordException();
		currentUser.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
		userRepository.save(currentUser);
	}
	
	public void createResetPasswordToken(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
		String token;
		if(user.getPasswordResetToken() != null) {
			token = renewPasswordResetToken(user.getPasswordResetToken());
		} else {
			token = generatePasswordResetToken(user);
		}
		notificationService.sendEmail(email, NotificationType.RESET_PASSWORD, token);
	}
	
	public void validateTokenAndSetPassword(ResetPasswordDto resetPasswordDto) {
		PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(resetPasswordDto.getToken())
				.orElseThrow(() -> new ResourceNotFoundException("Token", "name", resetPasswordDto.getToken()));
		if(passwordResetToken.getExpirationDate().isBefore(DateUtils.getCurrentDateTime())) throw new TooLateOperationException();
		User user = passwordResetToken.getUser();
		user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
		userRepository.save(user);
		passwordResetTokenRepository.delete(passwordResetToken);
	}
	
	private String generatePasswordResetToken(User user) {
		String token = UUID.randomUUID().toString();
		PasswordResetToken passwordResetToken = new PasswordResetToken();
		passwordResetToken.setToken(token);
		passwordResetToken.setUser(user);
		passwordResetToken.setExpirationDate(DateUtils.getCurrentDateTime().plusMinutes(globalSettingsRepository.getReferenceById(1L).getResetPasswordTokenValidity()));
		passwordResetTokenRepository.save(passwordResetToken);
		return token;
	}
	
	private String renewPasswordResetToken(PasswordResetToken passwordResetToken) {
		String token = UUID.randomUUID().toString();
		passwordResetToken.setToken(token);
		passwordResetToken.setExpirationDate(DateUtils.getCurrentDateTime().plusMinutes(globalSettingsRepository.getReferenceById(1L).getResetPasswordTokenValidity()));
		passwordResetTokenRepository.save(passwordResetToken);
		return token;
	}
}
