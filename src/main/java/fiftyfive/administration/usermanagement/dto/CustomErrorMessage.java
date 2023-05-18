package fiftyfive.administration.usermanagement.dto;

public class CustomErrorMessage {
    private String message;

    public CustomErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
