package com.gifthub.gifticon.repository.storage;

import com.gifthub.admin.dto.StorageAdminListDto;
import com.gifthub.gifticon.dto.GifticonStorageListDto;
import com.gifthub.gifticon.entity.GifticonStorage;
import com.gifthub.gifticon.enumeration.StorageStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface GifticonStorageRepositorySupport {
    Page<GifticonStorageListDto> findGifticonStorageDtoByUserId(Long userId, Pageable pageable);
    Page<GifticonStorage> findStorageByUserId(Long userId, Pageable pageable);
    Long getCountByAdminApproval();

    Page<StorageAdminListDto> findStorageByStorageStatus(StorageStatus status, Pageable pageable);


}
