package fiftyfive.administration.usermanagement.utility;

import fiftyfive.administration.usermanagement.entity.User;
import fiftyfive.administration.usermanagement.exception.UserNotExistsException;
import fiftyfive.administration.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserValidation {
    @Autowired
    UserRepository userRepository;
    public User isUserExists(String constant, Long userId) throws UserNotExistsException {
        return userRepository.findAll().stream()
                .filter(user -> user.getId() != null && user.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new UserNotExistsException(getExceptionMessage(constant,userId.toString())));
    }

    private String getExceptionMessage(String constant, String userName) {
        return String.format(constant, userName);
    }

}
