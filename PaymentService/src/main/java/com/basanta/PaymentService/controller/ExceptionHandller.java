package com.basanta.PaymentService.controller;


import com.basanta.PaymentService.error.PayMentException;
import com.basanta.PaymentService.model.PaymentErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandller {


    @ExceptionHandler(PayMentException.class)
    public ResponseEntity<PaymentErrorResponse> handlerPaymentError(PayMentException exception){
        PaymentErrorResponse response = new PaymentErrorResponse();
        response.setMsg(exception.getMessage());
        response.setFlag(false);
        response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

    }
}
