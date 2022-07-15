package com.example.learn.observability.custom.metrics;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Queue;

/**
 * Function description:
 * Generate all Queues MeterBinder , then We can inspect into these Queues
 *
 * Algorithm description:
 * 1. Find all Queue Bean in Spring Context
 * 2. Get the beanDefinition of MyQueueMeterBinder
 * 3. register the beanDefinition of MyQueueMeterBinder and generate bean
 *
 * @author kim
 */
public class MeterBinderBeanRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        if(registry instanceof DefaultListableBeanFactory){
            DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)registry;
            Map<String, Queue> queueMap = beanFactory.getBeansOfType(Queue.class);
            if(!CollectionUtils.isEmpty(queueMap)){
                queueMap.forEach((beanName,queue)->{
                    BeanDefinitionBuilder bdb = BeanDefinitionBuilder.genericBeanDefinition(MyQueueMeterBinder.class);
                    BeanDefinition beanDefinition1 = bdb.getBeanDefinition();
                    bdb.addConstructorArgValue(queue);
                    bdb.addConstructorArgValue(beanName);
                    registry.registerBeanDefinition(MyQueueMeterBinder.class.getSimpleName()+"-"+beanName, beanDefinition1);
                });
            }
        }
    }
}
