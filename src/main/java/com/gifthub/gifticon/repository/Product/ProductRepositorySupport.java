package com.gifthub.gifticon.repository.Product;

import com.gifthub.gifticon.dto.ProductDto;
import java.util.List;

public interface ProductRepositorySupport {

    List<String> findAllBrandName();
    List<ProductDto> findProductByBrand(String brand);
    List<ProductDto> findAllProduct();


}
