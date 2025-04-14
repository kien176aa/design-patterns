package com.dp.eventdrivencqrs.events;

import com.dp.eventdrivencqrs.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserRegisteredEventListener {

    @EventListener
    public void handle(UserRegisteredEvent event) {
        User user = event.getUser();
        log.info("👋 Welcome email sent to: {}", user.getEmail());
        // Hoặc gửi email thực sự hoặc ghi log vào bảng khác
    }
}
