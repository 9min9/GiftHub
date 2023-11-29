package com.gifthub.admin;


import com.gifthub.gifticon.dto.storage.GifticonStorageDto;
import com.gifthub.gifticon.exception.NotFoundProductNameException;
import com.gifthub.gifticon.service.GifticonService;
import com.gifthub.gifticon.service.GifticonStorageService;
import com.gifthub.product.service.ProductService;
import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final GifticonService gifticonService;
    private final UserJwtTokenProvider userJwtTokenProvider;
    private final UserService userService;
    private final ProductService productService;
    private final GifticonStorageService gifticonStorageService;


    @PostMapping("/gifticon/register")
    public ResponseEntity<Object> sendToAdmin(@RequestBody GifticonStorageDto gifticonStorageDto, @RequestHeader HttpHeaders headers) {
        HashMap<String, String> result = new HashMap<>();

        try {
            System.out.println("@@@");
            Long storageId = gifticonStorageDto.getId();
            String productName = gifticonStorageDto.getProductName();
            String brandName = gifticonStorageDto.getBrandName();
            String barcode = gifticonStorageDto.getBarcode();
            LocalDate due = gifticonStorageDto.getDue();

            System.out.println(storageId);
            System.out.println(productName);
            System.out.println(brandName);
            System.out.println(barcode);
            System.out.println(due);

            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

            if (gifticonService.productNameIsNull(productName)) {
//                bindingResult.rejectValue("product", "NotNull", null);
            }

            if (gifticonService.brandNameIsNull(brandName)) {
//                bindingResult.rejectValue("brand", "NotNull", null);
            }

            if (gifticonService.barcodeIsNull(barcode)) {
//                bindingResult.rejectValue("barcode", "NotNull", null);
            }

            if (gifticonService.dueIsNull(due)) {
//                bindingResult.rejectValue("due", "NotNull", null);
            }

            gifticonStorageService.storageToAdmin(gifticonStorageDto);

            result.put("ok", "ok");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (bindingResult.hasErrors()) {
//            return ResponseEntity.badRequest().body(bindingResult);
//        }



        return ResponseEntity.ok().body(result);
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
