package com.gifthub.gifticon.repository.storage;

import com.gifthub.gifticon.dto.GifticonStorageListDto;
import com.gifthub.gifticon.entity.GifticonStorage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface GifticonStorageRepositorySupport {
    Page<GifticonStorageListDto> findGifticonStorageDtoByUserId(Long userId, Pageable pageable);

    Page<GifticonStorage> findStorageByUserId(Long userId, Pageable pageable);

}
