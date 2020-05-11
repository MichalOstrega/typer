package pl.most.typer.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@EqualsAndHashCode(callSuper = true)
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadResourceException extends RuntimeException {

    String errorMessage;

    public BadResourceException() {
    }

    public BadResourceException(String message) {
        super(message);
    }

    public BadResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadResourceException(Throwable cause) {
        super(cause);
    }

    public BadResourceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
