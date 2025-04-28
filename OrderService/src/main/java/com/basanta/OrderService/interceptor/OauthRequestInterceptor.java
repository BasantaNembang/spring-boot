package com.basanta.OrderService.interceptor;

import com.basanta.OrderService.service.TokenService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OauthRequestInterceptor implements RequestInterceptor {

    private final TokenService tokenService;

    public OauthRequestInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }


    @Override
    public void apply(RequestTemplate template) {
        String token = tokenService.getToken();

        if(token!=null){
         template.header("Authorization", "Bearer "+token);
        }
    }


}
