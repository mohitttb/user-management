package fiftyfive.administration.usermanagement.exception;

public class RecordAlreadyExistsException extends RuntimeException{
    public RecordAlreadyExistsException(String message) {
        super(message);
    }
}
