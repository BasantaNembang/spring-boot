package com.basanta.OrderService.fiegnClient;

import com.basanta.OrderService.error.OrderExceptionHandler;
import com.basanta.OrderService.fiegnClient.services.ProductService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImp {


    private final ProductService productService;

    public ProductServiceImp(ProductService productService) {
        this.productService = productService;
    }

     @CircuitBreaker(name = "external", fallbackMethod = "fallbackForReduceProduct")
     public  ResponseEntity<String> reduceProductByOrder(String id,
                                                 int quantity){

        return productService.reduceProductByOrder(id, quantity);
    }



    @CircuitBreaker(name = "external", fallbackMethod = "fallbackForAddProduct")
    public ResponseEntity<HttpStatus> addProductIfOrderCancel(String id,
                                                        int quantity){

        return productService.addProductIfOrderCancel(id, quantity);
    }


    public ResponseEntity<String> fallbackForReduceProduct(String id, int quantity, Throwable  e){
        //return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product Service is facing some issue");
        throw new OrderExceptionHandler(e.getMessage());
    }

    public ResponseEntity<HttpStatus> fallbackForAddProduct(String id, int quantity, Throwable e){
       // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(HttpStatus.INTERNAL_SERVER_ERROR);
        throw new OrderExceptionHandler(e.getMessage());
    }


}
