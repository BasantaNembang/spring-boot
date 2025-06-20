package com.basanta.ApiGatway.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;



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
        return exchange -> ReactiveSecurityContextHolder.getContext()
        .map(ssc->ssc.getAuthentication()
               .getCredentials().toString());
    }


    @Bean
    public ObservedAspect observedAspect(ObservationRegistry observationRegistry){
        return new ObservedAspect(observationRegistry);
    }


}
