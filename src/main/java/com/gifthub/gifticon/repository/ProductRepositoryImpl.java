package com.gifthub.gifticon.repository;


import static com.gifthub.gifticon.entity.QProduct.product;

import com.gifthub.gifticon.dto.ProductDto;
import com.gifthub.gifticon.dto.QProductDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositorySupport {

private final JPAQueryFactory queryFactory;

public List<String> findAllBrandName(){
    return queryFactory
            .select(product.brandName)
            .from(product)
            .fetch();
}

@Override
public List<ProductDto> findProductByBrand(String brand) {
    return queryFactory
            .select(new QProductDto(product.id, product.name, product.price, product.brandName))
            .from(product)
            .where(product.brandName.eq(brand))
            .fetch();
}

@Override
public List<ProductDto> findAllProduct() {
    return queryFactory
            .select(new QProductDto(product.id, product.brandName, product.price, product.name))
            .from(product)
            .fetch();
}

private BooleanExpression test(Long id) {
    return product.id.eq(id);
}


}
