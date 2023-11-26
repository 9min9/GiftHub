package com.gifthub.gifticon.repository;

import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.entity.Gifticon;
import com.gifthub.gifticon.entity.QGifticon;
import com.gifthub.gifticon.entity.QProduct;
import com.gifthub.gifticon.enumeration.GifticonStatus;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.gifthub.gifticon.entity.QGifticon.*;
import static com.gifthub.gifticon.entity.QProduct.*;

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

}
