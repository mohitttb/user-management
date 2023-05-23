package fiftyfive.administration.usermanagement.controller;

import fiftyfive.administration.usermanagement.dto.CreateUserRequest;
import fiftyfive.administration.usermanagement.dto.UpdateUserRequestData;
import fiftyfive.administration.usermanagement.dto.UserResponseData;
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

    @PostMapping
    public ResponseEntity<UserResponseData> createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(createUserRequest));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseData> updateUser(@RequestBody UpdateUserRequestData userRequestData, @PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userRequestData, userId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseData> getUser(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<List<UserResponseData>> getAllUser() {
        List<UserResponseData> user = userService.getAllUsers();
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<UserResponseData>> getAllDeletedUser() {
        List<UserResponseData> user = userService.getAllDeletedUsers();
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
