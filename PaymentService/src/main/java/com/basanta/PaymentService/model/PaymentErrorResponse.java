package com.basanta.PaymentService.model;

import lombok.Data;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Data
@ToString
public class PaymentErrorResponse {

    private String msg;
    private boolean flag;
    private HttpStatus httpStatus;
}
