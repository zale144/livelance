package livelance.app.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value= HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {
    public ConflictException() {
    }

    public ConflictException(Throwable cause) {
        super(cause);
    }
}