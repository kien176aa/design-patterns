package com.dp.eventdrivencqrs.controllers;

import com.dp.eventdrivencqrs.commands.RegisterUserCommand;
import com.dp.eventdrivencqrs.commands.RegisterUserHandler;
import com.dp.eventdrivencqrs.entity.User;
import com.dp.eventdrivencqrs.queries.UserQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final RegisterUserHandler registerUserHandler;
    private final UserQueryService userQueryService;

    public UserController(RegisterUserHandler registerUserHandler, UserQueryService userQueryService) {
        this.registerUserHandler = registerUserHandler;
        this.userQueryService = userQueryService;
    }

    // Create a new user
    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody RegisterUserCommand command) {
        registerUserHandler.handle(command);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userQueryService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userQueryService.getByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}