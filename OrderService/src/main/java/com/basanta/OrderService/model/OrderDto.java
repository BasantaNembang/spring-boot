package com.basanta.OrderService.model;

import com.basanta.OrderService.externalStuff.PaymentDto;
import com.basanta.OrderService.externalStuff.ProductDto;
import lombok.Builder;

import java.time.Instant;

@Builder
public record OrderDto(String id,
         String  product_id,
         int quantity,
         OrderStatus order_status,
         OrderMode order_mode,
         int amount,
         Instant order_date,
         ProductDto productDto,
         PaymentDto paymentDto
) {
}
