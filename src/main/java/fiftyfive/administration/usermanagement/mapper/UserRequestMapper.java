package fiftyfive.administration.usermanagement.mapper;

import fiftyfive.administration.usermanagement.dto.CreateUserRequest;
import fiftyfive.administration.usermanagement.dto.UpdateUserRequestData;
import fiftyfive.administration.usermanagement.dto.UserResponseData;
import fiftyfive.administration.usermanagement.entity.DeletedUser;
import fiftyfive.administration.usermanagement.entity.User;
import fiftyfive.administration.usermanagement.repository.DeletedUserRepository;
import fiftyfive.administration.usermanagement.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserRequestMapper {
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    UserRepository userRepository;

    @Autowired
    DeletedUserRepository deletedUserRepository;

    public UserResponseData createUserResponseMapper(CreateUserRequest createUserRequest) {
        return modelMapper.map(createUserRequest, UserResponseData.class);
    }

    public UserResponseData updateUserMapper(UpdateUserRequestData updateUserRequestData, Optional<User> user) {
        user.ifPresent(exsistingUser -> {
            Optional.ofNullable(updateUserRequestData.getFirstName()).ifPresent(exsistingUser::setFirstName);
            Optional.ofNullable(updateUserRequestData.getLastName()).ifPresent(exsistingUser::setLastName);
            Optional.ofNullable(updateUserRequestData.getPassword()).ifPresent(exsistingUser::setPassword);
            Optional.ofNullable(updateUserRequestData.getRole()).ifPresent(exsistingUser::setRole);
        });
        return modelMapper.map(user.orElse(null), UserResponseData.class);
    }

    public UserResponseData getUserMapper(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(u -> modelMapper.map(u, UserResponseData.class)).orElse(null);
    }

    public UserResponseData getDeleteMapper(DeletedUser deletedUser, Optional<User> userOptional) {
        User existingUser = userOptional.orElse(null);
        if (existingUser != null) {
            modelMapper.map(existingUser, deletedUser);
        }
        return modelMapper.map(deletedUser, UserResponseData.class);
    }

    public <T, R> List<R> getAllUserMapper(JpaRepository<T, Long> repository, Class<R> dtoClass) {
        return repository.findAll().stream().map(entity -> modelMapper.map(entity, dtoClass)).toList();
    }

    public <T> User mapToUser(T dto) {
        return modelMapper.map(dto, User.class);
    }

}
