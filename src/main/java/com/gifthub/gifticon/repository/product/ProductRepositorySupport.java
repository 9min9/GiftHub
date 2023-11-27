package com.gifthub.gifticon.repository.product;

import com.gifthub.gifticon.dto.ProductDto;
import java.util.List;

public interface ProductRepositorySupport {

    List<String> findAllBrandName();
    List<ProductDto> findProductByBrand(String brand);
    List<ProductDto> findAllProduct();


}
