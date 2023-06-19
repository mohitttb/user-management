package fiftyfive.administration.usermanagement.entity;


import jakarta.persistence.*;
import lombok.Data;


import java.util.List;
@Entity(name = "api_role_permission")
@Data
public class ApiRolePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "endpoints", nullable = false)
    private String endpoints;
    @Column(name = "roles", columnDefinition = "text[]")
    @Convert(converter = StringListConverter.class)
    public List<String> roles;
}
