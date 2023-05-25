package fiftyfive.administration.usermanagement.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class CreateUserRequest {

    @JsonProperty(value = "username", required = true)
    @NotEmpty(message = "Username is required")
    private String username;

    @JsonProperty(value = "password", required = true)
    @NotEmpty(message = "Password is required")
    private String password;

    @JsonProperty(value = "first_name", required = true)
    @NotEmpty(message = "First Name is required")
    private String firstName;

    @JsonProperty(value = "last_name", required = true)
    @NotEmpty(message = "Last Name is required")
    private String lastName;

    @JsonProperty(value = "role", required = true)
    @NotEmpty(message = "Role is required")
    private String role;
}
