package fiftyfive.administration.usermanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fiftyfive.administration.usermanagement.entity.AccessPermissions;
import lombok.Data;

import java.util.List;
@Data
public class YourEntityDTO {
    @JsonProperty("permissions")
    private List<AccessPermissions> permissions;

    // Getters and setters
}
