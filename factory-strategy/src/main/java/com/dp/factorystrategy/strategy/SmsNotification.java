package com.dp.factorystrategy.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SmsNotification implements NotificationStrategy {
    public void send(String to, String message) {
        log.info("📱 Sending SMS to {}: {}", to, message);
    }
}
