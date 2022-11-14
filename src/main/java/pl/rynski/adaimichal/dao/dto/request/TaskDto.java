package pl.rynski.adaimichal.dao.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import pl.rynski.adaimichal.dao.model.Task;
import pl.rynski.adaimichal.dao.model.User;

@Data
public class TaskDto {
	@NotBlank
	@NotNull
	private String name;
	private String comment;
	@Min(1)
	@Max(90)
	private Long daysToUse;
	
	public static Task taskFromDto(TaskDto dto, User creator) {
		Task task = new Task();
		task.setName(dto.getName());
		task.setComment(dto.getComment());
		task.setDaysToUse(dto.getDaysToUse());
		task.setCreator(creator);
		return task;
	}
	
	public static Task editTaskFromDto(Task task, TaskDto taskDto) {
		task.setName(taskDto.getName());
		task.setComment(taskDto.getComment());
		task.setDaysToUse(taskDto.getDaysToUse());
		return task;
	}
}