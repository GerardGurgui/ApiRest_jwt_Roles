package prueba14.sqldriver.exceptions;

import org.springframework.http.HttpStatus;

public class PlayerHaveRoleException extends RuntimeException{


    private HttpStatus httpStatus;
    private String message;

    public PlayerHaveRoleException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public PlayerHaveRoleException(String message, HttpStatus httpStatus, String message1) {
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
