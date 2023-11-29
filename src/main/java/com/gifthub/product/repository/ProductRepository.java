package com.gifthub.product.repository;

import com.gifthub.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> , ProductRepositorySupport {
    Optional<Product> findProductByName(String productName);

    List<Product> findProductByCategory(String category);

    Page<Product> findProductByCategory(Pageable pageable, String category);
}
