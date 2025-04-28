package com.basanta.PaymentService.service;


import com.basanta.PaymentService.entity.PaymentDetails;
import com.basanta.PaymentService.error.PayMentException;
import com.basanta.PaymentService.model.PaymentDto;
import com.basanta.PaymentService.model.Status;
import com.basanta.PaymentService.repo.PaymentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class PaymentServiceImp implements PaymentService{

    @Autowired
    private PaymentRepo paymentRepo;


    @Override
    @Transactional
    public ResponseEntity<String> doPayment(PaymentDto paymentDto) {
        System.out.println(paymentDto);
        PaymentDetails paymentDetails = PaymentDetails.builder()
                .id(UUID.randomUUID().toString().substring(0,5))
                .payment_date(Instant.now())
                .order_mode(paymentDto.orderMode_mode())
                .reference_number(paymentDto.reference_number())
                .status(Status.ACCEPTED)
                .OrderId(paymentDto.OrderId())
                .amount(paymentDto.amount()*paymentDto.quantity())
                .build();
        System.out.println(paymentDetails);
        paymentRepo.save(paymentDetails);
        return ResponseEntity.status(HttpStatus.OK).body("Payment completed");
    }


    @Override
    public PaymentDto getPaymentResponse(String id) {

         PaymentDetails paymentDetails = paymentRepo.findByOrderId(id);

         if(paymentDetails == null){
            throw  new PayMentException("No payment found of given id");
         }
         PaymentDto dto = PaymentDto.builder()
                 .OrderId(paymentDetails.getOrderId())
                 .amount(paymentDetails.getAmount())
                 .payment_date(paymentDetails.getPayment_date())
                 .status(paymentDetails.getStatus())
                 .orderMode_mode(paymentDetails.getOrder_mode())
                 .reference_number(paymentDetails.getReference_number())
                 .build();
        return dto;
    }


    @Override
    @Transactional
    public ResponseEntity<String> deletePaymentDetails(String id) {
         PaymentDetails details = paymentRepo.findById(id).orElseThrow(()->new PayMentException("No such payment is done") );
         paymentRepo.delete(details);
        return ResponseEntity.status(HttpStatus.OK).body("deleted");
    }


}
