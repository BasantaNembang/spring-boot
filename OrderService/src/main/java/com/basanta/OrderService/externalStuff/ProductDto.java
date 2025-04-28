package com.basanta.OrderService.externalStuff;


public record ProductDto(
        String id,
        String name,
        int price,
        int quantity) {
}
