package com.example.learn.observability.httptrace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class AController {
    @Value("${server.port:8080}")
    private Integer port;
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/a")
    public String a(){
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://127.0.0.1:"+port+"/b", HttpMethod.GET,null,String.class);
        String body = responseEntity.getBody();
        return "a->"+body;
    }
}
