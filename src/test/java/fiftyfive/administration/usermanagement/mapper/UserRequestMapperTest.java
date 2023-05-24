package fiftyfive.administration.usermanagement.mapper;

import fiftyfive.administration.usermanagement.dto.UpdateUserRequestData;
import fiftyfive.administration.usermanagement.dto.UserResponseData;
import fiftyfive.administration.usermanagement.entity.DeletedUser;
import fiftyfive.administration.usermanagement.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserRequestMapperTest {
    private UserRequestMapper userRequestMapper;
    private UserResponseData userResponseData = new UserResponseData();
    private User existingUser = new User();

    @BeforeEach
    void setUp() {
        userRequestMapper = new UserRequestMapper();
        existingUser.setId(1L);
        existingUser.setFirstName("Jane");
        existingUser.setUsername("janedane");
        existingUser.setLastName("Smith");
        existingUser.setRole("user");
        existingUser.setPassword("password");
        existingUser.setCreatedAt(LocalDateTime.now());
        existingUser.setUpdatedAt(existingUser.getCreatedAt());

        userResponseData.setId(1L);
        userResponseData.setFirstName("Jane");
        userResponseData.setUsername("janedane");
        userResponseData.setLastName("Smith");
        userResponseData.setRole("user");
        userResponseData.setCreatedAt(LocalDateTime.now());
        userResponseData.setUpdatedAt(existingUser.getCreatedAt());
    }

    @Test
    void testCreateUserResponseMapper() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        UserResponseData responseData = userRequestMapper.createUserResponseMapper(user);
        assertNotNull(responseData);
        assertEquals(1L, responseData.getId());
        assertEquals("John", responseData.getFirstName());
        assertEquals("Doe", responseData.getLastName());
    }

    @Test
    void testUpdateUserMapper_UserExists() {
        UpdateUserRequestData updateUserRequestData = new UpdateUserRequestData();
        updateUserRequestData.setFirstName("John");
        updateUserRequestData.setLastName("Doe");
        updateUserRequestData.setPassword("password");
        updateUserRequestData.setRole("user");
        Optional<User> user = Optional.of(existingUser);
        UserResponseData responseData = userRequestMapper.updateUserMapper(updateUserRequestData, user);
        assertNotNull(responseData);
        assertEquals(1L, responseData.getId());
        assertEquals("John", responseData.getFirstName());
        assertEquals("Doe", responseData.getLastName());
        assertEquals("password", existingUser.getPassword());
        assertEquals("user", existingUser.getRole());
        assertNotNull(existingUser.getUpdatedAt());
        assertTrue(existingUser.getUpdatedAt().isBefore(LocalDateTime.now()));
    }

    @Test
    void testUpdateUserMapper_UserNotExists() {
        UpdateUserRequestData updateUserRequestData = new UpdateUserRequestData();
        updateUserRequestData.setFirstName("John");
        updateUserRequestData.setLastName("Doe");
        Optional<User> user = Optional.empty();
        UserResponseData responseData = userRequestMapper.updateUserMapper(updateUserRequestData, user);
        assertNull(responseData);
    }

    @Test
    void testGetUserMapper_UserExists() {
        Optional<User> user = Optional.of(existingUser);
        UserResponseData responseData = userRequestMapper.getUserMapper(user);
        assertNotNull(responseData);
        assertEquals(existingUser.getId(), responseData.getId());
        assertEquals(existingUser.getFirstName(), responseData.getFirstName());
        assertEquals(existingUser.getLastName(), responseData.getLastName());
    }

    @Test
    void testGetUserMapper_UserNotExists() {
        Optional<User> user = Optional.empty();
        UserResponseData responseData = userRequestMapper.getUserMapper(user);
        assertNull(responseData);
    }

    @Test
    void testGetDeleteMapper_UserExists() {
        UserResponseData responseData = userRequestMapper.getDeleteMapper(new DeletedUser(), Optional.of(existingUser));
        assertNotNull(responseData);
        assertEquals(existingUser.getId(), responseData.getId());
        assertEquals(existingUser.getFirstName(), responseData.getFirstName());
        assertEquals(existingUser.getLastName(), responseData.getLastName());
    }

    @Test
    void testGetDeleteMapper_UserNotExists() {
        UserResponseData responseData = userRequestMapper.getDeleteMapper(new DeletedUser(), Optional.of(new User()));
        assertNull(responseData.getId());
        assertNull(responseData.getUsername());
        assertNull(responseData.getFirstName());
        assertNull(responseData.getLastName());
        assertNull(responseData.getRole());
        assertNull(responseData.getCreatedAt());
        assertNull(responseData.getUpdatedAt());
        assertNull(responseData.getDeletedAt());
    }

    @Test
    void testGetAllUserMapper_UserExists() {
        List<User> users = new ArrayList<>();
        users.add(existingUser);
        List<UserResponseData> responseDataList = userRequestMapper.getAllUserMapper(users, UserResponseData.class);
        for (UserResponseData responseData : responseDataList) {
            assertNotNull(responseData.getId());
            assertNotNull(responseData.getUsername());
            assertNotNull(responseData.getFirstName());
            assertNotNull(responseData.getLastName());
            assertNotNull(responseData.getRole());
            assertNotNull(responseData.getCreatedAt());
            assertNotNull(responseData.getUpdatedAt());
        }
    }

    @Test
    void testGetAllUserMapper_NullUser() {
        List<User> users = new ArrayList<>();
        List<UserResponseData> responseDataList = userRequestMapper.getAllUserMapper(users, UserResponseData.class);
        assertEquals(0, responseDataList.size());
    }

    @Test
    void testMapToUser() {
        User user = userRequestMapper.mapToUser(userResponseData);
        assertNotNull(user);
    }
}
