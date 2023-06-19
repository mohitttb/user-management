package fiftyfive.administration.usermanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;


import java.util.List;

@Entity
@Data
@Table(name = "your_entity", schema = "public")
public class YourEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


//    @Column(columnDefinition = "text[]")
//    @Type(type = "string-array")
//    public List<AccessPermissions> permissions;


}
