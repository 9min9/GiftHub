package com.gifthub.admin.controller;


import com.gifthub.admin.dto.ProductAddRequest;
import com.gifthub.admin.dto.StorageAdminListDto;
import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.dto.storage.GifticonStorageDto;
import com.gifthub.gifticon.exception.NotFoundProductNameException;
import com.gifthub.gifticon.service.GifticonService;
import com.gifthub.gifticon.service.GifticonStorageService;
import com.gifthub.product.dto.ProductDto;
import com.gifthub.product.service.ProductService;
import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.gifthub.gifticon.enumeration.StorageStatus.ADMIN_APPROVAL;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final GifticonService gifticonService;
    private final UserJwtTokenProvider userJwtTokenProvider;
    private final GifticonStorageService gifticonStorageService;
    private final ProductService productService;

    @PostMapping("/gifticon/confirm/register")
    public ResponseEntity<Object> registerGifticon(@RequestBody ProductAddRequest request,
                                                   @RequestHeader HttpHeaders headers) {

        System.out.println("adminController");

        String productName = request.getProductName();
        String brandName = request.getBrandName();
        String due = request.getDue();
        String barcode = request.getBarcode();
        long storageId = Long.parseLong(request.getStorageId());
        Boolean isConfirm = request.getIsConfirm();
        System.out.println(isConfirm);

        try {
            if (isConfirm == null) {
                throw new Exception();
            }

            GifticonStorageDto storage = gifticonStorageService.getStorageById(storageId);
            UserDto user = storage.getUser();
            System.out.println(user.getId());

//            productService.saveProduct();

            System.out.println("%%%");
            System.out.println(isConfirm);

            if (isConfirm) {
                //todo : save product

//                productService.saveProduct(productDto);
//            System.out.println(product.getId());
                //todo : save gifticon
            }

            if (!isConfirm) {
                //todo return storage and reject message
            }

            return ResponseEntity.ok().body(Collections.singletonMap("status", "ok"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/get/cancel/reason")
    public ResponseEntity<Object> getCancelReason() {
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
        System.out.println("####");
        System.out.println(count);

        return ResponseEntity.ok().body(Collections.singletonMap("count", count));

    }

    @PostMapping("/gifticon/confirm/list")
    public ResponseEntity<Object> confirmRegisterList(@RequestHeader HttpHeaders headers, @PageableDefault(size = 10) Pageable pageable) {
        Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));
        System.out.println("!!!");
        System.out.println(userId);

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
