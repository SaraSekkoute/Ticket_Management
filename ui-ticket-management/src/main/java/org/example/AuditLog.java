package org.example;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditLog {
    private Long Id;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    private String action;

    private String details;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public String getAction() {
        return action;
    }

    public AuditLog(Long id, String action, String details, LocalDateTime timestamp) {
        Id = id;
        this.action = action;
        this.details = details;
        this.timestamp = timestamp;
    }

    public AuditLog() {
    }

    public void setAction(String action) {
        this.action = action;
    }

    public AuditLog(String action, String details, LocalDateTime timestamp) {
        this.action = action;
        this.details = details;
        this.timestamp = timestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "AuditLog{" +
                "action=" + action +
                "details=" + details +
                "timestamp=" + timestamp +
                '}';
    }

}
