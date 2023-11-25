package com.gifthub.gifticon.service;

import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.dto.GifticonImageDto;
import com.gifthub.gifticon.entity.GifticonStorage;
import com.gifthub.gifticon.repository.GifticonStorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GifticonStorageService {

    private final GifticonStorageRepository gifticonStorageRepository;

    public Long saveStorage(GifticonDto gifticonDto, GifticonImageDto imageDto) {

        GifticonStorage saveStorage = gifticonStorageRepository.save(gifticonDto.toStorageEntity(imageDto));
        return saveStorage.getId();
    }

    public Long saveTempStorage(GifticonDto gifticonDto, GifticonImageDto imageDto){
//        GifticonStorage tempStorage = GifticonStorage.builder()
//                .productName(gifticonDto.getProductName())
//                .gifticonImage(imageDto.toEntity())
//                .
        GifticonStorage tempStorage = gifticonStorageRepository.save(gifticonDto.toStorageEntity(imageDto));
        return tempStorage.getId();
    }

}
