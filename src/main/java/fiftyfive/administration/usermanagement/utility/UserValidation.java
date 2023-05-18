package fiftyfive.administration.usermanagement.utility;

import fiftyfive.administration.usermanagement.dto.User;
import fiftyfive.administration.usermanagement.exception.UserNotExistsException;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class UserValidation {
    public void isUserExists(List<User> users, String constant, String userName) throws UserNotExistsException {
        users.stream()
                .filter(user -> user.getUsername() != null && user.getUsername().equals(userName))
                .findFirst()
                .orElseThrow(() -> new UserNotExistsException(getExceptionMessage(constant, userName)));
    }

    private String getExceptionMessage(String constant, String userName) {
        return constant + userName;
    }

}
