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
	private boolean isFinished;
	private boolean isStarted;
	private boolean isHidden;
	private LocalDateTime creationDate;
	private Long daysToUse;
	private LocalDateTime expirationDate;
	private LocalDateTime finishDate;
	private UserResponse creator;
	private UserResponse drawnUser;
	
	public static TaskResponse toResponse(Task task) {
		TaskResponse response = new TaskResponse();
		response.setId(task.getId());
		response.setName(task.getName());
		response.setComment(task.getComment());
		response.setFinished(task.getIsFinished());
		response.setStarted(task.getIsStarted());
		response.setHidden(task.getIsHidden());
		response.setCreationDate(task.getCreationDate());
		response.setDaysToUse(task.getDaysToUse());
		Optional.ofNullable(task.getExpirationDate())
			.ifPresent(expirationDate -> response.setExpirationDate(expirationDate));
		Optional.ofNullable(task.getFinishDate())
			.ifPresent(doneData -> response.setFinishDate(doneData));
		response.setCreator(UserResponse.toResponse(task.getCreator()));
		Optional.ofNullable(task.getDrawnUser())
			.ifPresent(drawnUser -> response.setDrawnUser(UserResponse.toResponse(drawnUser)));
		return response;
	}
}
