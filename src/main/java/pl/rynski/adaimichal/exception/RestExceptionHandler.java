package pl.rynski.adaimichal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException (
            ResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
    
    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<?> handleWrongPasswordException (
    		WrongPasswordException exception) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(exception.getMessage());
    }
    
    @ExceptionHandler(TooEarlyOperationException.class)
    public ResponseEntity<?> handleTooEarlyOperationException (
    		TooEarlyOperationException exception) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(exception.getMessage());
    }
    
    @ExceptionHandler(NoTaskToDrawnException.class)
    public ResponseEntity<?> handleNoTaskToDrawnException (
    		NoTaskToDrawnException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}
