package com.gifthub.gifticon.repository;

import com.gifthub.gifticon.entity.Gifticon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GifticonRepositorySupport {

    Page<Gifticon> findByGifticonStatusIsOnSale(Pageable pageable, String type);

    List<String> findBrandNameByCategory(String category);

}
