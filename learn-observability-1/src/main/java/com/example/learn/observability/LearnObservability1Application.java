package com.example.learn.observability;

import com.example.learn.observability.endpoint.LogStatsEndpoint;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@SpringBootApplication
public class LearnObservability1Application {

	public static void main(String[] args) {
		SpringApplication.run(LearnObservability1Application.class, args);
	}

	@Bean
	public LogStatsEndpoint logStatsEndpoint(){
		return new LogStatsEndpoint();
	}

	@Bean
	Queue queue1(){
		Queue queue = new LinkedList();
		queue.add("tom");
		return queue;
	}
	@Bean
	Queue queue2(){
		Queue queue = new LinkedList();
		queue.add("kim");
		queue.add("ketty");
		return queue;
	}
	/*
	@Bean
	public MeterBinder queueSize(List<Queue> queues) {
		for(int i=0;i<queues.size();i++){
			Queue queue = queues.get(i);
			return (registry) -> Gauge.builder("queue-"+i+"-size", queue::size).register(registry);
		}
	}
	 */
}
