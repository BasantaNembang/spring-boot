package com.basanta.OrderService.interceptor;

import com.basanta.OrderService.service.TokenService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@Configuration
public class RestTempleteInterceptor implements ClientHttpRequestInterceptor {

    private final TokenService tokenService;

    public RestTempleteInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }


    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String token = tokenService.getToken();

        if(token!=null){
           request.getHeaders().add("Authorization", "Bearer "+token);
        }

        return execution.execute(request, body);
    }



}
