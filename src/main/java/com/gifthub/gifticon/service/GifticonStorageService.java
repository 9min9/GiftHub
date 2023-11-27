package com.gifthub.gifticon.service;

import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.dto.GifticonImageDto;
import com.gifthub.gifticon.dto.GifticonStorageListDto;
import com.gifthub.gifticon.entity.GifticonStorage;
import com.gifthub.gifticon.repository.storage.GifticonStorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GifticonStorageService {

    private final GifticonStorageRepository gifticonStorageRepository;

    public GifticonStorage saveStorage(GifticonDto gifticonDto, GifticonImageDto imageDto) {
        return gifticonStorageRepository.save(gifticonDto.toStorageEntity(imageDto));
    }

    public Page<GifticonStorageListDto> getStorageList(Long userId, Pageable pageable){
        return gifticonStorageRepository.findGifticonStorageDtoByUserId(userId, pageable);
    }
    public Page<GifticonStorage> getStorageListTest (Long userId, Pageable pageable){
        return gifticonStorageRepository.findStorageByUserId(userId, pageable);
    }

    public GifticonStorage getStorageById(Long storageId){
        GifticonStorage gifticonStorage = gifticonStorageRepository.findById(storageId).orElse(null);
        return (gifticonStorage != null) ? gifticonStorage : null;
    }

    // 삭제하기
    public void removeFromStorageTable(Long storageId){


    }


}
