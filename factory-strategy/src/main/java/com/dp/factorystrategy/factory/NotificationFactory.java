package com.dp.factorystrategy.factory;

import com.dp.factorystrategy.strategy.EmailNotification;
import com.dp.factorystrategy.strategy.NotificationStrategy;
import com.dp.factorystrategy.strategy.SmsNotification;
import org.springframework.stereotype.Component;

@Component
public class NotificationFactory {
    private final EmailNotification email;
    private final SmsNotification sms;

    public NotificationFactory(EmailNotification email, SmsNotification sms) {
        this.email = email;
        this.sms = sms;
    }

    public NotificationStrategy getStrategy(String type) {
        return switch (type.toLowerCase()) {
            case "email" -> email;
            case "sms" -> sms;
            default -> throw new IllegalArgumentException("Unsupported type: " + type);
        };
    }
}
