package pl.rynski.adaimichal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.rynski.adaimichal.dao.dto.response.TaskResponse;
import pl.rynski.adaimichal.dao.model.Task;
import pl.rynski.adaimichal.dao.model.User;
import pl.rynski.adaimichal.repository.TaskRepository;
import pl.rynski.adaimichal.security.CustomUserDetailsService;

@Service
@RequiredArgsConstructor
public class TaskService {
	private final TaskRepository taskRepository;
	private final CustomUserDetailsService userDetailsService;
	
	public List<TaskResponse> getAllCurrentUserTasks() {
		User currentUser = userDetailsService.getLoggedUser();
		List<Task> allTasksByCreator = taskRepository.findAllByCreator(currentUser);
		return allTasksByCreator.stream().map(task -> TaskResponse.toResponse(task, currentUser)).toList();
	}
}
