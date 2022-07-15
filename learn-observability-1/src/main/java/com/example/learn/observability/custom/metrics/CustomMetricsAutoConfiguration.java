package com.example.learn.observability.custom.metrics;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.LinkedList;
import java.util.Queue;

@AutoConfiguration
@Import(MeterBinderBeanRegistrar.class)
public class CustomMetricsAutoConfiguration {

    @Bean
    Queue queue1(){
        Queue queue = new LinkedList();
        queue.add("kim");
        return queue;
    }
    @Bean
    Queue queue2(){
        Queue queue = new LinkedList();
        queue.add("Annie");
        queue.add("kim");
        return queue;
    }


   /*
    @Bean
    QueueBeanPostProcessor queueBeanPostProcessor(){
        return new QueueBeanPostProcessor();
    }*/


}
