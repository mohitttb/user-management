package fiftyfive.administration.usermanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data
public class CreateUserRequest {
    @JsonProperty(value = "username",required = true)
//    @NotEmpty(message = "Username is required")
    @NotNull(message = "Username is required")
//    @NotBlank
    private String username;

    @JsonProperty(value = "password",required = true)
//    @NotEmpty(message = "Password is required")
    @NotNull(message = "Password is required")
//    @NotBlank
    private String password;

    @JsonProperty(value = "first_name",required = true)
//    @NotEmpty(message = "First Name is required")
    @NotNull(message = "First Name is required")
//    @NotBlank
    private String firstName;

    @JsonProperty(value = "last_name",required = true)
//    @NotEmpty(message = "Last Name is required")
    @NotNull(message = "Last Name is required")
//    @NotBlank
    private String lastName;

    @JsonProperty(value = "role",required = true)
//    @NotEmpty(message = "Role is required")
    @NotNull(message = "Role is required")
//    @NotBlank
    private String role;
}
