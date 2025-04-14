package com.dp.factorystrategy.service;

import com.dp.factorystrategy.factory.NotificationFactory;
import com.dp.factorystrategy.strategy.NotificationStrategy;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final NotificationFactory factory;

    public NotificationService(NotificationFactory factory) {
        this.factory = factory;
    }

    public void notify(String type, String to, String message) {
        NotificationStrategy strategy = factory.getStrategy(type);
        strategy.send(to, message);
    }
}
