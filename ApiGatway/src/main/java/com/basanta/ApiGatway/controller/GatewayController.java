package com.basanta.ApiGatway.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayController {

    @GetMapping("/productServiceFallback")
    public String unavailableProductServiceHandel(){
        return "ProductService not unavailable!...";
    }

    @GetMapping("/paymentServiceFallback")
    public String unavailablePaymentServiceHandel(){
        return "PaymentService not unavailable!...";
    }

    @GetMapping("/orderServiceFallback")
    public String unavailableOrderServiceHandel(){
        return "OrderService not unavailable!...";
    }



}
