package fiftyfive.administration.usermanagement.exception;

import fiftyfive.administration.usermanagement.dto.CustomErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler(UserManagementException.class)
    public ResponseEntity<CustomErrorMessage> handleUserManagementException(UserManagementException ex) {
        CustomErrorMessage errorMessage = new CustomErrorMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorMessage> handleGlobalException(Exception ex) {
        CustomErrorMessage errorMessage = new CustomErrorMessage("An error occurred while processing the request.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomErrorMessage> handleMethodArgumentNotValid(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorMessage(ex.getMessage()));
    }
}

