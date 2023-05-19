package fiftyfive.administration.usermanagement.controller;

import fiftyfive.administration.usermanagement.dto.*;
import fiftyfive.administration.usermanagement.implemention.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserManagementController {

    @Autowired
    UserService userService;

    @PostMapping()
    public ResponseEntity<UserResponseData> createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(createUserRequest));
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserResponseData> updateUser(@RequestBody UpdateUserRequestData userRequestData, @PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userRequestData, username));

    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseData> getUser(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(username));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<UserResponseData> deleteUser(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userService.deleteUser(username));
    }

    @GetMapping()
    public ResponseEntity<List<UserResponseData>> getAllUser() {
        List<UserResponseData> user = userService.getAllUsers();
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(user);
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<UserResponseData>> getAllDeletedUser() {
        List<UserResponseData> user = userService.getAllDeletedUsers();
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(user);
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
