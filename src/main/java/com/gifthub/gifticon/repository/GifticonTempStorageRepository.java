package com.gifthub.gifticon.repository;

import com.gifthub.gifticon.entity.GifticonTempStorage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GifticonTempStorageRepository extends JpaRepository<GifticonTempStorage, Long> {
}
