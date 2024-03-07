package prueba14.sqldriver.exceptions.globalExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import prueba14.sqldriver.exceptions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    //HANDLE SPECIFIC EXCEPTIONS

    //RESOURCE NOT FOUND EXCEPTION
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {

        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(new Date());
        errorDetails.setMessage("Resource Not Found");
        errorDetails.setErrors(errors);

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    //PLAYERS--> PLAYER NOT FOUND EXCEPTION
    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<ErrorDetails> handlePlayerNotFoundException(PlayerNotFoundException ex, WebRequest request) {

        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(new Date());
        errorDetails.setMessage("Player Not Found");
        errorDetails.setErrors(errors);

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    //PLAYERS--> USER NAME NOT FOUND EXCEPTION
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {

        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(new Date());
        errorDetails.setMessage("Username Not Found");
        errorDetails.setErrors(errors);

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    //PLAYERS--> EXISTENT USER NAME EXCEPTION
    @ExceptionHandler(ExistentUsernameException.class)
    public ResponseEntity<ErrorDetails> handleExistentUserNameException(ExistentUsernameException ex, WebRequest request) {

        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(new Date());
        errorDetails.setMessage("Existent Username");
        errorDetails.setErrors(errors);

        return new ResponseEntity<>(errorDetails, HttpStatus.FOUND);
    }

    //PLAYERS-->EXISTENT EMAIL USER
    @ExceptionHandler(ExistentEmailException.class)
    public ResponseEntity<ErrorDetails> handleExistentEmailException(ExistentEmailException ex, WebRequest request) {

        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(new Date());
        errorDetails.setMessage("Existent Email");
        errorDetails.setErrors(errors);

        return new ResponseEntity<>(errorDetails, HttpStatus.FOUND);
    }

    //ROLES-->ROLE NOT FOUND EXCEPTION
    @ExceptionHandler(RolNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleRolNotFoundException(RolNotFoundException ex, WebRequest request) {

        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(new Date());
        errorDetails.setMessage("Role Not Found");
        errorDetails.setErrors(errors);

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    //ROLES-->ADMIN ALREADY EXISTS EXCEPTION
    @ExceptionHandler(AdminAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handleAdminAlreadyExistsException(AdminAlreadyExistsException ex, WebRequest request) {

        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(new Date());
        errorDetails.setMessage("Admin Already Exists");
        errorDetails.setErrors(errors);

        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    //ROLES-->PLAYER ALREADY HAS ROLE EXCEPTION
    @ExceptionHandler(PlayerHaveRoleException.class)
    public ResponseEntity<ErrorDetails> handlePlayerHaveRoleException(PlayerHaveRoleException ex, WebRequest request) {

        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(new Date());
        errorDetails.setMessage("Player Have Role");
        errorDetails.setErrors(errors);

        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    //ROLES --> USER UNAUTHORIZED EXCEPTION
    @ExceptionHandler(UserUnauthorizedException.class)
    public ResponseEntity<ErrorDetails> handleUserUnauthorizedException(UserUnauthorizedException ex, WebRequest request) {

        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(new Date());
        errorDetails.setMessage("User Unauthorized");
        errorDetails.setErrors(errors);

        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {

        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(new Date());
        errorDetails.setMessage("Access Denied");
        errorDetails.setErrors(errors);

        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    //DICE--> PLAYER NO DICE THROWS EXCEPTION
    @ExceptionHandler(PlayerNoDiceThrowsException.class)
    public ResponseEntity<ErrorDetails> handlePlayerNoDiceThrowsException(PlayerNoDiceThrowsException ex, WebRequest request) {

        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(new Date());
        errorDetails.setMessage("Player No Dice Throws");
        errorDetails.setErrors(errors);

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    //DICE--> PLAYER HAS THROWS EXCEPTION
    @ExceptionHandler(PlayerHasThrowsException.class)
    public ResponseEntity<ErrorDetails> handlePlayerHasThrowsException(PlayerHasThrowsException ex, WebRequest request) {

        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(new Date());
        errorDetails.setMessage("Player Has Throws and can't be deleted");
        errorDetails.setErrors(errors);

        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    //MANEJAR VALIDACION DE DATOS INTRODUCIDOS POR EL USUARIO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(new Date());
        errorDetails.setMessage("Validation Failed");
        errorDetails.setErrors(errors);

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
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
