package com.gifthub.product.repository;

import com.gifthub.product.dto.ProductDto;
import com.gifthub.product.dto.ProductEngCategoryDto;
import com.gifthub.product.enumeration.CategoryName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepositorySupport {

    List<String> findAllCategory();

    List<String> findAllBrandName();
    List<ProductDto> findProductByBrand(String brand);
    List<ProductDto> findAllProduct();

    Page<ProductEngCategoryDto> findProductByBrand(Pageable pageable, String brand);

    Page<ProductEngCategoryDto> findAllProduct(Pageable pageable);

    Page<ProductEngCategoryDto> findAllProductByName(Pageable pageable, String name);

    List<String> findBrandNameByCategory(CategoryName category);

    Page<ProductEngCategoryDto> findProductByCategoryByName(Pageable pageable, String category, String name);

    Page<ProductEngCategoryDto> findProductByBrandByName(Pageable pageable, String category, String brand, String name);

    ProductDto findProductByGifticonId(Long gifticonId);
}
