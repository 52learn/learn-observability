package com.example.learn.observability.endpoint;

import lombok.Data;

@Data
public class LogLevelInfo {
    private String logLevel;
    private Integer records;
}
