package com.gifthub.gifticon.repository;

import com.gifthub.gifticon.entity.Gifticon;
import com.gifthub.gifticon.enumeration.GifticonStatus;
import com.gifthub.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GifticonRepository extends JpaRepository<Gifticon, Long>, GifticonRepositorySupport {
    List<Gifticon> findByUser(User user);
    List<Gifticon> findByBrandName(String brandName);
    List<Gifticon> findByProductName(String productName);
    List<Gifticon> findByGifticonStatus(GifticonStatus gifticonStatus);
}
