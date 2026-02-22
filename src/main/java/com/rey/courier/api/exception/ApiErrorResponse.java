package com.rey.courier.api.exception;

import java.time.LocalDateTime;

public class ApiErrorResponse {
    
    private LocalDateTime timestamp;
    private int status;
    private String error;
    
    // We use Object here so it can hold EITHER a simple String ("Not found") 
    // OR a Map of validation errors ({ "weight": "must be positive" })
    private Object message; 
    
    private String path;

    public ApiErrorResponse(LocalDateTime timestamp, int status, String error, Object message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    // Getters are strictly required for Spring to convert this object to JSON!
    public LocalDateTime getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getError() { return error; }
    public Object getMessage() { return message; }
    public String getPath() { return path; }
}