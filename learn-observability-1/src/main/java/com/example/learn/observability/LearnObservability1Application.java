package com.example.learn.observability;

import com.example.learn.observability.endpoint.LogStatsEndpoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LearnObservability1Application {

	public static void main(String[] args) {
		SpringApplication.run(LearnObservability1Application.class, args);
	}

	@Bean
	public LogStatsEndpoint logStatsEndpoint(){
		return new LogStatsEndpoint();
	}

}
