package fiftyfive.administration.usermanagement.implementation;

import fiftyfive.administration.usermanagement.constant.Constant;
import fiftyfive.administration.usermanagement.dto.CreateUserRequest;
import fiftyfive.administration.usermanagement.dto.UpdateUserRequestData;
import fiftyfive.administration.usermanagement.dto.UserResponseData;
import fiftyfive.administration.usermanagement.entity.DeletedUser;
import fiftyfive.administration.usermanagement.entity.User;
import fiftyfive.administration.usermanagement.exception.RecordAlreadyExistsException;
import fiftyfive.administration.usermanagement.exception.UserNotExistsException;
import fiftyfive.administration.usermanagement.implemention.UserService;
import fiftyfive.administration.usermanagement.mapper.UserRequestMapper;
import fiftyfive.administration.usermanagement.repository.DeletedUserRepository;
import fiftyfive.administration.usermanagement.repository.UserRepository;
import fiftyfive.administration.usermanagement.utility.UserValidation;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.util.Assert;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class UserServiceTest {

    private UserService userService;
    private UserResponseData userResponseData= new UserResponseData();
    private CreateUserRequest createUserRequest= new CreateUserRequest();

    private DeletedUser deletedUser;
    private UpdateUserRequestData updateUserRequestData;
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    private User existingUser = new User();
    @Mock
    private UserRequestMapper userRequestMapper;
    @Mock
    private UserValidation userValidation;
    @Mock
    private UserRepository userRepository;
    @Mock
    private DeletedUserRepository deletedUserRepository;
    @BeforeEach
    void setUp() {
        ModelMapper modelMapper = new ModelMapper();

        userService= new UserService();
        existingUser.setId(1L);
        existingUser.setFirstName("Jane");
        existingUser.setUsername("janedane");
        existingUser.setLastName("Smith");
        existingUser.setRole("user");
        existingUser.setPassword("password");
        existingUser.setCreatedAt(LocalDateTime.now());
        existingUser.setUpdatedAt(existingUser.getCreatedAt());

        deletedUser= modelMapper.map(existingUser,DeletedUser.class);

        userResponseData.setId(1L);
        userResponseData.setFirstName("Jane");
        userResponseData.setUsername("janedane");
        userResponseData.setLastName("Smith");
        userResponseData.setRole("user");
        userResponseData.setCreatedAt(LocalDateTime.now());
        userResponseData.setUpdatedAt(existingUser.getCreatedAt());

        createUserRequest=modelMapper.map(existingUser,CreateUserRequest.class);

        userRequestMapper = mock(UserRequestMapper.class);
        userValidation = mock(UserValidation.class);
        userRepository = mock(UserRepository.class);
        deletedUserRepository = mock(DeletedUserRepository.class);
        userService = new UserService();
        userService.userRequestMapper = userRequestMapper;
        userService.userValidation = userValidation;
        userService.userRepository = userRepository;
        userService.deletedUserRepository = deletedUserRepository;

    }

    @Test
    void testCreateUser_Successfull() {
        User user = new User();
        UserResponseData userResponseData = new UserResponseData();
        when(userRepository.findByUsername(createUserRequest.getUsername())).thenReturn(null);
        when(userRequestMapper.mapToUser(createUserRequest)).thenReturn(user);
        when(userRequestMapper.createUserResponseMapper(user)).thenReturn(userResponseData);
        when(userRepository.save(user)).thenReturn(user);
        UserResponseData responseData = userService.createUser(createUserRequest);
        assertNotNull(responseData);
        assertSame(userResponseData, responseData);
        verify(userRepository).findByUsername(createUserRequest.getUsername());
        verify(userRequestMapper).mapToUser(createUserRequest);
        verify(userRepository).save(user);
        verify(userRequestMapper).createUserResponseMapper(user);
    }

    @Test()
    void testCreateUser_RecordAlreadyExistsException() {
        User user = new User();
        UserResponseData userResponseData = new UserResponseData();
        when(userRepository.findByUsername(createUserRequest.getUsername())).thenReturn(existingUser);
        assertThrows(RecordAlreadyExistsException.class, () -> {
            userService.createUser(createUserRequest);
        });
    }

    @Test()
    void testCreateUser_CreateUserValidation(){
        createUserRequest.setPassword(null);
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testUpdateUser_Successfull() {
        UpdateUserRequestData updateUserRequestData = new UpdateUserRequestData();
        UserResponseData userResponseData = new UserResponseData();
        when(userValidation.isUserExists(Constant.USER_NOT_EXISTS, 1L)).thenReturn(existingUser);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRequestMapper.updateUserMapper(eq(updateUserRequestData), eq(Optional.of(existingUser)))).thenReturn(userResponseData);
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        UserResponseData response = userService.updateUser(updateUserRequestData, 1L);
        Assertions.assertNotNull(response);

    }

    @Test
    void testUpdateUser_Exception() {
        UpdateUserRequestData updateUserRequestData = new UpdateUserRequestData();
        when(userValidation.isUserExists(Constant.USER_NOT_EXISTS, 1L)).thenThrow(new UserNotExistsException("User does not exist"));
        Assertions.assertThrows(UserNotExistsException.class, () -> userService.updateUser(updateUserRequestData, 1L));
    }
    public UserResponseData getUser(Long userId) throws UserNotExistsException {
        userValidation.isUserExists(Constant.USER_NOT_EXISTS, userId);
        return userRequestMapper.getUserMapper(userRepository.findById(userId));
    }

    @Test
    void getUser_Successfully(){
        when(userValidation.isUserExists(anyString(), anyLong())).thenReturn(existingUser);
        when(userValidation.isUserExists(anyString(), anyLong())).thenReturn(new User());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(userRequestMapper.getUserMapper(any(Optional.class))).thenReturn(new UserResponseData());
        UserResponseData response = userService.getUser(existingUser.getId());
        Assertions.assertNotNull(response);
    }

    @Test
    void getUser_UserNotExistsException(){
        when(userValidation.isUserExists(Constant.USER_NOT_EXISTS, 1L)).thenThrow(new UserNotExistsException(String.format(Constant.USER_NOT_EXISTS,1)));
        Assertions.assertThrows(UserNotExistsException.class, () -> userService.getUser(1L));
    }

    @Test
    void testDeleteUser_Successful() throws UserNotExistsException {
        // Mocking dependencies

        // Creating test data


        // Mocking repository behavior
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        userService.deleteUser(existingUser.getId());

        // Verifying the interactions
        verify(userRepository, times(1)).findById(existingUser.getId());
     }


    @Test
    void testDeleteUser_UserNotExistsException() {
        // Creating test data
        Long userId = 1L;

        // Mocking repository behavior
        when(userRepository.findById(userId)).thenReturn(Optional.empty());


        // Asserting that UserNotExistsException is thrown
        assertThrows(UserNotExistsException.class, () -> {
            userService.deleteUser(userId);
        });

        // Verifying the interactions
        verify(userRepository, times(1)).findById(userId);
        verify(deletedUserRepository, never()).save(any(DeletedUser.class));
        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    void testGetAllUsers_Success() {
        // Given
        List<User> users = new ArrayList<>();
        users.add(existingUser);
        List<UserResponseData>userResponseDataList= new ArrayList<>();
        userResponseDataList.add(userResponseData);
        when(userRepository.findAll()).thenReturn(users);
        when(userRequestMapper.getAllUserMapper(anyList(), any(Class.class))).thenReturn(userResponseDataList);

        // When
        List<UserResponseData> response = userService.getAllUsers();

        // Then
        Assertions.assertNotNull(response);
        // Add more assertions as per your requirements
    }

    @Test
    void testGetAllDeletedUsers_Success() {
        // Given
        List<DeletedUser> deletedUsers = new ArrayList<>();
        deletedUsers.add(new DeletedUser());
        when(deletedUserRepository.findAll()).thenReturn(deletedUsers);
        when(userRequestMapper.getAllUserMapper(anyList(), any(Class.class))).thenReturn(new ArrayList<>());

        // When
        List<UserResponseData> response = userService.getAllDeletedUsers();

        // Then
        Assertions.assertNotNull(response);

    }
}
