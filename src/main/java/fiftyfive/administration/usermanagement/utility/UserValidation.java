package fiftyfive.administration.usermanagement.utility;

import fiftyfive.administration.usermanagement.entity.User;
import fiftyfive.administration.usermanagement.exception.UserNotExistsException;
import fiftyfive.administration.usermanagement.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserValidation {
    public User isUserExists(UserRepository users, String constant, String userName) throws UserNotExistsException {
        return users.findAll().stream()
                .filter(user -> user.getUsername() != null && user.getUsername().equals(userName))
                .findFirst()
                .orElseThrow(() -> new UserNotExistsException(getExceptionMessage(constant, userName)));
    }

    private String getExceptionMessage(String constant, String userName) {
        return String.format(constant, userName);
    }

}
