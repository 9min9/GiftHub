package com.gifthub.gifticon.service;

import com.gifthub.gifticon.dto.ProductDto;
import com.gifthub.gifticon.entity.Product;
import com.gifthub.gifticon.repository.ProductRepository;
import com.gifthub.gifticon.repository.ProductRepositoryImpl;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductRepositoryImpl productRepositoryQdsl;
    private final EntityManager em;

    public Long saveProduct(ProductDto productDto) {
        Product product = productRepository.save(productDto.toProductEntity());
        return product.getId();
    }

    public Long saveAll(List<ProductDto> productDtoList) { // 엑셀파일 받아서 한번에 추가

        List<Product> productList = productDtoList.stream()
                .map(ProductDto::toProductEntity)
                .collect(Collectors.toList());


        List<Product> savedProducts = productRepository.saveAll(productList);

        // 총 넣은 productDto 개수를 return

        return (long) savedProducts.size();
    }

    public List<ProductDto> getAllProduct() {
        return productRepositoryQdsl.findAllProduct();
    }

}
