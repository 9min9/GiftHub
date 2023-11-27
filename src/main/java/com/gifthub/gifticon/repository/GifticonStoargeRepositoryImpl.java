package com.gifthub.gifticon.repository;

import com.gifthub.gifticon.dto.GifticonStorageListDto;
import com.gifthub.gifticon.dto.QGifticonStorageListDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.gifthub.gifticon.entity.QGifticonImage.gifticonImage;
import static com.gifthub.gifticon.entity.QGifticonStorage.*;
import static com.gifthub.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class GifticonStoargeRepositoryImpl implements GifticonStorageRepositorySupport {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<GifticonStorageListDto> findGifticonStorageDtoByUserId(Long userId, Pageable pageable) {
        List<GifticonStorageListDto> content = getGifticonStoargeList(userId, pageable);
        Long count = getGifticonStorageListCount(userId);

        return new PageImpl<>(content, pageable, count);
    }

    private List<GifticonStorageListDto> getGifticonStoargeList(Long userId, Pageable pageable) {
        return queryFactory
                .select(new QGifticonStorageListDto(
                        gifticonStorage.id,
                        gifticonStorage.brandName,
                        gifticonStorage.productName,
                        gifticonStorage.barcode,
                        gifticonStorage.due,
                        gifticonStorage.gifticonImage.accessUrl))
                .from(gifticonStorage)
                .where(gifticonStorage.user.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .leftJoin(gifticonStorage.user, user)
                .leftJoin(gifticonStorage.gifticonImage, gifticonImage)
                .fetchJoin().fetch();
    }

    private Long getGifticonStorageListCount(Long userId) {
        return queryFactory
                .select(gifticonStorage.count())
                .from(gifticonStorage)
                .where(gifticonStorage.user.id.eq(userId))
                .fetchOne();
    }
}
