package com.basanta.OrderService.security;

import com.basanta.OrderService.service.OrderServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
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

    //@Autowired
    //private OrderServiceImp username;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth->auth
                             .anyRequest().authenticated())
                .oauth2ResourceServer(server->server
                        .jwt(jwt->jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();


    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt->{
            JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
            Collection<GrantedAuthority> authorities = authoritiesConverter.convert(jwt);
            //username = jwt.getClaimAsString("https://basantanembang.com/email");
            //username.username = jwt.getClaimAsString("https://basantanembang.com/email");
            //System.out.println("the username   "+ username);

            Collection<GrantedAuthority> authorityCollection = jwt.getClaimAsStringList("https://basantanembang.com/roles")
                    .stream()
                    .map(role-> new SimpleGrantedAuthority("ROLE_"+role))
                    .collect(Collectors.toList());
                authorities.addAll(authorityCollection);
            return authorities;
        });
    return converter;
    }

}
