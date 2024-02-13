package prueba14.sqldriver.exceptions;

import org.springframework.http.HttpStatus;

public class UserUnauthorizedException extends RuntimeException{

    private final HttpStatus httpStatus;
    private final String message;

    public UserUnauthorizedException(HttpStatus httpStatus, String message){
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public UserUnauthorizedException(String message, HttpStatus httpStatus, String message2){
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
