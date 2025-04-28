package com.basanta.ProductService.controller;


import com.basanta.ProductService.error.ErrorResponse;
import com.basanta.ProductService.error.ResourseNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandller {


    @ExceptionHandler(ResourseNotFound.class)
    public ResponseEntity<ErrorResponse> handlerError(ResourseNotFound notFound){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMsg(notFound.getMessage());
            errorResponse.setFlag(false);
            errorResponse.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }



}
