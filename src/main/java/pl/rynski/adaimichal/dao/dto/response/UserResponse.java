package pl.rynski.adaimichal.dao.dto.response;

import lombok.Data;
import pl.rynski.adaimichal.dao.model.User;

@Data
public class UserResponse {
	private Long id;
	private String name;
	private String email;
	
    public static UserResponse toResponse(User user) {
        UserResponse result = new UserResponse();
        result.setId(user.getId());
        result.setName(user.getName());
        result.setEmail(user.getEmail());
        return result;
    }
}
