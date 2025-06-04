package com.basanta.OrderService.fiegnClient.services;


import com.basanta.OrderService.externalStuff.PaymentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "payment", url = "${microservice.payment}")
public interface PaymentService {



    @PostMapping("/order")
    public ResponseEntity<String> doPayment(@RequestBody PaymentDto paymentDto);

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<String> cancelPayment(@PathVariable("id") String id);





}
