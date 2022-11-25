package pl.rynski.adaimichal.exception;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException (
            ResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorValidationMessage.getValidationError(exception.getMessage()));
    }
    
    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<?> handleWrongPasswordException (
    		WrongPasswordException exception) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ErrorValidationMessage.getValidationError(exception.getMessage()));
    }
    
    @ExceptionHandler(TooEarlyOperationException.class)
    public ResponseEntity<?> handleTooEarlyOperationException (
    		TooEarlyOperationException exception) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ErrorValidationMessage.getValidationError(exception.getMessage()));
    }
    
    @ExceptionHandler(NoTaskToDrawnException.class)
    public ResponseEntity<?> handleNoTaskToDrawnException (
    		NoTaskToDrawnException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorValidationMessage.getValidationError(exception.getMessage()));
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException exception) {
    	return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ErrorValidationMessage.getValidationError(exception.getMessage()));
    }
   
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                  HttpHeaders headers, HttpStatus status, WebRequest request) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorValidationMessage.getValidationErrors(ex.getBindingResult().getFieldErrors()));
    }
}
