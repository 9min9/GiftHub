package com.gifthub.payment.controller;

import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.service.GifticonService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final GifticonService gifticonService;

    @GetMapping
    public ResponseEntity<Object> checkout(Long[] gifticonIds) {
        List<GifticonDto> gifticons = new ArrayList<>();

        Long originalPrice = 0L;
        Long price = 0L;
        for (Long gifticonId : gifticonIds) {
            GifticonDto gifticon = gifticonService.findGifticon(gifticonId);

            originalPrice += gifticon.getProductDto().getPrice();
            price += gifticon.getPrice();

            gifticons.add(gifticon);
        }



        Map map = new HashMap();
        map.put("list", gifticons);
        map.put("originalPrice", originalPrice);
        map.put("price", price);

        return ResponseEntity.ok(map);
    }

}
