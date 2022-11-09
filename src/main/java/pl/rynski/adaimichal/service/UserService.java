package pl.rynski.adaimichal.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.rynski.adaimichal.dao.dto.request.PasswordDto;
import pl.rynski.adaimichal.dao.dto.response.UserResponse;
import pl.rynski.adaimichal.dao.model.User;
import pl.rynski.adaimichal.exception.WrongPasswordException;
import pl.rynski.adaimichal.repository.UserRepository;
import pl.rynski.adaimichal.security.CustomUserDetailsService;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final CustomUserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;
	
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
}
