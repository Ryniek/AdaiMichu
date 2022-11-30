package pl.rynski.adaimichal.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.rynski.adaimichal.dao.dto.request.GlobalSettingsDto;
import pl.rynski.adaimichal.dao.dto.response.GlobalSettingsResponse;
import pl.rynski.adaimichal.dao.model.GlobalSettings;
import pl.rynski.adaimichal.dao.model.Task;
import pl.rynski.adaimichal.exception.ResourceNotFoundException;
import pl.rynski.adaimichal.repository.GlobalSettingsRepository;
import pl.rynski.adaimichal.repository.TaskRepository;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
	
	@Mock private GlobalSettingsRepository globalSettingsRepository;
	@Mock private TaskRepository taskRepository;
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
	
	@Test
	void shouldDeleteFinishedTask() {
		Task task = new Task();
		task.setId(1L);
		when(taskRepository.findByIdAndIsFinished(Mockito.anyLong())).thenReturn(Optional.of(task));

		adminService.deleteFinishedTask(1L);
		
		verify(taskRepository, times(1)).delete(Mockito.any());
	}

	@Test
	void shouldThrowTaskNotFoundWhenDeleteFinishedTask() {
		when(taskRepository.findByIdAndIsFinished(Mockito.anyLong())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> adminService.deleteFinishedTask(Mockito.anyLong()))
			.isInstanceOf(ResourceNotFoundException.class);
		
		verify(taskRepository, times(0)).delete(Mockito.any());
	}
}
