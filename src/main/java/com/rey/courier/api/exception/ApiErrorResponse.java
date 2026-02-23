package com.rey.courier.api.exception;

import java.time.LocalDateTime;

public class ApiErrorResponse {
    
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private Object message; 
    private String path;

    // Default constructor
    public ApiErrorResponse() {}

    // All-args constructor
    public ApiErrorResponse(LocalDateTime timestamp, int status, String error, Object message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    // Getters (Required for JSON serialization)
    public LocalDateTime getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getError() { return error; }
    public Object getMessage() { return message; }
    public String getPath() { return path; }

    // Setters (Good practice for standard classes)
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public void setStatus(int status) { this.status = status; }
    public void setError(String error) { this.error = error; }
    public void setMessage(Object message) { this.message = message; }
    public void setPath(String path) { this.path = path; }
}