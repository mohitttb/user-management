package fiftyfive.administration.usermanagement.repository;

import fiftyfive.administration.usermanagement.entity.YourEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YourRepository extends JpaRepository<YourEntity,Long> {
}
