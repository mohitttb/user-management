package fiftyfive.administration.usermanagement.controller;

import fiftyfive.administration.usermanagement.dto.*;
import fiftyfive.administration.usermanagement.exception.UserManagementException;
import fiftyfive.administration.usermanagement.implemention.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserManagementController {
     List<User> userList = new ArrayList<>();

    @Autowired
    UserService userService;

       @PostMapping()
    public ResponseEntity<UserResponseData> createUser(@RequestBody CreateUserRequest createUserRequest) {
           UserResponseData createdUser=new UserResponseData();
           try {
               createdUser = userService.createUser(createUserRequest,userList);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (UserManagementException e) {
               createdUser.setErrorMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(createdUser);
        }
    }
    @PutMapping("/{username}")
    public ResponseEntity<UserResponseData> updateUser(@RequestBody UpdateUserRequestData userRequestData,@PathVariable String username) {
        UserResponseData userResponseData=new UserResponseData();
           try {
               userResponseData = userService.updateUser(userRequestData,userList,username);
            return ResponseEntity.status(HttpStatus.OK).body(userResponseData);
        } catch (UserManagementException e) {
               userResponseData.setErrorMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userResponseData);
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseData> getUser(@PathVariable String username) {
        UserResponseData user = new UserResponseData();
        try {
             user = userService.getUser(username,userList);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (UserManagementException e) {
            user.setErrorMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        }

    }

    @DeleteMapping ("/{username}")
    public ResponseEntity<UserResponseData> deleteUser(@PathVariable String username) {
        UserResponseData user=new UserResponseData();
        try {
             user = userService.deleteUser(username,userList);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(user);
        } catch (UserManagementException e) {
            user.setErrorMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        }

    }

    @GetMapping()
    public ResponseEntity<List<UserResponseData>> getAllUser() {
            List<UserResponseData> user = userService.getAllUsers(userList);
            if(user.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(user);
            }
            return ResponseEntity.status(HttpStatus.OK).body(user);
    }


}
