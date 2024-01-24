package prueba14.sqldriver.exceptions.globalExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import prueba14.sqldriver.exceptions.ExistentEmailException;
import prueba14.sqldriver.exceptions.ExistentUsernameException;
import prueba14.sqldriver.exceptions.PlayerNotFoundException;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //HANDLE SPECIFIC EXCEPTIONS

    //RESOURCE NOT FOUND EXCEPTION
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException,
                                                             WebRequest webRequest) {

        ErrorDetails errorDetails = new ErrorDetails(new Date(), resourceNotFoundException.getMessage(),
                webRequest.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    //PLAYERS--> PLAYER NOT FOUND EXCEPTION
    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<?> handlePlayerNotFoundException(PlayerNotFoundException playerNotFoundException,
                                                           WebRequest webRequest) {

        ErrorDetails errorDetails = new ErrorDetails(new Date(), playerNotFoundException.getMessage(),
                webRequest.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);

    }

    //PLAYERS--> EXISTENT USER NAME EXCEPTION
    @ExceptionHandler(ExistentUsernameException.class)
    public ResponseEntity<?> handleExistentUserNameException(ExistentUsernameException existentUserNameException,
                                                             WebRequest webRequest) {

        ErrorDetails errorDetails = new ErrorDetails(new Date(), existentUserNameException.getMessage(),
                webRequest.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.FOUND);

    }

    //PLAYERS-->EXISTENT EMAIL USER
    @ExceptionHandler(ExistentEmailException.class)
    public ResponseEntity<?> handleExistentEmailException(ExistentEmailException existentEmailException,
                                                          WebRequest webRequest) {

        ErrorDetails errorDetails = new ErrorDetails(new Date(), existentEmailException.getMessage(),
                webRequest.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.FOUND);

    }



    //HANDLE GLOBAL EXCEPTIONS
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception exception,
                                                   WebRequest webRequest) {

        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
