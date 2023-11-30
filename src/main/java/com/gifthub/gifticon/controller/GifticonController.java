package com.gifthub.gifticon.controller;

import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.dto.storage.GifticonStorageDto;
import com.gifthub.gifticon.service.GifticonImageService;
import com.gifthub.gifticon.service.GifticonService;
import com.gifthub.gifticon.service.GifticonStorageService;
import com.gifthub.gifticon.service.OcrService;
import com.gifthub.product.dto.ProductDto;
import com.gifthub.product.service.ProductService;
import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.dto.UserDto;
import com.gifthub.user.service.UserService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class GifticonController {
    private final GifticonStorageService gifticonStorageService;
    private final GifticonService gifticonService;
    private final UserService userService;
    private final ProductService productService;
    private final UserJwtTokenProvider userJwtTokenProvider;


    @GetMapping("/barcode/{barcode}")
    public void barcode(@PathVariable("barcode") String barcode, HttpServletResponse response) {
        ServletOutputStream outputStream = null;

        try {
            outputStream = response.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        GifticonService.writeBarcode(barcode, outputStream);
    }


    @GetMapping("/gifticons/{type}")
    public ResponseEntity<Object> gifticons(Pageable pageable, @PathVariable("type") String type) {
        return ResponseEntity.ok(gifticonService.getPurchasingGifticon(pageable, type));
    }


    @PostMapping("/gifticon/register") // db에 있는경우
    public ResponseEntity<Object> registerGifticon(@RequestBody Map<String, String> request,
                                                   @RequestHeader HttpHeaders headers) {
        Map<String, String> result = new HashMap<>();

        long storageId = Long.parseLong(request.get("id"));

        try {
            GifticonStorageDto storage = gifticonStorageService.getStorageById(storageId);
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));
            UserDto findUser = userService.getUserById(userId);
            storage.setUser(findUser);

            ProductDto product = productService.getByProductName(storage.getProductName());
            GifticonDto gifticonDto = storage.toGifticonDto(product);
            gifticonService.saveGifticon(gifticonDto);
            gifticonStorageService.deleteStorage(storage.getId());

            result.put("status", "success");
            return ResponseEntity.ok().body(result);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/gifticons")
    public ResponseEntity<Object> gifticonDtos(Pageable pageable, @RequestHeader HttpHeaders headers) {
        try {
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

            Page<GifticonDto> gifticons = gifticonService.getGifticonByUserId(pageable, userId);

            return ResponseEntity.ok(gifticons);
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/gifticon/delete/{gifticonId}")
    public ResponseEntity<Object> deleteGifticon(@PathVariable("gifticonId") Long gifticonId,
                                                 @RequestHeader HttpHeaders headers) {
        Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

        GifticonDto gifticonDto = gifticonService.findGifticon(gifticonId);

        gifticonService.deleteById(gifticonId);

        return ResponseEntity.ok().build();
    }

}



