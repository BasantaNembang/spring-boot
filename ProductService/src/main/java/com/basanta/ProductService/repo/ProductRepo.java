package com.basanta.ProductService.repo;

import com.basanta.ProductService.entity.Product;
import io.micrometer.observation.annotation.Observed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
@Observed
public interface ProductRepo extends JpaRepository<Product, String> {
}
