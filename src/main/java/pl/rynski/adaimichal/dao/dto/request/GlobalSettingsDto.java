package pl.rynski.adaimichal.dao.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;
import pl.rynski.adaimichal.dao.model.GlobalSettings;

@Data
public class GlobalSettingsDto {
	
	@NotNull
	@Min(value = 60, message = "Minimalna wartość to 60")
	@Max(value = 86400, message = "Maksymalna wartość to 86400")
	private Long minutesBetweenDrawing;
	
	@NotNull 
	@Min(value = 5, message = "Minimalna wartość to 60")
	@Max(value = 86400, message = "Maksymalna wartość to 10080")
	private Long resetPasswordTokenValidity;
	
	public static GlobalSettings editFromDto(GlobalSettings globalSettings, GlobalSettingsDto dto) {
		globalSettings.setMinutesBetweenDrawing(dto.getMinutesBetweenDrawing());
		globalSettings.setResetPasswordTokenValidity(dto.getResetPasswordTokenValidity());
		return globalSettings;
	}
}
