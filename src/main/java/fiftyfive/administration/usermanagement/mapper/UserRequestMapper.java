package fiftyfive.administration.usermanagement.mapper;

import fiftyfive.administration.usermanagement.dto.UpdateUserRequestData;
import fiftyfive.administration.usermanagement.dto.UserResponseData;
import fiftyfive.administration.usermanagement.entity.DeletedUser;
import fiftyfive.administration.usermanagement.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class UserRequestMapper {
    ModelMapper modelMapper = new ModelMapper();

    public UserResponseData createUserResponseMapper(User user) {
        return modelMapper.map(user, UserResponseData.class);
    }

    public UserResponseData updateUserMapper(UpdateUserRequestData updateUserRequestData, Optional<User> user) {
        if (user.isEmpty()) {
            return null;
        }
        User existingUser = user.get();
        Optional.ofNullable(updateUserRequestData.getFirstName()).ifPresent(existingUser::setFirstName);
        Optional.ofNullable(updateUserRequestData.getLastName()).ifPresent(existingUser::setLastName);
        Optional.ofNullable(updateUserRequestData.getPassword()).ifPresent(existingUser::setPassword);
        Optional.ofNullable(updateUserRequestData.getRole()).ifPresent(existingUser::setRole);
        existingUser.setUpdatedAt(LocalDateTime.now());
        return modelMapper.map(existingUser, UserResponseData.class);
    }

    public UserResponseData getUserMapper(Optional<User> user) {
        return user.map(u -> modelMapper.map(u, UserResponseData.class)).orElse(null);
    }

    public UserResponseData getDeleteMapper(DeletedUser deletedUser, Optional<User> userOptional) {
        userOptional.ifPresent(existingUser -> modelMapper.map(existingUser, deletedUser));
        return modelMapper.map(deletedUser, UserResponseData.class);
    }

    public <T, R> List<R> getAllUserMapper(List<T> entities, Class<R> dtoClass) {
        return entities.stream().map(entity -> modelMapper.map(entity, dtoClass)).toList();
    }

    public <T> User mapToUser(T dto) {
        return modelMapper.map(dto, User.class);
    }
}
