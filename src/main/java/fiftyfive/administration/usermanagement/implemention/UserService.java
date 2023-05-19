package fiftyfive.administration.usermanagement.implemention;

import fiftyfive.administration.usermanagement.constant.Constant;
import fiftyfive.administration.usermanagement.dto.CreateUserRequest;
import fiftyfive.administration.usermanagement.dto.UpdateUserRequestData;
import fiftyfive.administration.usermanagement.dto.User;
import fiftyfive.administration.usermanagement.dto.UserResponseData;
import fiftyfive.administration.usermanagement.exception.RecordAlreadyExistsException;
import fiftyfive.administration.usermanagement.exception.UserNotExistsException;
import fiftyfive.administration.usermanagement.mapper.UserRequestMapper;
import fiftyfive.administration.usermanagement.utility.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    final UserRequestMapper userRequestMapper;

    @Autowired
    UserValidation userValidation;

    public UserService(UserRequestMapper userRequestMapper) {
        this.userRequestMapper = userRequestMapper;
    }

    public UserResponseData createUser(CreateUserRequest createUserRequest, List<User> users) throws RecordAlreadyExistsException {
        for (User existingUser : users) {
            if (existingUser.getUsername().equals(createUserRequest.getUsername())) {
                throw new RecordAlreadyExistsException(String.format(Constant.USER_ALREADY_EXISTS, createUserRequest.getUsername()));
            }
        }
        return userRequestMapper.createUserMapper(createUserRequest, users);
    }

    public UserResponseData updateUser(UpdateUserRequestData updateUserRequestData, List<User> users, String userName) throws UserNotExistsException {
        userValidation.isUserExists(users, Constant.USER_NOT_EXISTS, userName);
        return userRequestMapper.updateUserMapper(updateUserRequestData, users, userName);
    }

    public UserResponseData getUser(String userName, List<User> users) throws UserNotExistsException {
        userValidation.isUserExists(users, Constant.USER_NOT_EXISTS, userName);
        return userRequestMapper.getUserMapper(userName, users);
    }

    public UserResponseData deleteUser(String userName, List<User> users) throws UserNotExistsException {
        userValidation.isUserExists(users, Constant.USER_NOT_EXISTS, userName);
        return userRequestMapper.getDeleteMapper(userName, users);
    }

    public List<UserResponseData> getAllUsers(List<User> users) {
        return userRequestMapper.getAllUserMapper(users);
    }
}
