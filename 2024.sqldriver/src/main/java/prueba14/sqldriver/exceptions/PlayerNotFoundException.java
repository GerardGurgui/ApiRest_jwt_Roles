package prueba14.sqldriver.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@Getter
@Setter
public class PlayerNotFoundException extends RuntimeException {

    private HttpStatus httpStatus;

    private String message;

    public PlayerNotFoundException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public PlayerNotFoundException(String message, HttpStatus httpStatus, String message1) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = message1;
    }
}
