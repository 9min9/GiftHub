package com.gifthub.gifticon.controller;

import com.gifthub.gifticon.dto.BarcodeImageDto;
import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.dto.GifticonRegisterRequest;
import com.gifthub.gifticon.dto.storage.GifticonStorageDto;
import com.gifthub.gifticon.exception.NotFoundStorageException;
import com.gifthub.gifticon.service.GifticonImageService;
import com.gifthub.gifticon.service.GifticonService;
import com.gifthub.gifticon.service.GifticonStorageService;
import com.gifthub.global.error.ErrorResponse;
import com.gifthub.global.exception.ExceptionResponse;
import com.gifthub.product.dto.ProductDto;
import com.gifthub.product.enumeration.CategoryName;
import com.gifthub.product.service.ProductService;
import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.dto.UserDto;
import com.gifthub.user.exception.NotLoginedException;
import com.gifthub.user.service.UserService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;

import static java.util.Objects.isNull;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GifticonController {
    private final GifticonService gifticonService;
    private final GifticonStorageService gifticonStorageService;
    private final GifticonStorageService storageService;
    private final UserJwtTokenProvider userJwtTokenProvider;
    private final UserService userService;
    private final ProductService productService;
    private final ExceptionResponse exceptionResponse;
    private final ErrorResponse errorResponse;
    private final GifticonImageService imageService;

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

    @PostMapping("/gifticon/register") // db에 있는경우
    public ResponseEntity<Object> registerGifticon(@Valid @RequestBody GifticonRegisterRequest request,
                                                   BindingResult bindingResult, @RequestHeader HttpHeaders headers) {
        GifticonStorageDto storageDto = null;
        ProductDto productDto = null;

        Long storageId = request.getStorageId();
        String brandName = request.getBrandName();
        Long price = request.getPrice();
        LocalDate due = request.getDue();

        log.error("request: "+ request);

        try {
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

            if (userId == null) {
                throw new NotLoginedException();
            }

            UserDto findUser = userService.getUserById(userId);
            storageDto = storageService.getStorageById(storageId);
            storageDto.setBrandName(brandName);
            storageDto.setUser(findUser);
            storageDto.setDue(due);
            storageDto.setPrice(price);

            productDto = productService.getByProductName(storageDto.getProductName());
            CategoryName engCategory = CategoryName.ofKor(productDto.getCategory());
            productDto.setCategory(engCategory.name());

        } catch (NotLoginedException e) {
            log.error("registerGifticon | " + e);
            return ResponseEntity.badRequest().body(exceptionResponse.getException(null, e.getCode(), e.getMessage()));
        } catch (NotFoundStorageException e) {
            log.error("registerGifticon | " + e);
            return ResponseEntity.badRequest().body(exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage()));
        } catch (Exception e) {
            log.error("registerGifticon | " + e);
            return ResponseEntity.badRequest().body(exceptionResponse.getException(null, "Exception", e.getMessage()));
        }

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(errorResponse.getErrors(bindingResult));

        } else {
            gifticonService.saveGifticon(storageDto.toGifticonDto(productDto));
            imageService.deleteFileByStorage(storageDto);
            storageService.deleteStorage(storageDto.getId());
            return ResponseEntity.ok().body(Collections.singletonMap("status", "200"));
        }
    }

    @PostMapping("/gifticon/register/check")
    public ResponseEntity<Object> confirmRegister(@Valid @RequestBody GifticonRegisterRequest request, BindingResult bindingResult, @RequestHeader HttpHeaders headers) {
        try {
            GifticonStorageDto gifticonStorageDto = request.storageDto();
            gifticonStorageService.storageToAdmin(gifticonStorageDto);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        if(bindingResult.hasErrors()) {
            ResponseEntity.badRequest().body(errorResponse.getErrors(bindingResult));
        }

        return ResponseEntity.ok().body(Collections.singletonMap("status", "ok"));
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
    @PostMapping("/gifticon/notForSale/{gifticonId}")
    public ResponseEntity<Object> setNone(@PathVariable("gifticonId") Long gifticonId,
                                             @RequestHeader HttpHeaders headers){
        try{
            GifticonDto gifticon = gifticonService.findGifticon(gifticonId);
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

            if(!gifticon.getUser().getId().equals(userId)){
                ResponseEntity.badRequest().build();
            }
            gifticonService.notForSale(gifticonId);

        } catch (Exception e){
            log.error("setNone | " + e);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
    @PostMapping("/gifticon/price/{gifticonId}")
    public ResponseEntity<Object> changePrice(@PathVariable("gifticonId") Long gifticonId,
                                              @RequestHeader HttpHeaders headers){
        try{
            GifticonDto gifticon = gifticonService.findGifticon(gifticonId);
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

            if(!gifticon.getUser().getId().equals(userId)){
                ResponseEntity.badRequest().build();
            }
            gifticonService.changePrice(gifticonId);

        } catch (Exception e){
            log.error("changePrice | " + e);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/gifticon/products/{productId}")
    public ResponseEntity<Object> findGifticonByProudctId(Pageable pageable,
                                                     @PathVariable("productId") Long productId) {
        Page<GifticonDto> gifticons = gifticonService.getGifticonByProudctId(pageable, productId);  

        return ResponseEntity.ok(gifticons);
    }

    @PostMapping("/gifticon/use/{gifticonId}")
    public ResponseEntity<Object> useMyGifticonTest(@PathVariable("gifticonId") Long gifticonId,
                                                    @RequestHeader HttpHeaders headers){
        // 먼저 이미지 만들어서 서버에 저장
        try{
            GifticonDto gifticon = gifticonService.findGifticon(gifticonId);
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

            if(!gifticon.getUser().getId().equals(userId)){
                ResponseEntity.badRequest().build();
            }
            int width = 200;
            int height = 200;

            File file = gifticonService.getBarcodeImage(gifticon.getId(), width, height);

            BarcodeImageDto barcodeImage = imageService.saveBarcodeImage(file, gifticonId);

            return ResponseEntity.ok(barcodeImage);



        } catch (Exception e){
            log.error("useMyGifticonTest | " + e);
            return ResponseEntity.badRequest().build();
        }
//        return ResponseEntity.ok().build();

    }



}



