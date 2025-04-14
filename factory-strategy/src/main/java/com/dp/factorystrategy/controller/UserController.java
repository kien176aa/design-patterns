package com.dp.factorystrategy.controller;

import com.dp.factorystrategy.dto.NotificationRequest;
import com.dp.factorystrategy.entity.User;
import com.dp.factorystrategy.repository.UserRepository;
import com.dp.factorystrategy.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PostMapping("/{id}/notify")
    public ResponseEntity<String> notifyUser(
            @PathVariable Long id,
            @RequestBody NotificationRequest req) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String contact = req.getType().equalsIgnoreCase("email") ? user.getEmail() : user.getPhoneNumber();
        notificationService.notify(req.getType(), contact, req.getMessage());
        return ResponseEntity.ok("Notification sent!");
    }
}
