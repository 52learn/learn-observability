package com.example.learn.observability.endpoint;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Endpoint(id = "orderStatsEndpoint")
public class OrderStatsEndpoint {
 
    @ReadOperation
    public Map<String, Object> todayOrderCount() {
        Map<String, Object> map = new HashMap<>(16);
        map.put("count", 100);
        return map;
    }
}