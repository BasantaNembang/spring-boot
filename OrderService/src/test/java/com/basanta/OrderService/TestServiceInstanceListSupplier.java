package com.basanta.OrderService;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

public class TestServiceInstanceListSupplier implements ServiceInstanceListSupplier {


    @Override
    public String getServiceId() {
        return "";
    }

    @Override
    public Flux<List<ServiceInstance>> get() {

        List<ServiceInstance> instanceList = new ArrayList<>();

        instanceList.add(new DefaultServiceInstance(
             "ProductService",
              "ProductService",
              "localhost",
                8080,
              false
        ));
        instanceList.add(new DefaultServiceInstance(
                "OrderService",
                "OrderService",
                "localhost",
                8080,
                false
        ));
        return Flux.just(instanceList);
    }



}
