package com.ClearSolutions.TestTask.controler;

import com.ClearSolutions.TestTask.dto.model.UserDto;
import com.ClearSolutions.TestTask.model.User;
import com.ClearSolutions.TestTask.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<User> create(@RequestBody @Valid UserDto userRequest) {
        log.info(String.format("request to create user with values: %s", userRequest.toString()));

        User user = modelMapper.map(userRequest, User.class);

        User createdUser = userService.create(user);

        return ResponseEntity.created(URI.create(String.format("api/users/%d", createdUser.getId()))).body(createdUser);
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("user_id") long id) {
        log.info(String.format("request to get user with id: %s", id));
        return new ResponseEntity<>(
                modelMapper.map(userService.readById(id), UserDto.class), HttpStatus.OK
        );
    }

    @DeleteMapping("/{user_id}")
    public void delete(@PathVariable("user_id") long id) {
        log.debug(String.format("request to delete user with id: %s", id));
        userService.delete(id);
    }
}
