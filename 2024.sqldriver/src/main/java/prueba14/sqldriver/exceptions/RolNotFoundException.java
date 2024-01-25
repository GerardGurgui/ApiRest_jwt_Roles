package prueba14.sqldriver.exceptions;

import org.springframework.http.HttpStatus;

public class RolNotFoundException extends RuntimeException{


    private final HttpStatus httpStatus;
    private final String message;

    public RolNotFoundException(HttpStatus httpStatus, String message){
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public RolNotFoundException(String message, HttpStatus httpStatus, String message2){
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
