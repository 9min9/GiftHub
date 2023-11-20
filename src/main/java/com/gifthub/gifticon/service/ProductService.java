package com.gifthub.gifticon.service;

import com.gifthub.gifticon.dto.ProductDto;
import com.gifthub.gifticon.entity.Product;
import com.gifthub.gifticon.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Long saveProduct(ProductDto productDto) {
        Product product = productRepository.save(productDto.toProductEntity());
        return product.getId();
    }

    public Long saveAll(List<ProductDto> productDtoList) {

        List<Product> productList = productDtoList.stream()
                .map(ProductDto::toProductEntity)
                .collect(Collectors.toList());


        List<Product> savedProducts = productRepository.saveAll(productList);

        // 총 넣은 productDto 개수를 return

        return (long) savedProducts.size();
    }


}
