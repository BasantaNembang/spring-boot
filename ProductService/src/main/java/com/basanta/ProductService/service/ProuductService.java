package com.basanta.ProductService.service;

import com.basanta.ProductService.model.ProductDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProuductService {

    ProductDto addProduct(ProductDto productDto);

    ProductDto getProductById(String id);

    List<ProductDto> getAllProduct();

    ResponseEntity<String> reduceProudct(String id, int quantity);

    ResponseEntity<HttpStatus> addQuantityInProduct(String id, int quantity);
}
