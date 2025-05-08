package com.basanta.ApiGatway.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class MyConfiguration {

   @Bean
   public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer(){
       return factory->factory.configureDefault(
               id->new Resilience4JConfigBuilder(id)
                       .circuitBreakerConfig(
                               CircuitBreakerConfig.ofDefaults()
                       ).build()
       );
   }


    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just("userKey");
    }


    @Bean
    public ObservedAspect observedAspect(ObservationRegistry observationRegistry){
        return new ObservedAspect(observationRegistry);
    }


}
