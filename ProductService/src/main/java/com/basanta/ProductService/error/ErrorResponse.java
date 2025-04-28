package com.basanta.ProductService.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ErrorResponse {

    private String msg;
    private boolean flag;
    private HttpStatus httpStatus;
}
