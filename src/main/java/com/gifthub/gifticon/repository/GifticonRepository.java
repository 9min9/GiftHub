package com.gifthub.gifticon.repository;

import com.gifthub.gifticon.entity.Gifticon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GifticonRepository extends JpaRepository<Gifticon, Long> {
}
