package com.basanta.PaymentService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;

import java.time.Instant;


@Builder
public record PaymentDto(int  amount,
        String  OrderId,
        Instant payment_date,
        OrderMode orderMode_mode,
        Status status,
        String reference_number,
       // @JsonIgnore
        int quantity) {
}
