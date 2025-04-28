package com.basanta.PaymentService.entity;


import com.basanta.PaymentService.model.OrderMode;
import com.basanta.PaymentService.model.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
public class PaymentDetails {

     @Id
     private String id;

     private  int  amount;

     @Column(name = "order_id")
     private String  OrderId;

     private Instant payment_date;

     @Enumerated(EnumType.STRING)
     private OrderMode order_mode;

     @Enumerated(EnumType.STRING)
     private Status status;

     private String reference_number;

}
