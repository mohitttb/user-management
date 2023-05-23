package fiftyfive.administration.usermanagement.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fiftyfive.administration.usermanagement.constant.Constant;
import fiftyfive.administration.usermanagement.dto.CreateUserRequest;
import fiftyfive.administration.usermanagement.dto.UpdateUserRequestData;
import fiftyfive.administration.usermanagement.dto.UserResponseData;
import fiftyfive.administration.usermanagement.entity.DeletedUser;
import fiftyfive.administration.usermanagement.entity.User;
import fiftyfive.administration.usermanagement.exception.RecordAlreadyExistsException;
import fiftyfive.administration.usermanagement.exception.UserControllerAdvice;
import fiftyfive.administration.usermanagement.implemention.UserService;
import fiftyfive.administration.usermanagement.mapper.UserRequestMapper;
import fiftyfive.administration.usermanagement.repository.DeletedUserRepository;
import fiftyfive.administration.usermanagement.repository.UserRepository;
import fiftyfive.administration.usermanagement.utility.UserValidation;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDateTime;
import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {UserManagementController.class})
@ExtendWith(SpringExtension.class)
public class UserManagementControllerTest {

    private UserResponseData userResponseData= new UserResponseData();
    private CreateUserRequest createUserRequest= new CreateUserRequest();




    private User existingUser = new User();

    @Autowired
    private UserManagementController userManagementController;

    @MockBean
    private UserService userService;

    @MockBean
    UserControllerAdvice userControllerAdvice;


    @Mock
    private UserRepository userRepository;




    private MockMvc mockMvc ;
    ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        ModelMapper modelMapper = new ModelMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(userManagementController)
                .setControllerAdvice(userControllerAdvice)
                .build();

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

        createUserRequest=modelMapper.map(existingUser, CreateUserRequest.class);


//        userService.userRequestMapper = userRequestMapper;
//        userService.userValidation = userValidation;
//        userService.userRepository = userRepository;
//        userService.deletedUserRepository = deletedUserRepository;

    }

    @Test
    public void testHandleMethodArgumentNotValid_InvalidRequest_ReturnsBadRequest() throws Exception {
        createUserRequest.setPassword(null);
        String requestJson = objectMapper.writeValueAsString(createUserRequest);
        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testCreateUser_Successful_Return_Created() throws Exception {
        objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectMapper.writeValueAsString(createUserRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated());
    }

    @Test
    public void testHandleRecordAlreadyExistsException_Return_Conflict() throws Exception {
        String requestJson = objectMapper.writeValueAsString(createUserRequest);
        createUserRequest.setUsername(null);
//        doThrow(new RecordAlreadyExistsException(Constant.USER_ALREADY_EXISTS)).when(userService).createUser(createUserRequest);
        when(userService.createUser(createUserRequest)).thenReturn(userResponseData);
//        when(userRepository.findByUsername(createUserRequest.getUsername())).thenReturn(existingUser);

//        doAnswer(invocation -> {
//            throw new RecordAlreadyExistsException(String.format(Constant.USER_ALREADY_EXISTS, 1L));
//        }).when(userService).createUser(Mockito.any(CreateUserRequest.class));

//        doThrow(new RecordAlreadyExistsException(String.format(Constant.USER_ALREADY_EXISTS, 1L)))
//                .when(userService).createUser(createUserRequest);
//        Mockito.when(userService.createUser(createUserRequest))
//                .thenThrow(new RecordAlreadyExistsException(String.format(Constant.USER_ALREADY_EXISTS, 1L)));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/users").contentType(MediaType.APPLICATION_JSON).content(requestJson);
        mockMvc.perform(requestBuilder).andExpect(status().isConflict());
//        mockMvc.perform(requestBuilder)
//                .andExpect(status().isConflict());
    }


    @Test
    public void testCreateUser_RecordNotFoundException() throws Exception {
        String requestJson = objectMapper.writeValueAsString(createUserRequest);
                when(userRepository.findByUsername(createUserRequest.getUsername())).thenReturn(existingUser);
//        doThrow(new RecordAlreadyExistsException(Constant.USER_ALREADY_EXISTS)).when(userService).createUser(Mockito.any(CreateUserRequest.class));
        given(userService.createUser(createUserRequest)).willThrow(RecordAlreadyExistsException.class);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isConflict());
    }
//    @Test
//    public void testUpdateUser_RecordNotFoundException() throws Exception {
//        Long userId = 1L;
//        String requestJson = objectMapper.writeValueAsString(updateUserRequestData);
//        doThrow(new RecordAlreadyExistsException("User not found")).when(userService).updateUser(Mockito.any(UpdateUserRequestData.class), Mockito.eq(userId));
//
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/v1/users/{userId}", userId)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(requestJson);
//
//        mockMvc.perform(requestBuilder)
//                .andExpect(status().isNotFound());
//    }
    @Test
    public void testUpdateUser_RecordNotFoundException2() throws Exception {
        String requestJson = objectMapper.writeValueAsString(createUserRequest);
//        when(userRepository.findByUsername(createUserRequest.getUsername())).thenReturn(existingUser);


        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)).andDo((ResultHandler) doThrow(new RecordAlreadyExistsException(Constant.USER_NOT_EXISTS)).when(userService.createUser(createUserRequest))).andReturn();
    }


}
