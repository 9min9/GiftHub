package com.gifthub.product.repository;


import static com.gifthub.product.entity.QProduct.product;

import com.gifthub.product.dto.ProductDto;
import com.gifthub.product.dto.QProductDto;
import com.gifthub.product.enumeration.CategoryName;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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

    @Override
    public Page<ProductDto> findProductByBrand(Pageable pageable, String brand) {
        List<ProductDto> fetch = queryFactory
                .select(new QProductDto(product.id, product.name, product.price, product.brandName, product.category))
                .from(product)
                .where(product.brandName.eq(brand))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(product.count())
                .from(product)
                .where(product.brandName.eq(brand))
                .fetchOne();

        return new PageImpl<>(fetch, pageable, count);
    }

    @Override
    public Page<ProductDto> findAllProduct(Pageable pageable) {
        List<ProductDto> fetch = queryFactory
                .select(new QProductDto(product.id, product.brandName, product.price, product.name, product.category))
                .from(product)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(product.count())
                .from(product)
                .fetchOne();

        return new PageImpl<>(fetch, pageable, count);
    }

    private BooleanExpression test(Long id) {
        return product.id.eq(id);
    }


    @Override
    public List<String> findBrandNameByCategory(CategoryName categoryName) {
        return queryFactory.select(product.brandName).distinct()
                .from(product)
                .where(product.category.eq(categoryName.getKorName()))
                .limit(5)
                .fetch();
    }

}
