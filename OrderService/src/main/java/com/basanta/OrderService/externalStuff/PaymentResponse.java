package com.basanta.OrderService.externalStuff;

import com.basanta.OrderService.model.OrderMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentResponse {

    private  int  amount;
    private String  OrderId;
    private Instant payment_date;
    private OrderMode orderMode_mode;
    private Status status;
    private String reference_number;
    private  int  quantity;


}
