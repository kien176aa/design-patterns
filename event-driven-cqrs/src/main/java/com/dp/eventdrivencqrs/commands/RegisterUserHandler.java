package com.dp.eventdrivencqrs.commands;

import com.dp.eventdrivencqrs.entity.User;
import com.dp.eventdrivencqrs.events.UserRegisteredEvent;
import com.dp.eventdrivencqrs.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserHandler {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public RegisterUserHandler(UserRepository userRepository, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    public void handle(RegisterUserCommand command) {
        User user = new User();
        user.setEmail(command.email());
        user.setName(command.name());
        userRepository.save(user);

        // publish event
        eventPublisher.publishEvent(new UserRegisteredEvent(this, user));
    }
}
