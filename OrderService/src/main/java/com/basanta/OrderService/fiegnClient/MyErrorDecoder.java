package com.basanta.OrderService.fiegnClient;

import com.basanta.OrderService.error.ErrorResponse;
import com.basanta.OrderService.error.OrderExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;


import java.io.IOException;

public class MyErrorDecoder implements ErrorDecoder {


    @Override
    public Exception decode(String methodKey, Response response) {

        ObjectMapper mapper = new ObjectMapper();

        ErrorResponse errorResponse= null;
        try {
            errorResponse   = mapper.readValue(response.body().asInputStream(), ErrorResponse.class);
         } catch (IOException e) {throw new OrderExceptionHandler("Can`t convert the Response"); }

        return new OrderExceptionHandler(errorResponse.getMsg());

    }
}
