package com.basanta.PaymentService.service;

import com.basanta.PaymentService.model.PaymentDto;
import org.springframework.http.ResponseEntity;

public interface PaymentService {

    ResponseEntity<String> doPayment(PaymentDto paymentDto);

    PaymentDto getPaymentResponse(String id);

    ResponseEntity<String> deletePaymentDetails(String id);


}
