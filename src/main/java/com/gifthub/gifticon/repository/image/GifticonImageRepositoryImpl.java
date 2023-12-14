package com.gifthub.gifticon.repository.image;

import com.gifthub.gifticon.dto.GifticonImageDto;
import com.gifthub.gifticon.dto.QGifticonImageDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.gifthub.gifticon.entity.QGifticonImage.gifticonImage;

@Repository
@RequiredArgsConstructor
public class GifticonImageRepositoryImpl implements GifticonImageRepositorySupport{

    private final JPAQueryFactory queryFactory;
    @Override
    public Optional<GifticonImageDto> findGifticonImageByGifticonStorageId(Long storageId) {
        GifticonImageDto result = queryFactory.select(new QGifticonImageDto(gifticonImage.id, gifticonImage.gifticonStorage, gifticonImage.accessUrl, gifticonImage.originalFileName, gifticonImage.storeFileName))
                .from(gifticonImage)
                .where(gifticonImage.gifticonStorage.id.eq(storageId))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
