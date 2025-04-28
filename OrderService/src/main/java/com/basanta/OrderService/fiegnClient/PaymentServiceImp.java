package com.basanta.OrderService.fiegnClient;


import com.basanta.OrderService.error.OrderExceptionHandler;
import com.basanta.OrderService.externalStuff.PaymentDto;
import com.basanta.OrderService.fiegnClient.services.PaymentService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class PaymentServiceImp {

    private final PaymentService paymentService;


    public PaymentServiceImp(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @CircuitBreaker(name = "external", fallbackMethod = "fallbackForPayment")
    public ResponseEntity<String> doPayment(PaymentDto paymentDto){
        return paymentService.doPayment(paymentDto);
    }


    @CircuitBreaker(name = "external", fallbackMethod = "fallbackForPaymentCancel")
    public ResponseEntity<String> cancelPayment(String id){
        return paymentService.cancelPayment(id);
    }

    public ResponseEntity<String> fallbackForPayment(PaymentDto paymentDto, Throwable e){
       // throw new OrderExceptionHandler("error   "+ e.getMessage());
        log.info(e.getMessage());
        throw new OrderExceptionHandler(e.getMessage());
       // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment Service is Facing some issue");
    }

    public ResponseEntity<String>  fallbackForPaymentCancel(String id, Throwable e){
        log.info(e.getMessage());
        throw new OrderExceptionHandler(e.getMessage());
       // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment Service is Facing some issue");
    }


}

