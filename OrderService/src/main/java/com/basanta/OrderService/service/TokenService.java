package com.basanta.OrderService.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class TokenService {


    public String getToken(){
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(requestAttributes!=null){
               HttpServletRequest servlet = requestAttributes.getRequest();
               String authHeader  =   servlet.getHeader("Authorization");
               if(authHeader!=null && authHeader.startsWith("Bearer ")){
                   //System.out.println("The token is" );
                  // System.out.println(authHeader.substring(7));
                    return authHeader.substring(7);
               }

        }

        return null;
    }



}
