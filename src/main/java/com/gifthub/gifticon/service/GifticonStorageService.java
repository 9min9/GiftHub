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


    public GifticonStorage saveTempStorage(GifticonDto gifticonDto, GifticonImageDto imageDto){

        GifticonStorage storage = new GifticonStorage();
        storage.setDue(gifticonDto.getDue());
        storage.setGifticonImage(imageDto.toEntity());
        storage.setBrandName(gifticonDto.getBrandName());
        storage.setProductName(gifticonDto.getProductName());

        GifticonStorage tempStorage = gifticonStorageRepository.save(storage);
        return tempStorage;
    }

}
