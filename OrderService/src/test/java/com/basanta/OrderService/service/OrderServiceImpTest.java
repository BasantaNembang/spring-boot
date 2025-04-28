package com.basanta.OrderService.service;


import com.basanta.OrderService.entity.Order;
import com.basanta.OrderService.error.OrderExceptionHandler;
import com.basanta.OrderService.event.OrderPlacedEvent;
import com.basanta.OrderService.externalStuff.PaymentDto;
import com.basanta.OrderService.externalStuff.ProductDto;
import com.basanta.OrderService.externalStuff.Status;
import com.basanta.OrderService.fiegnClient.PaymentServiceImp;
import com.basanta.OrderService.fiegnClient.ProductServiceImp;
import com.basanta.OrderService.model.OrderDto;
import com.basanta.OrderService.model.OrderMode;
import com.basanta.OrderService.model.OrderStatus;
import com.basanta.OrderService.repo.OrderRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@SpringBootTest
public class OrderServiceImpTest {

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private ProductServiceImp productService;

    @Mock
    private PaymentServiceImp paymentService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    @InjectMocks
    OrderService orderService = new OrderServiceImp();



    @DisplayName("Get-OrderDetails Success Case")
    @Test
    void test_getOrderDetails(){

        //MOCK

        //get Order
        Order order = getMockOrder();
        Mockito.when(orderRepo.findById(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(order));

        //get ProductDto
        Mockito.when(restTemplate.getForObject("http://PRODUCTSERVICE/product/get/"+order.getProduct_id(), ProductDto.class))
                .thenReturn(getMockProductDto());

        //PaymentDto
        Mockito.when(restTemplate.getForObject("http://PAYMENTSERVICE/payment/order/"+order.getId(), PaymentDto.class))
                .thenReturn(getMockPaymentDto());


        //METHOD-CALL
        OrderDto orderDto =  orderService.getOrderDetails("mwp188");


        //VERIFICATION
        Mockito.verify(orderRepo, Mockito.times(1)).findById(ArgumentMatchers.anyString());
        Mockito.verify(restTemplate, Mockito.times(1))
                .getForObject("http://PRODUCTSERVICE/product/get/"+order.getProduct_id(), ProductDto.class);
        Mockito.verify(restTemplate, Mockito.times(1))
                .getForObject("http://PAYMENTSERVICE/payment/order/"+order.getId(), PaymentDto.class);


        //ASSERT
        Assertions.assertNotNull(orderDto);
        Assertions.assertEquals(order.getId(), orderDto.id());

    }




    @DisplayName("Get-OrderDetails Fail Case")
    @Test
    void test_getOrderDetails_fail_case(){


    Mockito.when(orderRepo.findById(ArgumentMatchers.anyString()))
            .thenReturn(Optional.ofNullable(null));

    //OrderDto orderDto =  orderService.getOrderDetails("mwp188");

    OrderExceptionHandler exceptionHandler = Assertions.assertThrows(OrderExceptionHandler.class,
            ()->orderService.getOrderDetails("mwp188"));


    Mockito.verify(orderRepo, Mockito.times(1)).findById(ArgumentMatchers.anyString());

    }



    @DisplayName("Place Order Success case")
    @Test
    void place_order(){

        //MOCK
        Mockito.when(productService.reduceProductByOrder(ArgumentMatchers.anyString(),ArgumentMatchers.anyInt()))
                        .thenReturn(ResponseEntity.status(HttpStatus.OK).body("success"));

        Mockito.when(paymentService.doPayment(Mockito.any(PaymentDto.class)))
                        .thenReturn(ResponseEntity.status(HttpStatus.OK).body("success"));

        OrderPlacedEvent event = getMockOrderEvent();
        Mockito.when(kafkaTemplate.send("order-place", event))
                .thenReturn(new CompletableFuture<SendResult<String, OrderPlacedEvent>>());

        Order order = getMockOrder();
        orderRepo.save(order);


        //METHOD
        OrderDto orderDto = getMockOrderDTO();
        ResponseEntity<String> placeOrderResponse  =  orderService.placeOrder(orderDto);

       //VERIFICATION
       Mockito.verify(productService, Mockito.times(1)).reduceProductByOrder(ArgumentMatchers.anyString(),ArgumentMatchers.anyInt());
       Mockito.verify(paymentService, Mockito.times(1)).doPayment(Mockito.any(PaymentDto.class));
       //Mockito.verify(kafkaTemplate, Mockito.times(1)).send("order-place", event);
       Mockito.verify(orderRepo, Mockito.times(1));

       //ASSERT
       Assertions.assertEquals(orderDto.product_id(), order.getProduct_id());


    }


    @DisplayName("Place Order Fail Case")
    @Test
    void fail_order_case(){
        //MOCK
        Mockito.when(productService.reduceProductByOrder(ArgumentMatchers.anyString(),ArgumentMatchers.anyInt()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body("success"));

        Mockito.when(paymentService.doPayment(Mockito.any(PaymentDto.class)))
                .thenThrow(new OrderExceptionHandler());

        OrderPlacedEvent event = getMockOrderEvent();
        Mockito.when(kafkaTemplate.send("order-place", event))
                .thenReturn(new CompletableFuture<SendResult<String, OrderPlacedEvent>>());

        Order order = getMockOrder();
        orderRepo.save(order);


        //METHOD
        OrderDto orderDto = getMockOrderDTO();
        OrderExceptionHandler exceptionHandler = Assertions.assertThrows(OrderExceptionHandler.class,
                ()->orderService.placeOrder(orderDto));


        //VERIFICATION
        Mockito.verify(productService, Mockito.times(1)).reduceProductByOrder(ArgumentMatchers.anyString(),ArgumentMatchers.anyInt());
        Mockito.verify(paymentService, Mockito.times(1)).doPayment(Mockito.any(PaymentDto.class));
        Mockito.verify(kafkaTemplate, Mockito.times(0)).send("order-place", event);
        Mockito.verify(orderRepo, Mockito.times(0));

        //ASSERT
        Assertions.assertEquals(orderDto.product_id(), order.getProduct_id());


   }





    //HELPER
    private PaymentDto getMockPaymentDto() {
        PaymentDto paymentDto = new PaymentDto(
                199, "mwp188", Instant.now(), OrderMode.ON_DELIVERY, Status.ACCEPTED, null, 1);
        return paymentDto;
    }

    private ProductDto getMockProductDto() {
        ProductDto productDto = new ProductDto("#1212", "pixel-8", 199, 1);
        return productDto;
    }

    private Order getMockOrder() {
        return Order.builder()
                .order_mode(OrderMode.ON_DELIVERY)
                .order_date(Instant.now())
                .order_status(OrderStatus.PLACED)
                .amount(199)
                .id("mwp188")
                .quantity(1)
                .product_id("#1212")
                .build();
    }



    private OrderDto getMockOrderDTO() {
        OrderDto orderDto = new OrderDto(
                "1778","#1212",1, OrderStatus.PLACED, OrderMode.ON_DELIVERY, 199, Instant.now(), Mockito.any(ProductDto.class), Mockito.any(PaymentDto.class) );
        return orderDto;

    }


    private OrderPlacedEvent getMockOrderEvent() {
        OrderPlacedEvent event = new OrderPlacedEvent();
        event.setOrderNumber("#1227");
        event.setSubject("Order Placed");
        event.setUser("abc@gmail.com");
        return event;
    }



}