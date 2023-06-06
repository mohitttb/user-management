package fiftyfive.administration.usermanagement.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.techprimers.grpc.GreetingResponse;
import fiftyfive.administration.usermanagement.dto.CreateUserRequest;
import fiftyfive.administration.usermanagement.dto.UpdateUserRequestData;
import fiftyfive.administration.usermanagement.dto.UserResponseData;
import fiftyfive.administration.usermanagement.implemention.GreetingServiceImpl;
import fiftyfive.administration.usermanagement.implemention.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/users")
@Slf4j
public class UserManagementController {

    @Autowired
    private UserService userService;

    @Autowired
    private GreetingServiceImpl greetingService;

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

    @GetMapping
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

    @PostMapping("/test")
    public  ResponseEntity<Map<String,String>> testingGprc(@RequestBody Map<String, Object> requestBody){
        return ResponseEntity.status(HttpStatus.OK).body(greetingService.callGRPCApi((String) requestBody.get("message")));
    }
}
