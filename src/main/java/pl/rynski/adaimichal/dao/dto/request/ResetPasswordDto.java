package pl.rynski.adaimichal.dao.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ResetPasswordDto {
	
	@NotNull
	@NotBlank(message = "Token nie może być pusty")
	private String token;
	@NotNull                     
	@Size(min = 8, max = 25, message = "Minimalna ilość znaków w haśle: 8")
	private String newPassword;
}
