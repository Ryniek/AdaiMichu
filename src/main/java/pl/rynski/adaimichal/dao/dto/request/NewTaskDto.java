package pl.rynski.adaimichal.dao.dto.request;

import lombok.Data;

@Data
public class NewTaskDto {
	private String name;
	private String comment;
}
