package fiftyfive.administration.usermanagement.mapper;
import fiftyfive.administration.usermanagement.dto.CreateUserRequest;
import fiftyfive.administration.usermanagement.dto.UpdateUserRequestData;
import fiftyfive.administration.usermanagement.dto.UserResponseData;
import fiftyfive.administration.usermanagement.entity.DeletedUser;
import fiftyfive.administration.usermanagement.entity.User;
import fiftyfive.administration.usermanagement.repository.DeletedUserRepository;
import fiftyfive.administration.usermanagement.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class UserRequestMapper {
    ModelMapper modelMapper = new ModelMapper();

    public UserResponseData createUserMapper(CreateUserRequest createUserRequest, UserRepository userRepository) {
        fiftyfive.administration.usermanagement.entity.User user = modelMapper.map(createUserRequest, fiftyfive.administration.usermanagement.entity.User.class);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(user.getCreatedAt());
        userRepository.save(user);
        return modelMapper.map(user, UserResponseData.class);
    }

    public UserResponseData updateUserMapper(UpdateUserRequestData updateUserRequestData, UserRepository userRepository, String username) {
        UserResponseData userResponseData = new UserResponseData();
        List<fiftyfive.administration.usermanagement.entity.User> users = userRepository.findAll();
        for (fiftyfive.administration.usermanagement.entity.User user : users) {
            if (user.getUsername().equals(username)) {
                Optional.ofNullable(updateUserRequestData.getFirstName()).ifPresent(user::setFirstName);
                Optional.ofNullable(updateUserRequestData.getLastName()).ifPresent(user::setLastName);
                Optional.ofNullable(updateUserRequestData.getPassword()).ifPresent(user::setPassword);
                Optional.ofNullable(updateUserRequestData.getRole()).ifPresent(user::setRole);
                user.setUpdatedAt(LocalDateTime.now());
                userResponseData = modelMapper.map(user, UserResponseData.class);
                userRepository.save(user);
            }
        }
        return userResponseData;
    }

    public UserResponseData getUserMapper(String userName, UserRepository userRepository) {
        fiftyfive.administration.usermanagement.entity.User user = userRepository.findByUsername(userName);
        return modelMapper.map(user, UserResponseData.class);
    }

    public UserResponseData getDeleteMapper(String userName, UserRepository userRepository, DeletedUserRepository deletedUserRepository) {
        User user = userRepository.findByUsername(userName);
        DeletedUser deletedUser = new DeletedUser();
        deletedUser.setUserId(user.getId());
        deletedUser.setUsername(user.getUsername());
        deletedUser.setFirstName(user.getFirstName());
        deletedUser.setLastName(user.getLastName());
        deletedUser.setRole(user.getRole());
        deletedUser.setCreatedAt(user.getCreatedAt());
        deletedUser.setUpdatedAt(user.getUpdatedAt());
        deletedUser.setPassword(user.getPassword());
        deletedUser.setDeletedAt(LocalDateTime.now());

        deletedUserRepository.save(deletedUser);
        userRepository.delete(user);
        return modelMapper.map(user, UserResponseData.class);
    }

    public <T, R> List<R> getAllUserMapper(JpaRepository<T, Long> repository, Class<R> dtoClass) {
        return repository.findAll().stream()
                .map(entity -> modelMapper.map(entity, dtoClass))
                .toList();
    }


}
