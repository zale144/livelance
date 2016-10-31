package livelance.app.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
	public NotFoundException() {
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }
}