package fiftyfive.administration.usermanagement.implemention;

import fiftyfive.administration.usermanagement.constant.Constant;
import fiftyfive.administration.usermanagement.dto.CreateUserRequest;
import fiftyfive.administration.usermanagement.dto.UpdateUserRequestData;
import fiftyfive.administration.usermanagement.dto.UserResponseData;
import fiftyfive.administration.usermanagement.entity.DeletedUser;
import fiftyfive.administration.usermanagement.entity.User;
import fiftyfive.administration.usermanagement.exception.RecordAlreadyExistsException;
import fiftyfive.administration.usermanagement.exception.UserNotExistsException;
import fiftyfive.administration.usermanagement.mapper.UserRequestMapper;
import fiftyfive.administration.usermanagement.repository.DeletedUserRepository;
import fiftyfive.administration.usermanagement.repository.UserRepository;
import fiftyfive.administration.usermanagement.utility.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRequestMapper userRequestMapper;

    @Autowired
    UserValidation userValidation;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DeletedUserRepository deletedUserRepository;


    public UserResponseData createUser(CreateUserRequest createUserRequest) throws RecordAlreadyExistsException {
        String username = createUserRequest.getUsername();
        User user = userRepository.findByUsername(username);
        if (user != null) {
            throw new RecordAlreadyExistsException(String.format(Constant.USER_ALREADY_EXISTS, username));
        }
        user = userRequestMapper.mapToUser(createUserRequest);
        userRepository.save(user);
        createUserRequest.setId(user.getId());
        return userRequestMapper.createUserResponseMapper(user);
    }


    public UserResponseData updateUser(UpdateUserRequestData updateUserRequestData, Long userId) throws UserNotExistsException {
        userValidation.isUserExists(Constant.USER_NOT_EXISTS, userId);
        Optional<User> user = userRepository.findById(userId);
        UserResponseData userResponseData = userRequestMapper.updateUserMapper(updateUserRequestData, user);
        userRepository.save(user.orElseThrow(() -> new UserNotExistsException(Constant.USER_NOT_EXISTS)));
        return userResponseData;
    }

    public UserResponseData getUser(Long userId) throws UserNotExistsException {
        userValidation.isUserExists(Constant.USER_NOT_EXISTS, userId);
        return userRequestMapper.getUserMapper(userRepository.findById(userId));
    }

    public void deleteUser(Long userId) throws UserNotExistsException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotExistsException(String.format(Constant.USER_NOT_EXISTS, userId));
        }
        DeletedUser deletedUser = new DeletedUser();
        UserResponseData userResponseData = userRequestMapper.getDeleteMapper(deletedUser, user);
        deletedUserRepository.save(deletedUser);
        userRepository.deleteById(deletedUser.getUserId());
    }

    public List<UserResponseData> getAllUsers() {
        List<User> users= userRepository.findAll();
        return userRequestMapper.getAllUserMapper(users, UserResponseData.class);
    }

    public List<UserResponseData> getAllDeletedUsers() {
        List<DeletedUser> deletedUsers= deletedUserRepository.findAll();
        return userRequestMapper.getAllUserMapper(deletedUsers, UserResponseData.class);
    }

}
