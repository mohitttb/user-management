package fiftyfive.administration.usermanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateUserRequest {
    private String username;
    private String password;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String role;
    @JsonProperty("created_at")
    private LocalDateTime createdAt=LocalDateTime.now();
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt=LocalDateTime.now();

    public void validate() throws IllegalArgumentException {
        if (username == null) {
            throw new IllegalArgumentException("username is null");
        }
        if (password == null) {
            throw new IllegalArgumentException("password is null");
        }
        if (firstName == null) {
            throw new IllegalArgumentException("First Name is null");
        }
        if (lastName == null) {
            throw new IllegalArgumentException("Last Name is null");
        }
        if (role == null) {
            throw new IllegalArgumentException("role is null");
        }
    }
}
