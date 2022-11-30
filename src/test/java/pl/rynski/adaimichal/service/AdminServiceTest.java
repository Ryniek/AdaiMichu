package pl.rynski.adaimichal.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.rynski.adaimichal.dao.dto.request.GlobalSettingsDto;
import pl.rynski.adaimichal.dao.dto.response.GlobalSettingsResponse;
import pl.rynski.adaimichal.dao.model.GlobalSettings;
import pl.rynski.adaimichal.repository.GlobalSettingsRepository;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
	
	@Mock private GlobalSettingsRepository globalSettingsRepository;
	@InjectMocks private AdminService adminService;

	@Test
	void shouldFetchGlobalSettings() {
		GlobalSettings globalSettings = new GlobalSettings();
		globalSettings.setMinutesBetweenDrawing(100L);
		globalSettings.setResetPasswordTokenValidity(100L);
		when(globalSettingsRepository.getReferenceById(Mockito.anyLong())).thenReturn(globalSettings);
		
		GlobalSettingsResponse response = adminService.fetchGlobalSettings();
		
		assertEquals(globalSettings.getMinutesBetweenDrawing(), response.getMinutesBetweenDrawing());
		assertEquals(globalSettings.getResetPasswordTokenValidity(), response.getResetPasswordTokenValidity());
	}

	@Test
	void shouldSetGlobalSettings() {
		GlobalSettingsDto dto = new GlobalSettingsDto();
		dto.setMinutesBetweenDrawing(100L);
		dto.setResetPasswordTokenValidity(100L);
		GlobalSettings globalSettings = new GlobalSettings();
		globalSettings.setMinutesBetweenDrawing(100L);
		globalSettings.setResetPasswordTokenValidity(100L);
		when(globalSettingsRepository.getReferenceById(Mockito.anyLong())).thenReturn(globalSettings);
		when(globalSettingsRepository.save(Mockito.any())).thenReturn(globalSettings);

		GlobalSettingsResponse response = adminService.setGlobalSettings(dto);
		
		verify(globalSettingsRepository, times(1)).save(Mockito.any());
		assertEquals(response.getMinutesBetweenDrawing(), response.getMinutesBetweenDrawing());
		assertEquals(globalSettings.getResetPasswordTokenValidity(), response.getResetPasswordTokenValidity());
	}

}
