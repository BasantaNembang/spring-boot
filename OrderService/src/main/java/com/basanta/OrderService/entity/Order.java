package com.basanta.OrderService.entity;

import com.basanta.OrderService.model.OrderMode;
import com.basanta.OrderService.model.OrderStatus;
import jakarta.persistence.*;
import lombok.*;


import java.time.Instant;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "orders_table")
public class Order {

    @Id
    @Column(name = "id")
    private String id;

    private String  product_id;
    private int quantity;

    @Enumerated(EnumType.STRING)
    private OrderStatus order_status;

    @Enumerated(EnumType.STRING)
    private OrderMode order_mode;

    private int amount;
    private Instant order_date;

}
