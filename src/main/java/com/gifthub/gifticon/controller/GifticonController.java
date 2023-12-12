package com.gifthub.gifticon.controller;

import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.service.GifticonImageService;
import com.gifthub.gifticon.service.GifticonService;
import com.gifthub.gifticon.service.GifticonStorageService;
import com.gifthub.gifticon.service.OcrService;
import com.gifthub.global.exception.ExceptionResponse;
import com.gifthub.product.service.ProductService;
import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.exception.NotLoginedException;
import com.gifthub.user.service.UserService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.http.entity.ContentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class GifticonController {
    private final GifticonService gifticonService;
    private final UserJwtTokenProvider userJwtTokenProvider;
    private final ExceptionResponse exceptionResponse;


    @GetMapping("/barcode/{barcode}")
    public void barcode(@PathVariable("barcode") String barcode, HttpServletResponse response) {
        ServletOutputStream outputStream = null;

        try {
            outputStream = response.getOutputStream();

            response.setContentType(ContentType.IMAGE_PNG.getMimeType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        GifticonService.writeBarcode(barcode, outputStream);
    }


    @GetMapping("/gifticons/{type}")
    public ResponseEntity<Object> gifticons(Pageable pageable, @PathVariable("type") String type) {
        return ResponseEntity.ok(gifticonService.getPurchasingGifticon(pageable, type));
    }



    @GetMapping("/gifticons")
    public ResponseEntity<Object> gifticonDtos(Pageable pageable, @RequestHeader HttpHeaders headers) {
        try {
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

            if (isNull(userId)) {
                throw new NotLoginedException();
            }

            Page<GifticonDto> gifticons = gifticonService.getGifticonByUserId(pageable, userId);

            return ResponseEntity.ok(gifticons);
        } catch (NotLoginedException e) {
            return ResponseEntity.badRequest().body(exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage()));
        }

    }

    @PostMapping("/gifticon/delete/{gifticonId}")
    public ResponseEntity<Object> deleteGifticon(@PathVariable("gifticonId") Long gifticonId,
                                                 @RequestHeader HttpHeaders headers) {
        try {
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

            if (isNull(userId)) {
                throw new NotLoginedException();
            }

            gifticonService.deleteById(gifticonId);

            return ResponseEntity.ok().build();
        } catch (NotLoginedException e) {
            return ResponseEntity.badRequest().body(exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage()));
        }
    }

    @PostMapping("/gifticon/forSale/{gifticonId}")
    public ResponseEntity<Object> setSale(@PathVariable("gifticonId") Long gifticonId,
                                          @RequestHeader HttpHeaders headers) {
        try {
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

            if (isNull(userId)) {
                throw new NotLoginedException();
            }

            gifticonService.setSale(gifticonId);

            return ResponseEntity.ok().build();

        } catch (NotLoginedException e) {
            return ResponseEntity.badRequest().body(exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage()));
        }

    }

    @GetMapping("/gifticon/products/{productId}")
    public ResponseEntity<Object> findGifticonByProudctId(Pageable pageable,
                                                     @PathVariable("productId") Long productId) {
        Page<GifticonDto> gifticons = gifticonService.getGifticonByProudctId(pageable, productId);

        return ResponseEntity.ok(gifticons);
    }

    private static <T> boolean isNull(T t) {
        if (t == null) {
            return true;
        } else {
            return false;
        }
    }

}



