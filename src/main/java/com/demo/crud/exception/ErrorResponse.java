package com.demo.crud.exception;

import java.time.Instant;
import lombok.Data;

@Data
public class ErrorResponse {

    private int status;
    private String error;
    private String message;
    private Instant timestamp;

    public ErrorResponse() {
    }

    public ErrorResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = Instant.now();
    }

    public ErrorResponse(int status, String error, String message, Instant timestamp) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
    }
}
