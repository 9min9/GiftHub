package com.gifthub.gifticon.repository;

import com.gifthub.gifticon.entity.Gifticon;
import com.gifthub.gifticon.enumeration.GifticonStatus;
import com.gifthub.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GifticonRepository extends JpaRepository<Gifticon, Long> {
    List<Gifticon> findByUser(User user);
    List<Gifticon> findByBrandName(String brandName);
    List<Gifticon> findByProductName(String productName);
    List<Gifticon> findByGifticonStatus(GifticonStatus gifticonStatus);
}
