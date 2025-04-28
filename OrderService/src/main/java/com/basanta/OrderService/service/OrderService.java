package com.basanta.OrderService.service;


import com.basanta.OrderService.model.OrderDto;
import org.springframework.http.ResponseEntity;

public interface OrderService {

    ResponseEntity<String> placeOrder(OrderDto orderDto);

    OrderDto getOrderDetails(String id);

    ResponseEntity<?> deleteOrder(String id, String paymentid);


}
