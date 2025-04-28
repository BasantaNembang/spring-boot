package com.basanta.OrderService.service;

import com.basanta.OrderService.entity.Order;
import com.basanta.OrderService.error.OrderExceptionHandler;

import com.basanta.OrderService.event.OrderPlacedEvent;
import com.basanta.OrderService.externalStuff.*;
import com.basanta.OrderService.fiegnClient.PaymentServiceImp;
import com.basanta.OrderService.fiegnClient.ProductServiceImp;
import com.basanta.OrderService.model.OrderDto;
import com.basanta.OrderService.model.OrderMode;
import com.basanta.OrderService.model.OrderStatus;
import com.basanta.OrderService.repo.OrderRepo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
public class OrderServiceImp implements OrderService{

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ProductServiceImp productService;

    @Autowired
    private PaymentServiceImp paymentService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public String username;

    @Override
    @Transactional
    public ResponseEntity<String> placeOrder(OrderDto orderDto) {

        OrderMode orderMode0 = orderDto.order_mode();
        //reduce the quantity
        productService.reduceProductByOrder(orderDto.product_id(), orderDto.quantity());

        //place order
        Order order =  new Order();
        order.setId(UUID.randomUUID().toString().substring(0,5));
        order.setProduct_id(orderDto.product_id());
        order.setOrder_status(OrderStatus.PENDING);
        order.setQuantity(orderDto.quantity());
        order.setOrder_mode(orderMode0);
        order.setAmount(orderDto.amount());
        order.setOrder_date(Instant.now());

        //do payment
        PaymentResponse paymentResponse = null;
        PaymentDto paymentDto = null;
        try {
            paymentResponse  = new PaymentResponse();
            paymentResponse.setAmount(order.getAmount());
            paymentResponse.setOrderId(order.getId());
            paymentResponse.setPayment_date(Instant.now());
            paymentResponse.setOrderMode_mode(orderMode0);
            paymentResponse.setStatus(Status.ACCEPTED);
            paymentResponse.setQuantity(order.getQuantity());

            paymentDto = new PaymentDto(paymentResponse.getAmount(), paymentResponse.getOrderId(), paymentResponse.getPayment_date(),
                paymentResponse.getOrderMode_mode(),  paymentResponse.getStatus(), null, paymentResponse.getQuantity());

        } catch (Exception e) {
            order.setOrder_status(OrderStatus.REJECTED);

            paymentDto = new PaymentDto(paymentResponse.getAmount(), paymentResponse.getOrderId(), paymentResponse.getPayment_date(),
                    orderMode0,  Status.REJECTED, null, paymentResponse.getQuantity());

            e.printStackTrace();
            throw new OrderExceptionHandler(e.getMessage());
        }

        try{
            paymentService.doPayment(paymentDto);  //do payment
            System.out.println(paymentDto);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderExceptionHandler(e.getMessage());
        }
        order.setOrder_status(OrderStatus.PLACED);

        orderRepo.save(order);


        //send notification
        OrderPlacedEvent event = new OrderPlacedEvent();
        event.setOrderNumber(order.getId());
        event.setSubject("Your Order Has Been Placed");
        event.setUser(getUserName());
        //System.out.println(event);
        kafkaTemplate.send("order-place", event);

        return ResponseEntity.status(HttpStatus.OK).body("order place successfully");
    }



    @Override
    @Cacheable(value="orderDtoCache",key = "#id")
    @CircuitBreaker(name = "external", fallbackMethod = "fallbackOrderDetails")
    public OrderDto getOrderDetails(String id) {
        Order order = orderRepo.findById(id).orElseThrow(()->new OrderExceptionHandler("Order not found!.."));

       //get Product info from product service
       ProductDto productDto  =
               restTemplate.getForObject("http://PRODUCTSERVICE/product/get/"+order.getProduct_id(), ProductDto.class);

       log.info("Product data  {}  ",productDto);

       //get Payment info as well
       PaymentDto paymentDto =  restTemplate.getForObject("http://PAYMENTSERVICE/payment/order/"+order.getId(), PaymentDto.class);


        OrderDto dto = new OrderDto(order.getId(), order.getProduct_id(), order.getQuantity(),
                order.getOrder_status(), order.getOrder_mode(), order.getAmount(),
               order.getOrder_date(), productDto, paymentDto);
        return dto;
    }


    //fallback method of getOrderDetails
    public OrderDto fallbackOrderDetails(String id, Throwable e){
        log.info("{ }  : some service is down... ");
        log.info("error is   { }  ",  e.getMessage());
        return null;
    }


    //delete Order
    @Override
    @Transactional
    @CacheEvict(value="orderDtoCache",key = "#id")
    @CircuitBreaker(name = "external", fallbackMethod = "fallbackOrderCancel")
    public ResponseEntity<?> deleteOrder(String id, String paymentid) {
        Order order  =   orderRepo.findById(id).orElseThrow(()->new OrderExceptionHandler("No Order Found"));
        //also delete the payment details here and update the Product quantity

        //delete payment details
        paymentService.cancelPayment(paymentid);
        //Add the product quantity
        productService.addProductIfOrderCancel(order.getProduct_id(), order.getQuantity());

        orderRepo.delete(order);
        return ResponseEntity.status(HttpStatus.OK).body("order has been canceled");

    }

    public ResponseEntity<?> fallbackOrderCancel(String id, int quantity, String paymentid, Throwable e){
        log.info("{ }  : some service is down... ");
        log.info("error is   { }  ",  e.getMessage());
        return null;
    }


    //get username
    private String getUserName(){
        var authentication =  SecurityContextHolder.getContext().getAuthentication();
        System.out.println("auth  ::");
        System.out.println(authentication);
        if(authentication!=null && authentication.getPrincipal() instanceof Jwt jwt){
            return jwt.getClaimAsString("https://basantanembang.com/email");
        }
        return null;
    }



}
