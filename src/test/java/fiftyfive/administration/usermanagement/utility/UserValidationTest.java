package fiftyfive.administration.usermanagement.utility;

import fiftyfive.administration.usermanagement.constant.Constant;
import fiftyfive.administration.usermanagement.entity.User;
import fiftyfive.administration.usermanagement.exception.UserNotExistsException;
import fiftyfive.administration.usermanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserValidationTest {

    @Mock
    private UserRepository userRepository;

    private UserValidation userValidation;

    @BeforeEach
    void setUp() {
        userValidation = new UserValidation();
        userValidation.userRepository = userRepository;
    }

    @Test
    void testIsUserExists_UserExists() throws UserNotExistsException {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        List<User> users = new ArrayList<>();
        users.add(user);
        when(userRepository.findAll()).thenReturn(users);
        User result = userValidation.isUserExists(Constant.USER_NOT_EXISTS, userId);
        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testIsUserExists_UserNotExists() {
        // Given
        Long userId = 2L;
        List<User> users = new ArrayList<>();

        when(userRepository.findAll()).thenReturn(users);

        // When
        UserNotExistsException exception = assertThrows(UserNotExistsException.class, () ->
                userValidation.isUserExists(Constant.USER_NOT_EXISTS, userId));

        // Then
        assertEquals(String.format(Constant.USER_NOT_EXISTS, userId), exception.getMessage());

        verify(userRepository, times(1)).findAll();
    }
}
