package com.gifthub.gifticon.repository.image;

import com.gifthub.gifticon.entity.BarcodeImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BarcodeImageRepository extends JpaRepository<BarcodeImage, Long> {

    Optional<BarcodeImage> findBarcodeImageByGifticon_Id(Long gifticonId);
}
