package com.example.learn.observability.httptrace;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BController {

    @GetMapping("/b")
    public String b(){
        return "b";
    }
}
