package pl.rynski.adaimichal.dao.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class PasswordDto {
	@NotNull
	@NotBlank
	private String oldPassword;
	@NotNull                     
	@Size(min = 8, max = 25)
	private String newPassword;
}
