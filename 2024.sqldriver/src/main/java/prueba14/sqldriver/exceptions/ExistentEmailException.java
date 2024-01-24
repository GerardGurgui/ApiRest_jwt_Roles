package prueba14.sqldriver.exceptions;

import org.springframework.http.HttpStatus;

public class ExistentEmailException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;

    public ExistentEmailException(HttpStatus httpStatus, String message){
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public ExistentEmailException(String message, HttpStatus httpStatus, String message2){
        super(message);
        this.httpStatus = httpStatus;
        this.message = message2;
    }

    public HttpStatus getHttpStatus(){
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
