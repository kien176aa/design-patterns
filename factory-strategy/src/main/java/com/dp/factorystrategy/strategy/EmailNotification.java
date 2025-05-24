package com.dp.factorystrategy.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailNotification implements NotificationStrategy {
    public void send(String to, String message) {
        log.info("📧 Sending EMAIL to {}: {}", to, message);
    }
}
