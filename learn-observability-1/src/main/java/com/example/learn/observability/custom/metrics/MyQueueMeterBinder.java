package com.example.learn.observability.custom.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

import java.util.Queue;

public  class MyQueueMeterBinder implements MeterBinder {
        private Queue queue;
        private String queueName;

        MyQueueMeterBinder(Queue queue,String queueName){
            this.queue = queue;
            this.queueName = queueName;
        }
        @Override
        public void bindTo(MeterRegistry registry) {
            Gauge
                .builder(this.queueName+"-queueSize", queue::size)
                .description(this.queueName+" size").baseUnit("个")
                .register(registry);
        }
    }