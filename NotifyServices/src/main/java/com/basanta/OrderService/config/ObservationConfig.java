package com.basanta.OrderService.config;


import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import io.micrometer.core.instrument.config.MeterFilter;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

@Configuration
public class ObservationConfig {


//    @Autowired
//    private ConcurrentKafkaListenerContainerFactory concurrentKafkaListenerContainerFactory;
//
//    @PostConstruct
//    public void setConcurrentKafkaListenerContainerFactory(){
//        concurrentKafkaListenerContainerFactory.getContainerProperties().setObservationEnabled(true);
//    }


    @Bean
    public ObservedAspect observedAspect(ObservationRegistry observationRegistry){
        return new ObservedAspect(observationRegistry);
    }


}
