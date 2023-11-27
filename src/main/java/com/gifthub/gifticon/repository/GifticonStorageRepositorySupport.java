package com.gifthub.gifticon.repository;

import com.gifthub.gifticon.dto.GifticonStorageListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface GifticonStorageRepositorySupport {
    Page<GifticonStorageListDto> findGifticonStorageDtoByUserId(Long userId, Pageable pageable);

}
