package com.gifthub.product.repository;

import com.gifthub.product.dto.ProductDto;
import com.gifthub.product.enumeration.CategoryName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepositorySupport {

    List<String> findAllBrandName();
    List<ProductDto> findProductByBrand(String brand);
    List<ProductDto> findAllProduct();

    Page<ProductDto> findProductByBrand(Pageable pageable, String brand);

    Page<ProductDto> findAllProduct(Pageable pageable);

    List<String> findBrandNameByCategory(CategoryName category);

}
