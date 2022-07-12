package com.example.learn.observability.httptrace;

import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class HttpTraceAutoConfiguration {
    @Bean
    HttpTraceRepository httpTraceRepository(){
        return new InMemoryHttpTraceRepository();
    }
}
