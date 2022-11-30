package pl.rynski.adaimichal.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.rynski.adaimichal.dao.dto.request.GlobalSettingsDto;
import pl.rynski.adaimichal.dao.dto.response.GlobalSettingsResponse;
import pl.rynski.adaimichal.dao.model.GlobalSettings;
import pl.rynski.adaimichal.repository.GlobalSettingsRepository;

@Service
@RequiredArgsConstructor
public class AdminService {
	
	private final GlobalSettingsRepository globalSettingsRepository;
	
	public GlobalSettingsResponse fetchGlobalSettings() {
		return GlobalSettingsResponse.toResponse(globalSettingsRepository.getReferenceById(1L));
	}
	
	public GlobalSettingsResponse setGlobalSettings(GlobalSettingsDto dto) {
		GlobalSettings globalSettings = globalSettingsRepository.getReferenceById(1L);
		return GlobalSettingsResponse.toResponse(globalSettingsRepository.save(GlobalSettingsDto.editFromDto(globalSettings, dto)));
	}
}
