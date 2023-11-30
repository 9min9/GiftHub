package com.gifthub.gifticon.service;

import com.gifthub.admin.dto.StorageAdminListDto;
import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.dto.GifticonImageDto;
import com.gifthub.gifticon.dto.GifticonStorageListDto;
import com.gifthub.gifticon.dto.storage.GifticonStorageDto;
import com.gifthub.gifticon.entity.GifticonStorage;
import com.gifthub.gifticon.enumeration.StorageStatus;
import com.gifthub.gifticon.repository.storage.GifticonStorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.gifthub.gifticon.enumeration.StorageStatus.*;

@Service
@RequiredArgsConstructor
public class GifticonStorageService {

    private final GifticonStorageRepository gifticonStorageRepository;

    public GifticonStorage saveStorage(GifticonDto gifticonDto, GifticonImageDto imageDto) {
        GifticonStorage storageEntity = gifticonDto.toStorageEntity(imageDto);
        storageEntity.changeStatus(WAIT_REGISTRATION);
        return gifticonStorageRepository.save(storageEntity);
    }

    public void storageToAdmin(GifticonStorageDto storageDto) {
        GifticonStorage gifticonStorage = gifticonStorageRepository.findById(storageDto.getId()).orElse(null);

        if(gifticonStorage != null) {
            gifticonStorage.changeProductName(storageDto.getProductName());
            gifticonStorage.changeBrandName(storageDto.getBrandName());
            gifticonStorage.changeBarcode(storageDto.getBarcode());
            gifticonStorage.changeDue(storageDto.getDue());
            gifticonStorage.changeStatus(ADMIN_APPROVAL);

            gifticonStorageRepository.save(gifticonStorage);
        }
    }

    public void adminToStorage(GifticonStorageDto storageDto) {
        GifticonStorage findStorage =
                gifticonStorageRepository.findById(storageDto.getId()).orElse(null);

        if (findStorage != null) {
            findStorage.changeStatus(WAIT_REGISTRATION);
            gifticonStorageRepository.save(findStorage);
        }
    }

    public Page<GifticonStorageListDto> getStorageList(Long userId, Pageable pageable){
        return gifticonStorageRepository.findGifticonStorageDtoByUserId(userId, pageable);
    }
    public Page<GifticonStorage> getStorageListTest (Long userId, Pageable pageable){
        return gifticonStorageRepository.findStorageByUserId(userId, pageable);
    }

    public GifticonStorageDto getStorageById(Long storageId){
        GifticonStorage gifticonStorage = gifticonStorageRepository.findById(storageId).orElse(null);

        if (gifticonStorage == null) {
            return null;
        }

        return gifticonStorage.toStorageDto();
    }

    public void deleteStorage(Long storageId) {
        gifticonStorageRepository.deleteById(storageId);
    }

    public Page<StorageAdminListDto> getStorageListByStatus(StorageStatus status, Pageable pageable) {
        return gifticonStorageRepository.findStorageByStorageStatus(status, pageable);
    }

    public Long getStorageCountByStatus(StorageStatus storageStatus) {
        return gifticonStorageRepository.getCountByAdminApproval();
    }
}
