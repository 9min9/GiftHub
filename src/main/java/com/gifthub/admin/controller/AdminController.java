package com.gifthub.admin.controller;


import com.gifthub.admin.dto.GifticonAppovalRequest;
import com.gifthub.admin.dto.StorageAdminListDto;
import com.gifthub.admin.exception.NotSelectConfirmFlagException;
import com.gifthub.admin.exception.validator.GifticonAppovalValidator;
import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.dto.storage.GifticonStorageDto;
import com.gifthub.gifticon.enumeration.GifticonStatus;
import com.gifthub.gifticon.exception.NotFoundProductNameException;
import com.gifthub.gifticon.exception.NotFoundStorageException;
import com.gifthub.gifticon.service.GifticonImageService;
import com.gifthub.gifticon.service.GifticonService;
import com.gifthub.gifticon.service.GifticonStorageService;
import com.gifthub.global.error.ErrorResponse;
import com.gifthub.global.exception.ExceptionResponse;
import com.gifthub.product.dto.ProductDto;
import com.gifthub.product.service.ProductService;
import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.exception.NotLoginedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.gifthub.gifticon.enumeration.StorageStatus.ADMIN_APPROVAL;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {
    private final GifticonService gifticonService;
    private final UserJwtTokenProvider userJwtTokenProvider;
    private final GifticonStorageService gifticonStorageService;
    private final ProductService productService;
    private final ErrorResponse errorResponse;
    private final ExceptionResponse exceptionResponse;
    private final GifticonAppovalValidator gifticonConfirmRegister;
    private final GifticonImageService imageService;

    @PostMapping("/gifticon/confirm/register")
    public ResponseEntity<Object> registerGifticon(@Valid @RequestBody GifticonAppovalRequest request, BindingResult bindingResult, @RequestHeader HttpHeaders headers) {
        String productName = request.getProductName();
        String brandName = request.getBrandName();
        String due = request.getDue();
        String barcode = request.getBarcode();
        long storageId = Long.parseLong(request.getStorageId());
        Boolean isConfirm = request.getIsConfirm();

        try {
            if (isConfirm == null) {
                throw new NotSelectConfirmFlagException();
            }

            GifticonStorageDto storageDto = gifticonStorageService.getStorageById(storageId);

            if (isConfirm) {
                gifticonConfirmRegister.validate(request, bindingResult);

                if (!request.getCategory().isBlank() && request.getCategory() != null) {
                    ProductDto productDto = request.toProductDto();
                    ProductDto findProduct = productService.getByProductName(productName);

                    if (findProduct != null) {
                        productDto.setId(findProduct.getId());
                    }

                    if (findProduct == null) {
                        Long productId = productService.saveProduct(productDto);
                        productDto.setId(productId);
                    } else {
                        productDto.setId(findProduct.getId());
                    }

                    GifticonDto gifticonDto = GifticonDto.builder()
                            .user(storageDto.getUser())
                            .barcode(barcode)
                            .due(LocalDate.parse(due))
                            .brandName(brandName)
                            .productName(productName)
                            .productDto(productDto)
                            .gifticonStatus(GifticonStatus.NONE)
                            .price(productDto.getPrice())
                            .build();

                    gifticonService.saveGifticon(gifticonDto);
                    imageService.deleteFileByStorage(storageDto);
                    gifticonStorageService.deleteStorage(storageId);
                }
            }

            if (!isConfirm) {
                //TODO : Reg fail Reason 전송하기
                gifticonStorageService.adminToStorage(storageDto);
            }

        } catch (NotSelectConfirmFlagException e) {
            log.error("AdminController | registerGifticon | " + e);
            bindingResult.rejectValue("isConfirm", e.getCode(), e.getMessage());
        } catch (NotFoundStorageException e) {
            log.error("AdminController | registerGifticon | " + e);
            return ResponseEntity.badRequest().body(exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage()));

        } catch (Exception e) {
            log.error("AdminController | registerGifticon | " + e);
            return ResponseEntity.badRequest().body(e);
        }

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(errorResponse.getErrors(bindingResult));
        }

        return ResponseEntity.ok(Collections.singletonMap("status", "ok"));
    }

    @PostMapping("/get/cancel/reason")
    public ResponseEntity<Object> getRejectReason() {
        return null;
    }

    @PostMapping("/gifticon/confirm/count")
    public ResponseEntity<Object> confirmRegisterCount(@RequestHeader HttpHeaders headers) {
        Long count = gifticonStorageService.getStorageCountByStatus(ADMIN_APPROVAL);

        return ResponseEntity.ok().body(Collections.singletonMap("count", count));

    }

    @PostMapping("/gifticon/confirm/list")
    public ResponseEntity<Object> confirmRegisterList(@RequestHeader HttpHeaders headers, @PageableDefault(size = 10) Pageable pageable) {
        try {
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

            if (userId == null) {
                throw new NotLoginedException();
            }

            Page<StorageAdminListDto> findPage = gifticonStorageService.getStorageListByStatus(ADMIN_APPROVAL, pageable);
            return ResponseEntity.ok().body(findPage);

        } catch (NotLoginedException e) {
            log.error("AdminController | confirmRegisterList | " +e);
            return ResponseEntity.badRequest().body(exceptionResponse.getException(null, e.getCode(), e.getMessage()));
        }
    }


    @PostMapping("/validate/product")
    public ResponseEntity<Object> productValidate(@RequestBody Map<String, String> request, @RequestHeader HttpHeaders headers) {
        Map<String, String> result = new HashMap<>();

        try {
            result.put("target", "product");
            String product = request.get("product");

            if (gifticonService.productNameIsNull(product)) {
                throw new NotFoundProductNameException();
            }

            result.put("status", "success");

            return ResponseEntity.ok().body(result);

        } catch (NotFoundProductNameException e) {
            log.error("AdminController | productValidate | " +e);
            return ResponseEntity.badRequest().body(exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage()));
        }
    }
}
