package com.gifthub.admin.controller;


import com.gifthub.admin.dto.ProductAddRequest;
import com.gifthub.admin.dto.StorageAdminListDto;
import com.gifthub.admin.exception.NotSelectConfirmFlagException;
import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.dto.storage.GifticonStorageDto;
import com.gifthub.gifticon.enumeration.GifticonStatus;
import com.gifthub.gifticon.exception.NotFoundProductNameException;
import com.gifthub.gifticon.exception.NotFoundStorageException;
import com.gifthub.gifticon.service.GifticonService;
import com.gifthub.gifticon.service.GifticonStorageService;
import com.gifthub.global.error.ErrorResponse;
import com.gifthub.global.exception.ExceptionResponse;
import com.gifthub.product.dto.ProductDto;
import com.gifthub.product.service.ProductService;
import com.gifthub.user.UserJwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
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

    @PostMapping("/gifticon/confirm/register")
    public ResponseEntity<Object> registerGifticon(@Valid @RequestBody ProductAddRequest request, BindingResult bindingResult,
                                                   @RequestHeader HttpHeaders headers, Errors errors) {
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

            GifticonStorageDto storage = gifticonStorageService.getStorageById(storageId);

            if (isConfirm) {
                if (!request.getCategory().isBlank() && request.getCategory() != null) {
                    ProductDto productDto = request.toProductDto();
                    ProductDto findProduct = productService.getByProductName(productName);

                    if (findProduct != null) {
                        productDto.setId(findProduct.getId());
                    }

                    Long productId = productService.saveProduct(productDto);
                    productDto.setId(productId);

                    GifticonDto gifticonDto = GifticonDto.builder()
                            .user(storage.getUser())
                            .barcode(barcode)
                            .due(LocalDate.parse(due))
                            .brandName(brandName)
                            .productName(productName)
                            .productDto(productDto)
                            .gifticonStatus(GifticonStatus.NONE)
                            .price(productDto.getPrice())
                            .build();

                    gifticonService.saveGifticon(gifticonDto);
                    gifticonStorageService.deleteStorage(storageId);

                } else {
                    bindingResult.rejectValue("category", "NotSelect", null);
                }
            }

            if (!isConfirm) {
                gifticonStorageService.adminToStorage(storage);
            }

        } catch (NotSelectConfirmFlagException e) {
            log.error("AdminController | registerGifticon | " + e);
            errors.rejectValue("isConfirm", e.getCode(), e.getMessage());
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

    @PostMapping("/gifticon/register")
    public ResponseEntity<Object> confirmRegister(@RequestBody GifticonStorageDto gifticonStorageDto, @RequestHeader HttpHeaders headers) {
        try {
            Long storageId = gifticonStorageDto.getId();
            String productName = gifticonStorageDto.getProductName();
            String brandName = gifticonStorageDto.getBrandName();
            String barcode = gifticonStorageDto.getBarcode();
            LocalDate due = gifticonStorageDto.getDue();
            Long price = gifticonStorageDto.getPrice();

            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));
            gifticonStorageService.storageToAdmin(gifticonStorageDto);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(Collections.singletonMap("status", "ok"));
    }

    @PostMapping("/gifticon/confirm/count")
    public ResponseEntity<Object> confirmRegisterCount(@RequestHeader HttpHeaders headers) {
        Long count = gifticonStorageService.getStorageCountByStatus(ADMIN_APPROVAL);

        return ResponseEntity.ok().body(Collections.singletonMap("count", count));

    }

    @PostMapping("/gifticon/confirm/list")
    public ResponseEntity<Object> confirmRegisterList(@RequestHeader HttpHeaders headers, @PageableDefault(size = 10) Pageable pageable) {
        Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

        Page<StorageAdminListDto> findPage =
                gifticonStorageService.getStorageListByStatus(ADMIN_APPROVAL, pageable);

        return ResponseEntity.ok().body(findPage);
    }


    @PostMapping("/validate/product")
    public ResponseEntity<Object> productValidate(@RequestBody Map<String, String> request, @RequestHeader HttpHeaders headers) {
        Map<String, String> result = new HashMap<>();
//        userJwtTokenProvider.validateToken(headers.get("Authorization").get(0));

        try {
            result.put("target", "product");
            String product = request.get("product");

            if (gifticonService.productNameIsNull(product)) {
                throw new NotFoundProductNameException();
            }

            result.put("status", "success");

            return ResponseEntity.ok().body(result);

        } catch (NotFoundProductNameException e) {
            result.put("status", "error");
            result.put("code", e.getCode());
            result.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(result);
        }
    }


}
