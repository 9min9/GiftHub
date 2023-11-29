package com.gifthub.admin;


import com.gifthub.admin.dto.StorageAdminListDto;
import com.gifthub.gifticon.dto.storage.GifticonStorageDto;
import com.gifthub.gifticon.exception.NotFoundProductNameException;
import com.gifthub.gifticon.service.GifticonService;
import com.gifthub.gifticon.service.GifticonStorageService;
import com.gifthub.user.UserJwtTokenProvider;
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

    @PostMapping("/validate/product")
    public ResponseEntity<Object> productValidate(@RequestBody Map<String, String> request, @RequestHeader HttpHeaders headers){
        Map<String,String> result = new HashMap<>();
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
