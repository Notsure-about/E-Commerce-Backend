package com.example.ecommerce.Exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private  String message;
    private  boolean success;
    private LocalDateTime time;
    public ErrorResponse(){this.time = LocalDateTime.now();
    }

    public ErrorResponse(String message, boolean b) {
        this();
        this.message = message;
        this.success = b;
    }
}
