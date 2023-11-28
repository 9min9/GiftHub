package com.gifthub.product.repository;


import static com.gifthub.product.entity.QProduct.product;

import com.gifthub.product.dto.ProductDto;
import com.gifthub.product.dto.QProductDto;
import com.gifthub.product.entity.QProduct;
import com.gifthub.product.enumeration.ProductName;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositorySupport {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> findAllBrandName() {
        return queryFactory
                .select(product.brandName)
                .distinct()
                .from(product)
                .fetch();
    }


    @Override
    public List<ProductDto> findProductByBrand(String brand) {
        return queryFactory
                .select(new QProductDto(product.id, product.name, product.price, product.brandName, product.category))
                .from(product)
                .where(product.brandName.eq(brand))
                .fetch();
    }

    @Override
    public List<ProductDto> findAllProduct() {
        return queryFactory
                .select(new QProductDto(product.id, product.brandName, product.price, product.name, product.category))
                .from(product)
                .fetch();
    }

    private BooleanExpression test(Long id) {
        return product.id.eq(id);
    }


    @Override
    public List<String> findBrandNameByCategory(ProductName productName) {
        return queryFactory.select(product.brandName).distinct()
                .from(product)
                .where(product.category.eq(productName.getKorName()))
                .limit(5)
                .fetch();
    }

}
