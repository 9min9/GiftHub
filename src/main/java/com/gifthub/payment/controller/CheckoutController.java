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
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final GifticonService gifticonService;

    @GetMapping
    public ResponseEntity<Object> checkout(Long[] gifticonIds) {
        List<GifticonDto> gifticons = new ArrayList<>();

        for (Long gifticonId : gifticonIds) {
            gifticons.add(gifticonService.findGifticon(gifticonId));
        }

        return ResponseEntity.ok(gifticons);
    }

}
