package fiftyfive.administration.usermanagement.mapper;

import fiftyfive.administration.usermanagement.dto.UpdateUserRequestData;
import fiftyfive.administration.usermanagement.dto.UserResponseData;
import fiftyfive.administration.usermanagement.entity.DeletedUser;
import fiftyfive.administration.usermanagement.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
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
        ModelMapper modelMapper = new ModelMapper();
        userRequestMapper = new UserRequestMapper();
        existingUser.setId(1L);
        existingUser.setFirstName("Jane");
        existingUser.setUsername("janedane");
        existingUser.setLastName("Smith");
        existingUser.setRole("user");
        existingUser.setPassword("password");
        existingUser.setCreatedAt(LocalDateTime.now());
        existingUser.setUpdatedAt(existingUser.getCreatedAt());
        userResponseData = modelMapper.map(existingUser, UserResponseData.class);
    }

    @Test
    void testCreateUserResponseMapper() {
        // Given
        UserResponseData responseData = userRequestMapper.createUserResponseMapper(existingUser);
        assertNotNull(responseData);
        assertEquals(1L, responseData.getId());
        assertEquals("Jane", responseData.getFirstName());
        assertEquals("Smith", responseData.getLastName());
    }

    @Test
    void testUpdateUserMapper_WhenUserExists() {
        UpdateUserRequestData updateUserRequestData = new UpdateUserRequestData();
        updateUserRequestData.setFirstName("Jane");
        updateUserRequestData.setLastName("Smith");
        updateUserRequestData.setPassword("password");
        updateUserRequestData.setRole("user");
        Optional<User> user = Optional.of(existingUser);
        UserResponseData responseData = userRequestMapper.updateUserMapper(updateUserRequestData, user);
        assertNotNull(responseData);
        assertEquals(1L, responseData.getId());
        assertEquals("Jane", responseData.getFirstName());
        assertEquals("Smith", responseData.getLastName());
        assertEquals("password", existingUser.getPassword());
        assertEquals("user", existingUser.getRole());
        assertNotNull(existingUser.getUpdatedAt());
        assertTrue(existingUser.getUpdatedAt().isBefore(LocalDateTime.now()));
    }

    @Test
    void testUpdateUserMapper_WhenUserNotExists() {
        UpdateUserRequestData updateUserRequestData = new UpdateUserRequestData();
        updateUserRequestData.setFirstName("Jane");
        updateUserRequestData.setLastName("Smith");
        Optional<User> user = Optional.empty();
        UserResponseData responseData = userRequestMapper.updateUserMapper(updateUserRequestData, user);
        assertNull(responseData);
    }

    @Test
    void testGetUserMapper_WhenUserExists() {
        Optional<User> user = Optional.of(existingUser);
        UserResponseData responseData = userRequestMapper.getUserMapper(user);
        assertNotNull(responseData);
        assertEquals(existingUser.getId(), responseData.getId());
        assertEquals(existingUser.getFirstName(), responseData.getFirstName());
        assertEquals(existingUser.getLastName(), responseData.getLastName());
    }

    @Test
    void testGetUserMapper_WhenUserNotExists() {
        Optional<User> user = Optional.empty();
        UserResponseData responseData = userRequestMapper.getUserMapper(user);
        assertNull(responseData);
    }

    @Test
    void testGetDeleteMapper_WhenUserExists() {
        UserResponseData responseData = userRequestMapper.getDeleteMapper(new DeletedUser(), Optional.of(existingUser));
        assertNotNull(responseData);
        assertEquals(existingUser.getId(), responseData.getId());
        assertEquals(existingUser.getFirstName(), responseData.getFirstName());
        assertEquals(existingUser.getLastName(), responseData.getLastName());
    }

    @Test
    void testGetDeleteMapper_WhenUserNotExists() {
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
    void testGetAllUserMapper_WhenUserExists() {
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
    void testGetAllUserMapper_WhenUserNotExists() {
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
