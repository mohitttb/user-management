package fiftyfive.administration.usermanagement.repository;
import fiftyfive.administration.usermanagement.entity.DeletedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeletedUserRepository extends JpaRepository<DeletedUser,Long> {
}