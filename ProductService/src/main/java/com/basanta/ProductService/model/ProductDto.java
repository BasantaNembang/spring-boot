package com.basanta.ProductService.model;

public record ProductDto(
        String id,
        String name,
        int price,
        int quantity) {
}
