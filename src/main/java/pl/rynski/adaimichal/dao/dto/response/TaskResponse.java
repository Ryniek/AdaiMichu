package pl.rynski.adaimichal.dao.dto.response;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.Data;
import pl.rynski.adaimichal.dao.model.Task;

@Data
public class TaskResponse {
	private Long id;
	private String name;
	private String comment;
	private boolean isDone;
	private boolean isStarted;
	private boolean isHidden;
	private LocalDateTime creationDate;
	private LocalDateTime expirationDate;
	private LocalDateTime doneDate;
	private UserResponse creator;
	
	public static TaskResponse toResponse(Task task) {
		TaskResponse response = new TaskResponse();
		response.setId(task.getId());
		response.setName(task.getName());
		response.setComment(task.getComment());
		response.setDone(task.getIsDone());
		response.setStarted(task.getIsStarted());
		response.setHidden(task.getIsHidden());
		response.setCreationDate(task.getCreationDate());
		Optional.ofNullable(task.getExpirationDate()).ifPresent(expirationDate -> response.setExpirationDate(expirationDate));
		Optional.ofNullable(task.getDoneDate()).ifPresent(doneData -> response.setDoneDate(doneData));
		response.setCreator(UserResponse.toResponse(task.getCreator()));
		return response;
	}
}
