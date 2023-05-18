package fiftyfive.administration.usermanagement.mapper;

import fiftyfive.administration.usermanagement.dto.CreateUserRequest;
import fiftyfive.administration.usermanagement.dto.UpdateUserRequestData;
import fiftyfive.administration.usermanagement.dto.User;
import fiftyfive.administration.usermanagement.dto.UserResponseData;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserRequestMapper {
    ModelMapper modelMapper = new ModelMapper();

    public UserResponseData createUserMapper(CreateUserRequest createUserRequest, List<User> users) {
        User user = modelMapper.map(createUserRequest, User.class);
        users.add(user);
        return modelMapper.map(user, UserResponseData.class);
    }

    public UserResponseData updateUserMapper(UpdateUserRequestData updateUserRequestData, List<User> users, String username) {
        UserResponseData userResponseData = new UserResponseData();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                Optional.ofNullable(updateUserRequestData.getFirstName()).ifPresent(user::setFirstName);
                Optional.ofNullable(updateUserRequestData.getLastName()).ifPresent(user::setLastName);
                Optional.ofNullable(updateUserRequestData.getPassword()).ifPresent(user::setPassword);
                Optional.ofNullable(updateUserRequestData.getRole()).ifPresent(user::setRole);
                user.setUpdatedAt(LocalDateTime.now());
                userResponseData = modelMapper.map(user, UserResponseData.class);
            }
        }
        return userResponseData;
    }

    public UserResponseData getUserMapper(String userName, List<User> users) {
        UserResponseData userResponseData = new UserResponseData();
        for (User user : users) {
            if (userName != null && user.getUsername().equals(userName)) {
                userResponseData = modelMapper.map(user, UserResponseData.class);
            }
        }
        return userResponseData;
    }

    public UserResponseData getDeleteMapper(String userName, List<User> users) {
        UserResponseData userResponseData = new UserResponseData();
        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (userName != null && user.getUsername().equals(userName)) {
                user.setDeletedAt(LocalDateTime.now());
                userResponseData = modelMapper.map(user, UserResponseData.class);
                iterator.remove();
                break;
            }
        }

        return userResponseData;
    }

    public List<UserResponseData> getAllUserMapper(List<User> users) {
        return users.stream()
                .map(user -> modelMapper.map(user, UserResponseData.class))
                .toList();
    }
}
