package fiftyfive.administration.usermanagement.implemention;

import fiftyfive.administration.usermanagement.constant.Constant;
import fiftyfive.administration.usermanagement.dto.CreateUserRequest;
import fiftyfive.administration.usermanagement.dto.UpdateUserRequestData;
import fiftyfive.administration.usermanagement.dto.UserResponseData;
import fiftyfive.administration.usermanagement.exception.RecordAlreadyExistsException;
import fiftyfive.administration.usermanagement.exception.UserNotExistsException;
import fiftyfive.administration.usermanagement.mapper.UserRequestMapper;
import fiftyfive.administration.usermanagement.repository.DeletedUserRepository;
import fiftyfive.administration.usermanagement.repository.UserRepository;
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

    @Autowired
    UserRepository userRepository;

    @Autowired
    DeletedUserRepository deletedUserRepository;

    public UserService(UserRequestMapper userRequestMapper) {
        this.userRequestMapper = userRequestMapper;
    }

    public UserResponseData createUser(CreateUserRequest createUserRequest) throws RecordAlreadyExistsException {
        fiftyfive.administration.usermanagement.entity.User user = userRepository.findByUsername(createUserRequest.getUsername());
        if (user != null) {
            throw new RecordAlreadyExistsException(String.format(Constant.USER_ALREADY_EXISTS, createUserRequest.getUsername()));
        }
        return userRequestMapper.createUserMapper(createUserRequest, userRepository);
    }

    public UserResponseData updateUser(UpdateUserRequestData updateUserRequestData, String userName) throws UserNotExistsException {
        userValidation.isUserExists(userRepository, Constant.USER_NOT_EXISTS, userName);
        return userRequestMapper.updateUserMapper(updateUserRequestData, userRepository, userName);
    }

    public UserResponseData getUser(String userName) throws UserNotExistsException {
        userValidation.isUserExists(userRepository, Constant.USER_NOT_EXISTS, userName);
        return userRequestMapper.getUserMapper(userName, userRepository);
    }

    public UserResponseData deleteUser(String userName) throws UserNotExistsException {
        userValidation.isUserExists(userRepository, Constant.USER_NOT_EXISTS, userName);
        return userRequestMapper.getDeleteMapper(userName, userRepository, deletedUserRepository);
    }

    public List<UserResponseData> getAllUsers() {
        return userRequestMapper.getAllUserMapper(userRepository, UserResponseData.class);
    }

    public List<UserResponseData> getAllDeletedUsers() {
        return userRequestMapper.getAllUserMapper(deletedUserRepository, UserResponseData.class);
    }
}
