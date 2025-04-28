package com.basanta.OrderService.controller;


import com.basanta.OrderService.error.ErrorResponse;
import com.basanta.OrderService.error.OrderExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandlers {

   @ExceptionHandler(OrderExceptionHandler.class)
   public ResponseEntity<ErrorResponse> handelResponse(OrderExceptionHandler exceptionHandler){

      ErrorResponse errorResponse = new ErrorResponse();
      errorResponse.setMsg(exceptionHandler.getMessage());
      errorResponse.setFlag(false);
      errorResponse.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
   }


}
