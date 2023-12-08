package com.gifthub.product.repository;

import com.gifthub.product.dto.ProductDto;
import com.gifthub.product.entity.Product;
import com.gifthub.product.enumeration.CategoryName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;

import java.util.List;

public interface ProductRepositorySupport {

    List<String> findAllCategory();

    List<String> findAllBrandName();
    List<ProductDto> findProductByBrand(String brand);
    List<ProductDto> findAllProduct();

    Page<ProductDto> findProductByBrand(Pageable pageable, String brand);

    Page<ProductDto> findAllProduct(Pageable pageable);

    Page<ProductDto> findAllProductByName(Pageable pageable, String name);

    List<String> findBrandNameByCategory(CategoryName category);

    Page<ProductDto> findProductByCategoryByName(Pageable pageable, String category, String name);

    Page<ProductDto> findProductByBrandByName(Pageable pageable, String brand, String name);
}
