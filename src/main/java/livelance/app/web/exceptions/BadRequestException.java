package livelance.app.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    public BadRequestException() {
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }
}