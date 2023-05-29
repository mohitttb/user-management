package fiftyfive.administration.usermanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequest {
    @NotEmpty(message = "Username is required")
    @JsonProperty("username")
    private String username;
    @NotEmpty(message = "Password is required")
    @JsonProperty("password")
    private String password;
}
