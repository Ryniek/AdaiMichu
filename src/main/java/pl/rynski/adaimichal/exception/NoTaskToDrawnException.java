package pl.rynski.adaimichal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoTaskToDrawnException extends RuntimeException {

    public NoTaskToDrawnException() {
        super("No task to drawn in pool");
    }
}
