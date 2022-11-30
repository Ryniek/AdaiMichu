package pl.rynski.adaimichal.dao.dto.response;

import lombok.Data;
import pl.rynski.adaimichal.dao.model.GlobalSettings;

@Data
public class GlobalSettingsResponse {
	
	private Long minutesBetweenDrawing;
	private Long resetPasswordTokenValidity;
	
	public static GlobalSettingsResponse toResponse(GlobalSettings globalSettings) {
		GlobalSettingsResponse response = new GlobalSettingsResponse();
		response.setMinutesBetweenDrawing(globalSettings.getMinutesBetweenDrawing());
		response.setResetPasswordTokenValidity(globalSettings.getResetPasswordTokenValidity());
		return response;
	}
}
