package com.example.learn.observability.custom.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.LinkedList;
import java.util.Queue;

@AutoConfiguration
public class CustomMetricsAutoConfiguration {

    @Bean
    Queue queue1(){
        Queue queue = new LinkedList();
        queue.add("tom");
        return queue;
    }

    @Bean
    public MeterBinder queueSize(@Qualifier(value = "queue1") Queue queue) {
        return (registry) -> Gauge
                .builder("queue1-queueSize", queue::size)
                .description("queue1 size").baseUnit("个")
                .register(registry);
    }

   /*
    @Bean
    QueueBeanPostProcessor queueBeanPostProcessor(){
        return new QueueBeanPostProcessor();
    }*/
}
