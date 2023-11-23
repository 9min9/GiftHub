package com.gifthub.gifticon.repository;

import com.gifthub.gifticon.entity.Product;
import com.gifthub.gifticon.repository.ProductRepositorySupport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> , ProductRepositorySupport {

}
