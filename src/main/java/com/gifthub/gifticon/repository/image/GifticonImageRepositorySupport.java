package com.gifthub.gifticon.repository.image;

import com.gifthub.gifticon.dto.GifticonImageDto;

import java.util.Optional;

public interface GifticonImageRepositorySupport {
    Optional<GifticonImageDto> findGifticonImageByGifticonStorageId(Long storageId);
}
