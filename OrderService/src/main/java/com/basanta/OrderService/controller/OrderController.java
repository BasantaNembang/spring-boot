package com.basanta.OrderService.controller;

import com.basanta.OrderService.model.OrderDto;

import com.basanta.OrderService.service.OrderServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderServiceImp orderService;

    @PreAuthorize("hasAnyRole('Customer')")
    @PostMapping("/place-order")
    public ResponseEntity<String> placeOrder(@RequestBody OrderDto orderDto){
        return orderService.placeOrder(orderDto);
    }

    @PreAuthorize("hasAnyRole('Admin', 'Customer')")
    @GetMapping("/get/{id}")
    public ResponseEntity<OrderDto> getOrderDetails(@PathVariable("id") String id){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderDetails(id));
    }


    //delete Order
    @PreAuthorize("hasAnyRole('Admin', 'Customer')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id") String id,
                                         @RequestParam String paymentid){
        return orderService.deleteOrder(id, paymentid);
    }



}
