package fiftyfive.administration.usermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fiftyfive.administration.usermanagement.dto.CreateUserRequest;
import fiftyfive.administration.usermanagement.dto.UpdateUserRequestData;
import fiftyfive.administration.usermanagement.dto.UserResponseData;
import fiftyfive.administration.usermanagement.entity.User;
import fiftyfive.administration.usermanagement.exception.RecordAlreadyExistsException;
import fiftyfive.administration.usermanagement.exception.UserControllerAdvice;
import fiftyfive.administration.usermanagement.exception.UserNotExistsException;
import fiftyfive.administration.usermanagement.implemention.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@ExtendWith(SpringExtension.class)
public class UserManagementControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();
    @InjectMocks
    private UserManagementController userManagementController;
    @Mock
    private UserService userService;
    private MockMvc mockMvc;
    private User existingUser = new User();
    private CreateUserRequest createUserRequest = new CreateUserRequest();

    private UserResponseData userResponseData = new UserResponseData();

    private UpdateUserRequestData updateUserRequestData = new UpdateUserRequestData();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        ModelMapper modelMapper = new ModelMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(userManagementController)
                .setControllerAdvice(new UserControllerAdvice())
                .build();

        existingUser.setId(1L);
        existingUser.setFirstName("Jane");
        existingUser.setUsername("janedane");
        existingUser.setLastName("Smith");
        existingUser.setRole("user");
        existingUser.setPassword("password");
        existingUser.setCreatedAt(LocalDateTime.now());
        existingUser.setUpdatedAt(existingUser.getCreatedAt());
        createUserRequest = modelMapper.map(existingUser, CreateUserRequest.class);
        updateUserRequestData = modelMapper.map(existingUser, UpdateUserRequestData.class);
        userResponseData = modelMapper.map(existingUser, UserResponseData.class);

    }


    @Test
    public void testCreateUser_Null_Password_ArgumentNotValid_InvalidRequest_ReturnsBadRequest() throws Exception {
        createUserRequest.setPassword(null);
        String requestJson = objectMapper.writeValueAsString(createUserRequest);
        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Password is required"));
    }

    @Test
    public void testCreateUser_Null_Role_ArgumentNotValid_InvalidRequest_ReturnsBadRequest() throws Exception {
        createUserRequest.setRole(null);
        String requestJson = objectMapper.writeValueAsString(createUserRequest);
        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Role is required"));
    }

    @Test
    public void testCreateUser_Null_Username_ArgumentNotValid_InvalidRequest_ReturnsBadRequest() throws Exception {
        createUserRequest.setUsername(null);
        String requestJson = objectMapper.writeValueAsString(createUserRequest);
        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Username is required"));
    }

    @Test
    public void testCreateUser_Null_First_Name_ArgumentNotValid_InvalidRequest_ReturnsBadRequest() throws Exception {
        createUserRequest.setFirstName(null);
        String requestJson = objectMapper.writeValueAsString(createUserRequest);
        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("First Name is required"));
    }

    @Test
    public void testCreateUser_Null_Last_Name_ArgumentNotValid_InvalidRequest_ReturnsBadRequest() throws Exception {
        createUserRequest.setFirstName(null);
        String requestJson = objectMapper.writeValueAsString(createUserRequest);
        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("First Name is required"));
    }

    @Test
    public void testCreateUser_RecordAlreadyExistsException_ReturnConflict() throws Exception {
      Mockito.doThrow(new RecordAlreadyExistsException("User already exists"))
                .when(userService)
                .createUser(Mockito.any(CreateUserRequest.class));

        String requestJson = objectMapper.writeValueAsString(createUserRequest);

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("User already exists"));
    }

    @Test
    public void testCreateUser_Successfully_ReturnsCreated() throws Exception {
        String requestJson = objectMapper.writeValueAsString(createUserRequest);
        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdateUser_UserNotExistsException_ReturnNotFound() throws Exception {
       Mockito.doThrow(new UserNotExistsException("User not exists"))
                .when(userService)
                .updateUser(Mockito.any(UpdateUserRequestData.class), any());
        String requestJson = objectMapper.writeValueAsString(updateUserRequestData);
        mockMvc.perform(put("/v1/users/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not exists"));
    }

    @Test
    public void testUpdateUser_Successfully_ReturnsOk() throws Exception {
        String requestJson = objectMapper.writeValueAsString(updateUserRequestData);
        mockMvc.perform(put("/v1/users/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteUser_Successfully_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/v1/users/{userId}", 1L)).andExpect(status().isNoContent());

    }

    @Test
    public void testGetAllUser_Successfully_ReturnsOk() throws Exception {
        List<UserResponseData> userList = new ArrayList<>();
        userList.add(userResponseData);
        when(userService.getAllUsers()).thenReturn(userList);
        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllUser_EmptyList_ReturnsNoContent() throws Exception {
        List<UserResponseData> userList = Collections.emptyList();
        when(userService.getAllUsers()).thenReturn(userList);

        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isNoContent());
    }


    @Test
    public void testGetAllDeletedUser_Successfully_ReturnsOk() throws Exception {
        List<UserResponseData> userList = new ArrayList<>();
        userList.add(userResponseData);
        when(userService.getAllDeletedUsers()).thenReturn(userList);
        mockMvc.perform(get("/v1/users/deleted"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllDeletedUser_EmptyList_ReturnsNoContent() throws Exception {
        List<UserResponseData> userList = Collections.emptyList();
        when(userService.getAllDeletedUsers()).thenReturn(userList);
        mockMvc.perform(get("/v1/users/deleted"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteUser_UserNotExistsException_ReturnNotFound() throws Exception {
        Mockito.doThrow(new UserNotExistsException("User not exists"))
                .when(userService)
                .updateUser(Mockito.any(UpdateUserRequestData.class), any());
        String requestJson = objectMapper.writeValueAsString(updateUserRequestData);
        mockMvc.perform(put("/v1/users/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetUser_UserNotExistsException_ReturnNotFound() throws Exception {
        Mockito.doThrow(new UserNotExistsException("User not exists"))
                .when(userService)
                .getUser(any());
        mockMvc.perform(get("/v1/users/{userId}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetUser_ExistingUser_ReturnsOkWithUser() throws Exception {
        when(userService.getUser(any())).thenReturn(userResponseData);
        mockMvc.perform(get("/v1/users/{userId}", 1L))
                .andExpect(status().isOk());
    }


}
