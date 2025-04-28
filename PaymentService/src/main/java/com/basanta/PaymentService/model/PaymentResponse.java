package com.basanta.PaymentService.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.Instant;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {

    private String id;
    private  int  amount;
    private String  order_id;
    private Instant payment_date;
    private OrderMode order_mode;
    private Status status;
    private String reference_number;


}
