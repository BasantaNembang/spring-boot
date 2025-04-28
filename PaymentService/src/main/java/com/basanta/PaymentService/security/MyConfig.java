package com.basanta.PaymentService.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class MyConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth->auth
                        //.requestMatchers("/payment/**").hasAnyRole("Admin", "Customer")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(server->server
                        .jwt(jwt-> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();


    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt->{
            JwtGrantedAuthoritiesConverter defaultConvetor = new JwtGrantedAuthoritiesConverter();
            Collection<GrantedAuthority> authorities = defaultConvetor.convert(jwt);

            Collection<GrantedAuthority> grantedAuthorities = jwt.getClaimAsStringList("https://basantanembang.com/roles")
                    .stream()
                    .map(role->new SimpleGrantedAuthority("ROLE_"+role))
                    .collect(Collectors.toList());

            authorities.addAll(grantedAuthorities);
            return  authorities;
        });
        return  converter;
    }

}
