package pl.rynski.adaimichal.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class TooEarlyOperationException extends RuntimeException {
	
    public TooEarlyOperationException(LocalDateTime dateOfPossibilityToDraw) {
        super(dateOfPossibilityToDraw.toString());
    }

}
