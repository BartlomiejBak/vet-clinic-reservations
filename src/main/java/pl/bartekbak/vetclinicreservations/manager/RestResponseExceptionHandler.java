package pl.bartekbak.vetclinicreservations.manager;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.bartekbak.vetclinicreservations.exceptions.DataCollisionException;
import pl.bartekbak.vetclinicreservations.exceptions.IncompleteDataException;
import pl.bartekbak.vetclinicreservations.exceptions.InvalidCredentialsException;
import pl.bartekbak.vetclinicreservations.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({IncompleteDataException.class})
    public ResponseEntity<Object> handleIncompleteDataException(Exception e, WebRequest request) {
        return new ResponseEntity<Object>("Incomplete data, " + e.getMessage(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidCredentialsException.class})
    public ResponseEntity<Object> handleInvalidCredentialsException(Exception e, WebRequest request) {
        return new ResponseEntity<Object>("Invalid credentials, " + e.getMessage(),
                new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<Object> handleResourceNotFoundException(Exception e, WebRequest request) {
        return new ResponseEntity<Object>("Resource not found, " + e.getMessage(),
                new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DataCollisionException.class})
    public ResponseEntity<Object> handleDataCollisionException(Exception e, WebRequest request) {
        return new ResponseEntity<Object>("Data collision, " + e.getMessage(),
                new HttpHeaders(), HttpStatus.CONFLICT);
    }
}
