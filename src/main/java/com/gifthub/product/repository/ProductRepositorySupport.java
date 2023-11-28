package com.gifthub.product.repository;

import com.gifthub.product.dto.ProductDto;
import com.gifthub.product.enumeration.ProductName;

import java.util.List;

public interface ProductRepositorySupport {

    List<String> findAllBrandName();
    List<ProductDto> findProductByBrand(String brand);
    List<ProductDto> findAllProduct();

    List<String> findBrandNameByCategory(ProductName category);

}
