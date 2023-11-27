package com.gifthub.gifticon.repository.product;

import com.gifthub.gifticon.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> , ProductRepositorySupport {
    Optional<Product> findProductByName(String productName);

}
