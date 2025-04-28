package com.basanta.OrderService.fiegnClient.services;


import com.basanta.OrderService.externalStuff.PaymentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "PaymentService")
public interface PaymentService {



    @PostMapping("/payment/order")
    public ResponseEntity<String> doPayment(@RequestBody PaymentDto paymentDto);

    @DeleteMapping("/payment/cancel/{id}")
    public ResponseEntity<String> cancelPayment(@PathVariable("id") String id);





}
