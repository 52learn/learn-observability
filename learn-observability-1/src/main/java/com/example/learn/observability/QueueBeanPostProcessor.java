package com.example.learn.observability;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Queue;

public class QueueBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Queue) {
            Queue queue = (Queue)bean;
            MeterBinder meterBinder = (registry) -> Gauge.builder("queue-"+beanName+"-size", queue::size).register(registry);


        }
        return bean;
    }
}
