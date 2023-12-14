package com.gifthub.gifticon.repository.image;

import com.gifthub.gifticon.entity.GifticonImage;
import com.gifthub.gifticon.entity.GifticonStorage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GifticonImageRepository extends JpaRepository<GifticonImage, Long>, GifticonImageRepositorySupport {
    Optional<GifticonImage> findGifticonImageByGifticonStorage(GifticonStorage gifticonStorage);


}
