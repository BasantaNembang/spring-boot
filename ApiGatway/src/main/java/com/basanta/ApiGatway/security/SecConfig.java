package com.basanta.ApiGatway.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class SecConfig {

    @Value("${auth0.audience}")
    private String audience;

    private final ReactiveClientRegistrationRepository registrationRepository;

    public SecConfig(ReactiveClientRegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity httpSecurity){
        httpSecurity
                .csrf(csrf -> csrf.disable())

                .authorizeExchange(auth->auth
                         .pathMatchers("/actuator/health").permitAll() 
                         .anyExchange().authenticated()
                )
                .oauth2Login(oAuth2LoginSpec ->
                        oAuth2LoginSpec.authorizationRequestResolver(serverOAuth2AuthorizationRequestResolver(registrationRepository)))
                .oauth2ResourceServer(server->server
                        .jwt(jwt->jwt.jwtAuthenticationConverter(converterAdapter())));

        return  httpSecurity.build();
    }


    private ServerOAuth2AuthorizationRequestResolver serverOAuth2AuthorizationRequestResolver(ReactiveClientRegistrationRepository registrationRepository
    ){
        DefaultServerOAuth2AuthorizationRequestResolver resolver =
        new DefaultServerOAuth2AuthorizationRequestResolver(registrationRepository);
        resolver.setAuthorizationRequestCustomizer(builderConsumer());
        return resolver;
    }


    private Consumer<OAuth2AuthorizationRequest.Builder> builderConsumer(){
       return customizer->customizer
               .additionalParameters(
                       params-> params.put("audience", audience)
               );
    }

    //convertor
    private ReactiveJwtAuthenticationConverterAdapter converterAdapter(){
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt->{
            JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();

           Collection<GrantedAuthority> authorities = authoritiesConverter.convert(jwt);

           Collection<GrantedAuthority> grantedAuthorityCollection = jwt.getClaimAsStringList("https://basantanembang.com/roles")
                   .stream()
                   .map(role->new SimpleGrantedAuthority("ROLE_"+role))
                   .collect(Collectors.toList());
           authorities.addAll(grantedAuthorityCollection);
           return authorities;
        });

        return new  ReactiveJwtAuthenticationConverterAdapter(converter);

    }


}


