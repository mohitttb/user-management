package fiftyfive.administration.usermanagement.dto;

public class BaseErrorDTO {
    private String message;

    public BaseErrorDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }


}
