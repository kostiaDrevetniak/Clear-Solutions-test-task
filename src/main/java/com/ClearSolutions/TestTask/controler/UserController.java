package com.ClearSolutions.TestTask.controler;

import com.ClearSolutions.TestTask.dto.request.UserRequest;
import com.ClearSolutions.TestTask.dto.response.UserResponse;
import com.ClearSolutions.TestTask.model.User;
import com.ClearSolutions.TestTask.service.Patcher;
import com.ClearSolutions.TestTask.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    @Autowired
    private Patcher<User> patcher;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest) {
        log.info(String.format("request to create user with values: %s", userRequest.toString()));

        User user = modelMapper.map(userRequest, User.class);

        User createdUser = userService.create(user);

        return ResponseEntity.created(URI.create(String.format("api/users/%d", createdUser.getId())))
                .body(modelMapper.map(createdUser, UserResponse.class));
    }

    @PutMapping("/{user_id}")
    public ResponseEntity<UserResponse> updateUserBuId(@PathVariable("user_id") long id,
                                                       @RequestBody @Valid UserRequest userRequest) {
        log.debug(String.format("request to update user id: %d with values: %s", id, userRequest.toString()));
        User user = modelMapper.map(userRequest, User.class);
        user.setId(id);
        userService.update(user);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{user_id}")
    public ResponseEntity<UserResponse> updateUserFieldsBuId(@PathVariable("user_id") long id,
                                                             @RequestBody UserRequest userRequest) {
        log.debug(String.format("request to update fields in user id: %d with values: %s", id, userRequest.toString()));
        User user = modelMapper.map(userRequest, User.class);
        User existedUser = userService.readById(id);
        patcher.patch(existedUser, user);
        userService.update(existedUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("user_id") long id) {
        log.info(String.format("request to get user with id: %s", id));
        return new ResponseEntity<>(
                modelMapper.map(userService.readById(id), UserResponse.class), HttpStatus.OK
        );
    }

    @DeleteMapping("/{user_id}")
    public void deleteUserById(@PathVariable("user_id") long id) {
        log.debug(String.format("request to delete user with id: %s", id));
        userService.delete(id);
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        log.debug("request to get all users");
        return userService.getAll().stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .toList();
    }

    @GetMapping("/birthDate")
    public List<UserResponse> getAllUsersByBirthDateRange(@RequestParam("from") LocalDate from,
                                                          @RequestParam("to") LocalDate to) {
        log.info(String.format("request to get all users with births date from %s to %s", from, to));
        return userService.getAllByDateRange(from, to).stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .toList();
    }
}
