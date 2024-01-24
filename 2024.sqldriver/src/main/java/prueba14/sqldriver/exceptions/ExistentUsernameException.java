package prueba14.sqldriver.exceptions;

import org.springframework.http.HttpStatus;

public class ExistentUsernameException extends RuntimeException {

    private HttpStatus httpStatus;
    private String message;

    public ExistentUsernameException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public ExistentUsernameException(String message, HttpStatus httpStatus, String message1) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = message1;
    }

    public HttpStatus getHttpStatus(){
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
