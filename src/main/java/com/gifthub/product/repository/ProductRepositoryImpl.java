package com.gifthub.product.repository;


import com.gifthub.gifticon.entity.QGifticon;
import com.gifthub.product.dto.ProductDto;
import com.gifthub.product.dto.ProductEngCategoryDto;
import com.gifthub.product.dto.QProductDto;
import com.gifthub.product.dto.QProductEngCategoryDto;
import com.gifthub.product.enumeration.CategoryName;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.gifthub.product.entity.QProduct.product;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositorySupport {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> findAllCategory() {
        return queryFactory
                .select(product.category).distinct()
                .from(product).fetch();
    }

    @Override
    public List<String> loadBrandNames() {
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
    public Page<ProductEngCategoryDto> findProductByBrand(Pageable pageable, String brand) {
        List<ProductEngCategoryDto> fetch = queryFactory
                .select(new QProductEngCategoryDto(product.id, product.name, product.price, product.brandName, product.category))
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
    public Page<ProductEngCategoryDto> findAllProduct(Pageable pageable) {
        List<ProductEngCategoryDto> fetch = queryFactory
                .select(new QProductEngCategoryDto(product.id, product.name, product.price, product.brandName, product.category))
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

    @Override
    public Page<ProductEngCategoryDto> findAllProductByName(Pageable pageable, String name) {
        List<ProductEngCategoryDto> fetch = queryFactory
                .select(new QProductEngCategoryDto(product.id, product.name, product.price, product.brandName, product.category))
                .from(product)
                .where(product.name.contains(name))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(product.count())
                .from(product)
                .where(product.name.contains(name))
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

    @Override
    public Page<ProductEngCategoryDto> findProductByCategoryByName(Pageable pageable, String category, String name) {
        List<ProductEngCategoryDto> fetch = queryFactory.select(new QProductEngCategoryDto(product.id, product.name, product.price, product.brandName, product.category))
                .from(product)
                .where(product.category.eq(category), product.name.contains(name))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(product.count())
                .from(product)
                .where(product.category.eq(category), product.name.contains(name))
                .fetchOne();

        return new PageImpl<>(fetch, pageable, count);
    }

    @Override
    public Page<ProductEngCategoryDto> findProductByBrandByName(Pageable pageable, String category, String brand, String name) {
        List<ProductEngCategoryDto> fetch = queryFactory
                .select(new QProductEngCategoryDto(product.id, product.name, product.price, product.brandName, product.category))
                .from(product)
                .where(product.category.eq(category), product.brandName.eq(brand), product.name.contains(name))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(product.count())
                .from(product)
                .where(product.category.eq(category), product.brandName.eq(brand), product.name.contains(name))
                .fetchOne();

        return new PageImpl<>(fetch, pageable, count);
    }

    @Override
    public ProductDto findProductByGifticonId(Long gifticonId) {
        return queryFactory.select(new QProductDto(product.id, product.name, product.price, product.brandName, product.category))
                .from(QGifticon.gifticon)
                .where(QGifticon.gifticon.id.eq(gifticonId))
                .fetchOne();
    }

}
