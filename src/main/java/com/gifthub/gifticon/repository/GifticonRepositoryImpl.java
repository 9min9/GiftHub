package com.gifthub.gifticon.repository;

import com.gifthub.gifticon.dto.GifticonMessageDto;
import com.gifthub.gifticon.dto.QGifticonMessageDto;
import com.gifthub.gifticon.entity.Gifticon;
import com.gifthub.gifticon.entity.QGifticon;
import com.gifthub.gifticon.enumeration.GifticonStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.gifthub.gifticon.entity.QGifticon.gifticon;
import static com.gifthub.product.entity.QProduct.product;

@Repository
@RequiredArgsConstructor
public class GifticonRepositoryImpl implements GifticonRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Gifticon> findByGifticonStatusIsOnSale(Pageable pageable, String type) {
        type = type.replaceAll("-", "/");

        List<Gifticon> gifticons = jpaQueryFactory.select(gifticon)
                .from(gifticon)
                .innerJoin(gifticon.product, product)
                .where(gifticon.gifticonStatus.eq(GifticonStatus.ONSALE), product.category.eq(type))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(gifticon.modifiedDate.desc())
                .fetch();

        Long gifticonCount = jpaQueryFactory.select(gifticon.count())
                .from(gifticon)
                .where(gifticon.gifticonStatus.eq(GifticonStatus.ONSALE))
                .fetchOne();

        return new PageImpl<>(gifticons, pageable, gifticonCount);
    }

    @Override
    public List<String> findBrandNameByCategory(String category) {
        return jpaQueryFactory.select(product.brandName).distinct()
                .from(product)
                .where(product.category.eq(category))
                .limit(5)
                .fetch();
    }


    @Override
    public Page<Gifticon> findByUserId(Pageable pageable, Long userId) {
        List<Gifticon> gifticons = jpaQueryFactory.select(gifticon)
                .from(gifticon)
                .where(gifticon.user.id.eq(userId))
                .orderBy(gifticon.createDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory.select(gifticon.count())
                .from(gifticon)
                .where(gifticon.user.id.eq(userId))
                .fetchOne();

        return new PageImpl<>(gifticons, pageable, count);
    }

    @Override
    public Long updateSaleByGifticonId(Long gifticonId) {
        long execute = jpaQueryFactory.update(gifticon)
                .set(gifticon.gifticonStatus, GifticonStatus.ONSALE)
                .where(gifticon.id.eq(gifticonId))
                .execute();

        return execute;
    }

    @Override
    public Long updateNoneByGifticonId(Long gifticonId) {
        long execute = jpaQueryFactory
                .update(gifticon)
                .set(gifticon.gifticonStatus, GifticonStatus.NONE)
                .where(gifticon.id.eq(gifticonId))
                .execute();
        return execute;
    }

    @Override
    public Long updateFinishedByGifticonId(Long gifticonId) {
        long excute = jpaQueryFactory
                .update(gifticon)
                .set(gifticon.gifticonStatus, GifticonStatus.FINISHED)
                .where(gifticon.id.eq(gifticonId))
                .execute();
        return excute;
    }

    @Override
    public GifticonMessageDto findGifticonMessageDtoByGifticonId(Long gifticonId) {
        return jpaQueryFactory.select(new QGifticonMessageDto(gifticon.product.name, gifticon.brandName, gifticon.barcode, gifticon.due))
                .from(gifticon)
                .where(gifticon.id.eq(gifticonId))
                .fetchOne();
    }

    @Override
    public Page<Gifticon> findGifticonByProductIdOrderByProductPrice(Pageable pageable, Long productId) {
        List<Gifticon> gifticons = jpaQueryFactory.select(gifticon)
                .from(gifticon)
                .where(gifticon.product.id.eq(productId), gifticon.gifticonStatus.eq(GifticonStatus.ONSALE))
                .orderBy(gifticon.product.price.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory.select(gifticon.count())
                .from(gifticon)
                .where(gifticon.product.id.eq(productId))
                .fetchOne();

        return new PageImpl<>(gifticons, pageable, count);
    }

}
