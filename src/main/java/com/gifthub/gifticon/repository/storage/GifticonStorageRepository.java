package com.gifthub.gifticon.repository.storage;

import com.gifthub.gifticon.entity.GifticonStorage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GifticonStorageRepository extends JpaRepository<GifticonStorage, Long>, GifticonStorageRepositorySupport {
    List<GifticonStorage> findGifticonStorageByUserId(Long userId);
}
