package com.basanta.PaymentService.controller;



import com.basanta.PaymentService.model.PaymentDto;
import com.basanta.PaymentService.service.PaymentServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {


    @Autowired
    private PaymentServiceImp  paymentService;

    @PostMapping("/order")
    @PreAuthorize("hasAnyRole('Customer')")
    public ResponseEntity<String> doPayment(@RequestBody PaymentDto paymentDto){
        return paymentService.doPayment(paymentDto);
    }

    @GetMapping("/order/{id}")
    @PreAuthorize("hasAnyRole('Admin', 'Customer')")
    public PaymentDto getPaymentReponse(@PathVariable("id") String id){
        return paymentService.getPaymentResponse(id);
    }

    @DeleteMapping("/cancel/{id}")
    @PreAuthorize("hasAnyRole('Customer', 'Admin')")
    public ResponseEntity<String> cancelPayment(@PathVariable("id") String id){
        return paymentService.deletePaymentDetails(id);
    }



}
