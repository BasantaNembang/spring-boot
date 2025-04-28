package com.basanta.OrderService.externalStuff;


import com.basanta.OrderService.model.OrderMode;

import java.time.Instant;


public record PaymentDto(int  amount,
                         String  OrderId,
                         Instant payment_date,
                         OrderMode orderMode_mode,
                         Status status,
                         String reference_number,
                         int quantity

) {


}
