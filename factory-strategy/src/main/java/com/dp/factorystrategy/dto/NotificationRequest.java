package com.dp.factorystrategy.dto;

import lombok.Data;

@Data
public class NotificationRequest {
    private String type; // email or sms
    private String message;
}
