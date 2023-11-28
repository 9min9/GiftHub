package com.gifthub.product.repository;

import com.gifthub.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> , ProductRepositorySupport {
    Optional<Product> findProductByName(String productName);

}
