package com.basanta.ProductService.service;


import com.basanta.ProductService.entity.Product;
import com.basanta.ProductService.error.ResourseNotFound;
import com.basanta.ProductService.model.ProductDto;
import com.basanta.ProductService.repo.ProductRepo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ProductSerImp implements ProuductService {

    @Autowired
    private ProductRepo productRepo;

    @Override
    public ProductDto addProduct(ProductDto productDto) {
          Product product =  Product.builder()
                 .id(UUID.randomUUID().toString().substring(0,5))
                 .name(productDto.name())
                 .price(productDto.price())
                 .quantity(productDto.quantity()).build();
          productRepo.save(product);
          return productDto;
    }

    @Override
    public ProductDto getProductById(String id) {
       Product product =  productRepo.findById(id).orElseThrow(()->new ResourseNotFound());
       ProductDto dto = new ProductDto(product.getId(), product.getName(), product.getPrice(), product.getQuantity());
       return dto;
    }

    @Override
    public List<ProductDto> getAllProduct() {
         List<Product> products =  productRepo.findAll();
         return products.stream()
                 .map(product->new ProductDto(product.getId(), product.getName(), product.getPrice(), product.getQuantity()))
                 .toList();
    }

    @Override
    @Transactional
    public ResponseEntity<String> reduceProudct(String id, int quantity) {
        Product product = productRepo.findById(id).orElseThrow(()->new ResourseNotFound("No product avaiable"));
        if(product.getQuantity()<quantity){
            throw new ResourseNotFound("Out of Stock");
        }
        product.setQuantity(product.getQuantity()-quantity);
        log.info(" info {}", product);
        productRepo.save(product);
        return ResponseEntity.status(HttpStatus.OK).body("Product reduce successfully");
    }


    @Override
    @Transactional
    public ResponseEntity<HttpStatus> addQuantityInProduct(String id, int quantity) {

        Product product =  productRepo.findById(id).orElseThrow(()-> new ResourseNotFound("No product avaiable"));
        product.setQuantity(product.getQuantity()+quantity);
        productRepo.save(product);
        return ResponseEntity.ok(HttpStatus.OK);
    }


}
