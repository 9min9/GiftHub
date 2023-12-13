package com.gifthub.gifticon.service;

import com.gifthub.admin.dto.StorageAdminListDto;
import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.dto.GifticonImageDto;
import com.gifthub.gifticon.dto.GifticonStorageListDto;
import com.gifthub.gifticon.dto.storage.GifticonStorageDto;
import com.gifthub.gifticon.entity.GifticonStorage;
import com.gifthub.gifticon.enumeration.StorageStatus;
import com.gifthub.gifticon.exception.NotFoundStorageException;
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
        GifticonStorage storage = null;

        if (gifticonDto.getId() != null) {
            storage = gifticonStorageRepository.findById(gifticonDto.getId()).orElse(null);
        } else {
            storage = gifticonDto.toStorageEntity(imageDto);
        }

        if (storage != null) {
            storage.changeStatus(WAIT_REGISTRATION);
        }

        if (gifticonDto.getProductName() == null || gifticonDto.getBrandName() == null || gifticonDto.getBarcode() == null || gifticonDto.getDue() == null) {
            storage.changeStatus(NEED_APPROVAL);
        }

        return gifticonStorageRepository.save(storage);
    }

    public void storageToAdmin(GifticonStorageDto storageDto) {
        GifticonStorage gifticonStorage = gifticonStorageRepository.findById(storageDto.getId()).orElse(null);

        if (gifticonStorage != null) {
            gifticonStorage.changeProductName(storageDto.getProductName());
            gifticonStorage.changeBrandName(storageDto.getBrandName());
            gifticonStorage.changeBarcode(storageDto.getBarcode());
            gifticonStorage.changeDue(storageDto.getDue());
            gifticonStorage.changePrice(storageDto.getPrice());
            gifticonStorage.changeStatus(ADMIN_APPROVAL);

            gifticonStorageRepository.save(gifticonStorage);
        }
    }

    public void adminToStorage(GifticonStorageDto storageDto) {
        GifticonStorage findStorage = gifticonStorageRepository.findById(storageDto.getId()).orElse(null);

        if (findStorage != null) {
            findStorage.changeStatus(FAIL_REGISTRATION);
            gifticonStorageRepository.save(findStorage);
        }
    }

    public Page<GifticonStorageListDto> getStorageList(Long userId, Pageable pageable) {
        return gifticonStorageRepository.findGifticonStorageDtoByUserId(userId, pageable);
    }

    public Page<GifticonStorage> getStorageListTest(Long userId, Pageable pageable) {
        return gifticonStorageRepository.findStorageByUserId(userId, pageable);
    }

    public GifticonStorageDto getStorageById(Long storageId) throws NotFoundStorageException {
        GifticonStorage gifticonStorage = gifticonStorageRepository.findById(storageId).orElse(null);

        if (gifticonStorage == null) {
            throw new NotFoundStorageException();
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
