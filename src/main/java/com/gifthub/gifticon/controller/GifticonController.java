package com.gifthub.gifticon.controller;

import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.dto.GifticonRegisterRequest;
import com.gifthub.gifticon.dto.storage.GifticonStorageDto;
import com.gifthub.gifticon.exception.NotFoundStorageException;
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
    private final GifticonStorageService storageService;
    private final UserJwtTokenProvider userJwtTokenProvider;
    private final UserService userService;
    private final ProductService productService;
    private final ExceptionResponse exceptionResponse;
    private final ErrorResponse errorResponse;

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
        String price = request.getPrice();
        String due = request.getDue();

        try {
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

            if (userId == null) {
                throw new NotLoginedException();
            }

            UserDto findUser = userService.getUserById(userId);
            storageDto = storageService.getStorageById(storageId);
            storageDto.setUser(findUser);
            storageDto.setDue(LocalDate.parse(due));

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
            e.printStackTrace();
            log.error("registerGifticon | " + e);
            return ResponseEntity.badRequest().body(exceptionResponse.getException(null, "Exception", e.getMessage()));
        }

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(errorResponse.getErrors(bindingResult));

        } else {
            gifticonService.saveGifticon(storageDto.toGifticonDto(productDto));
            storageService.deleteStorage(storageDto.getId());
            return ResponseEntity.ok().body(Collections.singletonMap("status", "200"));
        }
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



}



