package com.gifthub.gifticon.service;

import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.dto.GifticonImageDto;
import com.gifthub.gifticon.entity.GifticonStorage;
import com.gifthub.gifticon.repository.GifticonStorageRepository;
import com.gifthub.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GifticonStorageService {

    private final GifticonStorageRepository gifticonStorageRepository;

    public GifticonStorage saveTempStorage(GifticonDto gifticonDto, GifticonImageDto imageDto) {

        return gifticonStorageRepository.save(gifticonDto.toStorageEntity(imageDto));
    }

    public List<GifticonStorage> getTempStorageList(User user){
        return gifticonStorageRepository.findGifticonStorageByUser(user);
    }


}
