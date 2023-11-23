package com.gifthub.gifticon.service;

import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.dto.GifticonImageDto;
import com.gifthub.gifticon.entity.GifticonTempStorage;
import com.gifthub.gifticon.repository.GifticonTempStorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GifticonTempStorageService {

    private final GifticonTempStorageRepository gifticonTempStorageRepository;


    public GifticonDto saveStorage(GifticonDto gifticonDto, GifticonImageDto imageDto) {

        GifticonTempStorage saveStorage = gifticonTempStorageRepository.save(gifticonDto.toStorageEntity(imageDto));
        // 임시 저장소에 기프티콘, 임시 기프티콘을 각각 저장
        return saveStorage.toGifticonDto();

    }




}
