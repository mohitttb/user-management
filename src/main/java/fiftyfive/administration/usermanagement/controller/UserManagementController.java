package fiftyfive.administration.usermanagement.controller;

import fiftyfive.administration.usermanagement.dto.CreateUserRequest;
import fiftyfive.administration.usermanagement.dto.UpdateUserRequestData;
import fiftyfive.administration.usermanagement.dto.User;
import fiftyfive.administration.usermanagement.dto.UserResponseData;
import fiftyfive.administration.usermanagement.exception.UserManagementException;
import fiftyfive.administration.usermanagement.implemention.UserService;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/")
public class UserManagementController {
    private List<User> userList = new ArrayList<>();

    @Autowired
    UserService userService;

       @PostMapping("create-user")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest createUserRequest) {
        try {
            UserResponseData createdUser = userService.createUser(createUserRequest,userList);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (UserManagementException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessage("User already exists"));
        }
    }
    @PutMapping("update-user/{username}")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequestData userRequestData,@PathVariable String username) {
        try {
            UserResponseData createdUser = userService.updateUser(userRequestData,userList,username);
            return ResponseEntity.status(HttpStatus.OK).body(createdUser);
        } catch (UserManagementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("User does not exists."));
        }
    }

    @GetMapping("get-user/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        try {
            UserResponseData user = userService.getUser(username,userList);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (UserManagementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("User does not exists."));
        }

    }

    @DeleteMapping ("delete-user/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        try {
            UserResponseData user = userService.deleteUser(username,userList);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(user);
        } catch (UserManagementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("User does not exists."));
        }

    }

    @GetMapping("get-all-user")
    public ResponseEntity<?> getUser() {
            List<UserResponseData> user = userService.getAllUsers(userList);
            if(user.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("No user exists"));
            }
            return ResponseEntity.status(HttpStatus.OK).body(user);
    }


}
