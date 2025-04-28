package com.basanta.ApiGatway.controller;



import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("/authenticate")
public class AuthController {


    private final ReactiveOAuth2AuthorizedClientService clientService;

    public AuthController(ReactiveOAuth2AuthorizedClientService clientService) {
        this.clientService = clientService;
    }


    @GetMapping("/login")
    public Mono<String> getTokens(Principal principal){
       return clientService.loadAuthorizedClient("auth0", principal.getName())
               .map(OAuth2AuthorizedClient->{
                   OAuth2AccessToken oAuth2AccessToken =
                           OAuth2AuthorizedClient.getAccessToken();
                   return oAuth2AccessToken.getTokenValue();
               })
               .defaultIfEmpty("No Token found");
    }


}
