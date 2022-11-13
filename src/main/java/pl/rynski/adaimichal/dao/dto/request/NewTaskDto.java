package pl.rynski.adaimichal.dao.dto.request;

import lombok.Data;
import pl.rynski.adaimichal.dao.model.Task;
import pl.rynski.adaimichal.dao.model.User;

@Data
public class NewTaskDto {
	private String name;
	private String comment;
	private Long daysToUse;
	
	public static Task taskFromDto(NewTaskDto dto, User creator) {
		Task task = new Task();
		task.setName(dto.getName());
		task.setComment(dto.getComment());
		task.setDaysToUse(dto.getDaysToUse());
		task.setCreator(creator);
		return task;
	}
}