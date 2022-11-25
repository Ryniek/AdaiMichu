package pl.rynski.adaimichal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class WrongPasswordException extends RuntimeException {
	
    public WrongPasswordException() {
        super("Niepoprawne stare hasło");
    }
}
