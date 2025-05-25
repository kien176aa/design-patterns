package com.dp.factorystrategy.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailNotification implements NotificationStrategy {
    public void send(String to, String message) {
        String abc = "commit 1";
        if(abc.equals("commit 2")) {
            log.info("This is a  22 test commit");
        }
        log.info("📧 Sending EMAIL to {}: {}", to, message);
    }
}
