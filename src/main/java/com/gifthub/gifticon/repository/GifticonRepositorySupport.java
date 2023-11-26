package com.gifthub.gifticon.repository;

import com.gifthub.gifticon.entity.Gifticon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GifticonRepositorySupport {

    Page<Gifticon> findByGifticonStatusIsOnSale(Pageable pageable, String type);

}
