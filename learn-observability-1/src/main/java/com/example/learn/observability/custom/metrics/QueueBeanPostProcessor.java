package com.example.learn.observability.custom.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Queue;

/**
 * 无效:this.beanFactory.registerSingleton 方法无法注入到IOC容器中
 */
public class QueueBeanPostProcessor implements BeanPostProcessor , BeanFactoryAware {
    private ConfigurableBeanFactory beanFactory;
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Queue) {
            Queue queue = (Queue)bean;
            //"queue-"+beanName+"-size"
            final String finalBeanName = beanName+"-meterBinder";
            MeterBinder meterBinder = (registry) -> Gauge.builder(finalBeanName, queue::size).register(registry);
            //this.beanFactory.registerSingleton(finalBeanName,meterBinder);
            this.beanFactory.registerSingleton(beanName+"Test","meterBinder");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableBeanFactory)beanFactory;
    }
}
