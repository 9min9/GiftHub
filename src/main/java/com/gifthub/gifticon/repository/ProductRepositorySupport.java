package com.gifthub.gifticon.repository;

import com.gifthub.gifticon.dto.ProductDto;
import java.util.List;

public interface ProductRepositorySupport {

    List<ProductDto> findProductByBrand(String brand);
    List<ProductDto> findAllProduct();


}
