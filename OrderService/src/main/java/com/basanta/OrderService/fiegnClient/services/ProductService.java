package com.basanta.OrderService.fiegnClient.services;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "product", url = "${microservice.product}")
public interface ProductService {

     @PutMapping("/reduce-product/{id}")
     ResponseEntity<String> reduceProductByOrder(@PathVariable("id") String id,
                                                       @RequestParam int quantity);


     @PutMapping("/add-product-quant/{id}")
     ResponseEntity<HttpStatus> addProductIfOrderCancel(@PathVariable("id") String id,
                                                              @RequestParam int quantity);



}
