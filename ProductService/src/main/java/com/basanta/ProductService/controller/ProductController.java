package com.basanta.ProductService.controller;


import com.basanta.ProductService.model.ProductDto;
import com.basanta.ProductService.service.ProductSerImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductSerImp service;

    //add product
    @PreAuthorize("hasAnyRole('Admin')")
    @PostMapping("/add")
    public ProductDto addProduct(@RequestBody ProductDto productDto){
        return ResponseEntity.status(HttpStatus.OK).body(service.addProduct(productDto)).getBody();
    }

    //get product by id
    @PreAuthorize("hasAnyRole('Admin', 'Customer')")
    @GetMapping("/get/{id}")
    public ProductDto getProductById(@PathVariable("id") String id){
        return ResponseEntity.status(HttpStatus.OK).body(service.getProductById(id)).getBody();
    }

    //get all product
    @PreAuthorize("hasAnyRole('Admin', 'Customer')")
    @GetMapping("/get")
    public List<ProductDto> getAllProduct(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllProduct()).getBody();
    }



    @PreAuthorize("hasAnyRole('Admin', 'Customer')")
    @PutMapping("/reduce-product/{id}")
    ResponseEntity<String> reduceProductByOrder(@PathVariable("id") String id,
                                                @RequestParam int quantity){
        return ResponseEntity.status(HttpStatus.OK).body(service.reduceProudct(id,quantity)).getBody();
    }


    //add product if order is canceled
    @PreAuthorize("hasAnyRole('Admin', 'Customer')")
    @PutMapping("/add-product-quant/{id}")
    public ResponseEntity<HttpStatus> addProductIfOrderCancel(@PathVariable("id") String id,
                                                              @RequestParam int quantity){
        return service.addQuantityInProduct(id, quantity);
    }


}
