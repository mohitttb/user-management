package fiftyfive.administration.usermanagement.implemention;

import fiftyfive.administration.usermanagement.dto.CreateUserRequest;
import fiftyfive.administration.usermanagement.dto.UpdateUserRequestData;
import fiftyfive.administration.usermanagement.dto.User;
import fiftyfive.administration.usermanagement.dto.UserResponseData;
import fiftyfive.administration.usermanagement.exception.UserManagementException;
import fiftyfive.administration.usermanagement.mapper.UserRequestMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private UserRequestMapper userRequestMapper = new UserRequestMapper();

    public UserResponseData createUser(CreateUserRequest createUserRequest, List<User> users) throws UserManagementException {
        for (User existingUser : users) {
            if (existingUser.getUsername().equals(createUserRequest.getUsername())) {
                throw new UserManagementException("User already exists");
            }
        }
        UserResponseData userResponseData = userRequestMapper.createUserMapper(createUserRequest, users);

        return userResponseData;
    }

    public UserResponseData updateUser(UpdateUserRequestData updateUserRequestData, List<User> users,String userName) throws UserManagementException {
        users.stream()
                .filter(user -> user.getUsername() != null && user.getUsername().equals(userName))
                .findFirst()
                .orElseThrow(() -> new UserManagementException("User not found with username: " + updateUserRequestData.getUsername()));

        UserResponseData userResponseData = userRequestMapper.updateUserMapper(updateUserRequestData, users,userName);
        return userResponseData;
    }

    public UserResponseData getUser(String userName, List<User> users) throws UserManagementException {
        users.stream()
                .filter(user -> user.getUsername() != null && user.getUsername().equals(userName))
                .findFirst()
                .orElseThrow(() -> new UserManagementException("User not found with username: " + userName));

        UserResponseData userResponseData = userRequestMapper.getUserMapper(userName, users);
        return userResponseData;
    }

    public UserResponseData deleteUser(String userName, List<User> users) throws UserManagementException {
        users.stream()
                .filter(user -> user.getUsername() != null && user.getUsername().equals(userName))
                .findFirst()
                .orElseThrow(() -> new UserManagementException("User not found with username: " + userName));

        UserResponseData userResponseData = userRequestMapper.getDeleteMapper(userName, users);
        return userResponseData;
    }

    public List<UserResponseData> getAllUsers(List<User> users) {

        List<UserResponseData> userResponseData = userRequestMapper.getAllUserMapper(users);
        return userResponseData;
    }
}
