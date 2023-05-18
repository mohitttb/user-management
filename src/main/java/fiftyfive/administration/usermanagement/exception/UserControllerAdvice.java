package fiftyfive.administration.usermanagement.exception;

import fiftyfive.administration.usermanagement.dto.BaseErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler(UserNotExistsException.class)
    public ResponseEntity<BaseErrorDTO> handleUserNotExistsException(UserNotExistsException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseErrorDTO(ex.getMessage()));
    }
    @ExceptionHandler(RecordAlreadyExistsException.class)
    public ResponseEntity<BaseErrorDTO> handleRecordAlreadyExistsException(RecordAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new BaseErrorDTO(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseErrorDTO> handleGlobalException(Exception ex) {
        BaseErrorDTO errorMessage = new BaseErrorDTO("An error occurred while processing the request.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseErrorDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseErrorDTO(ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseErrorDTO> handleMethodArgumentNotValid(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseErrorDTO(ex.getMessage()));
    }
}

