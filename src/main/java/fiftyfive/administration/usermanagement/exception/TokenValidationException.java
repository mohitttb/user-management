package fiftyfive.administration.usermanagement.exception;

public class TokenValidationException extends RuntimeException {

    public TokenValidationException(String message) {
        super(message);
    }
}
