package com.basanta.OrderService.config;


import com.basanta.OrderService.fiegnClient.MyErrorDecoder;
import com.basanta.OrderService.interceptor.RestTempleteInterceptor;
import com.basanta.OrderService.model.OrderDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.codec.ErrorDecoder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;

@Configuration
@EnableCaching
public class DecoderConfig {


   @Bean
   ErrorDecoder errorDecoder(){
       return new MyErrorDecoder();
   }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTempleteInterceptor interceptor){
       RestTemplate restTemplate = new RestTemplate();
       restTemplate.setInterceptors(List.of(interceptor));
       return restTemplate;
    }


    @Bean
    public RedisCacheManager manager(RedisConnectionFactory redisConnectionFactory){

       ObjectMapper mapper = new ObjectMapper();
       mapper.registerModule(new JavaTimeModule());
       mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

      Jackson2JsonRedisSerializer<OrderDto> serializer = new Jackson2JsonRedisSerializer<>(mapper, OrderDto.class);

      RedisCacheConfiguration cacheConfiguration
            =  RedisCacheConfiguration.defaultCacheConfig()
                                                   .entryTtl(Duration.ofMinutes(10))
                                                   .disableCachingNullValues()
                                                   .serializeValuesWith(RedisSerializationContext.SerializationPair
                                                   .fromSerializer(serializer));
    return RedisCacheManager
            .builder(redisConnectionFactory)
            .cacheDefaults(cacheConfiguration)
            .build();
    }


}
