package com.example.learn.observability;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
public class LogLevelConfigController {

    @GetMapping("/log")
    public void log(){
        log.trace("log trace ......");
        log.debug("log debug ......");
        log.info("log info ......");
        log.warn("log warn ......");
        log.error("log error ......");
    }
}
