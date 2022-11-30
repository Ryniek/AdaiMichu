package pl.rynski.adaimichal.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.FieldError;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ErrorValidationMessage {
	
	private List<SimpleValidationError> simpleError = new ArrayList<>();
	
	@AllArgsConstructor
	@Data
	static class SimpleValidationError { 
		private String field;
		private String message;
	}
	
	public static List<SimpleValidationError> getValidationErrors(List<FieldError> fieldErrors) {
		ErrorValidationMessage errorValidationMessage = new ErrorValidationMessage();
		for(FieldError error : fieldErrors) {
			errorValidationMessage.getSimpleError().add(new SimpleValidationError(error.getField(), error.getDefaultMessage()));
		}
		return errorValidationMessage.getSimpleError();
	}
	
	public static List<SimpleValidationError> getValidationError(String errorMessage) {
		ErrorValidationMessage errorValidationMessage = new ErrorValidationMessage();
		errorValidationMessage.getSimpleError().add(new SimpleValidationError("",errorMessage));
		return errorValidationMessage.getSimpleError();
	}
}
