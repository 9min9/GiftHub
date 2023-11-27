package com.gifthub.gifticon.controller;

import com.gifthub.chatbot.util.JsonConverter;
import com.gifthub.gifticon.dto.*;
import com.gifthub.gifticon.entity.Gifticon;
import com.gifthub.gifticon.entity.GifticonStorage;

import com.gifthub.gifticon.service.*;
import com.gifthub.gifticon.util.GifticonImageUtil;
import com.gifthub.gifticon.util.JsonMapper;
import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.entity.User;
import com.gifthub.user.service.UserService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class GifticonController {
    private final GifticonStorageService gifticonStorageService;
    private final GifticonImageService gifticonImageService;
    private final GifticonService gifticonService;
    private final OcrService ocrService;
    private final UserService userService;
    private final ProductService productService;
    private final UserJwtTokenProvider userJwtTokenProvider;


    @PostMapping("/kakao/chatbot/add")
    public ResponseEntity<Object> addGificonByKakao(@RequestBody Map<Object, Object> gifticon,
                                                    @RequestHeader HttpHeaders headers) {

        try {
            List<String> barcodeUrlList = JsonConverter.kakaoChatbotConverter(gifticon);

            for (String barcodeUrl : barcodeUrlList) {
                String barcode = GifticonService.readBarcode(barcodeUrl);
                GifticonDto gifticonDto = ocrService.readOcrUrlToGifticonDto(barcodeUrl);
                GifticonImageDto imageDto = gifticonImageService.saveImageByUrl(barcodeUrl);
                gifticonDto.setBarcode(barcode);
                gifticonDto.setUser(userService.getUserById(userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0))));

//                System.out.println(gifticonDto.getUser().getId());

                System.out.println(gifticonStorageService.saveStorage(gifticonDto, imageDto));


            }

        } catch (Exception e) {     //todo : url이 barcode가 아닌 경우 exception 처리하기
            return ResponseEntity.badRequest().build(); // 400이 날라감 -> ajax에
        }

        return ResponseEntity.ok().build();  // 200이 날라감 0 -> ajax에 success
    }

    @PostMapping("/gifticon/add") // MultipartType으로 받는다 (1개)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> addGifticonByFile(@RequestPart MultipartFile imageFile,
                                                    @RequestHeader HttpHeaders headers) {
        File file = null;
        try {
            file = GifticonImageUtil.convert(imageFile);

            GifticonDto gifticonDto = ocrService.readOcrMultipartToGifticonDto(file); // 파일
            GifticonImageDto imageDto = gifticonImageService.saveImage(imageFile); // 이미지 서버에 저장 및 db에 경로저장

            String barcode = GifticonService.readBarcode(imageDto.getAccessUrl());
            gifticonDto.setBarcode(barcode);
            gifticonDto.setUser(userService.getUserById(userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0))));

//            System.out.println(gifticonDto.getUser().getId());
            gifticonStorageService.saveStorage(gifticonDto, imageDto);


        } catch (Exception e) {
            return ResponseEntity.badRequest().build();

        } finally {
            file.delete();
        }
        return ResponseEntity.ok().build();


    }

    @PostMapping("/gifticon/addMultiple") // MultipartType으로 받는다 (여러개)
    @ResponseStatus(HttpStatus.OK)
    public List<String> addGifticonByFiles(@ModelAttribute ImageSaveDto imageSaveDto) {
//        return gifticonImageService.saveImages(imageSaveDto);
        return null;
    }


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

    @RequestMapping(value = "/gifticon/storage/list", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<Object> getStorageList(@RequestHeader HttpHeaders headers, @PageableDefault(size = 6) Pageable pageable) {

        try {
            User user = userService.getUserById(userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0))).toEntity();

            Page<GifticonStorageListDto> storageList = gifticonStorageService.getStorageList(user.getId(), pageable);

            return ResponseEntity.ok(storageList);

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/api/gifticon/register/{id}")
    public ResponseEntity<Object> registerGifticon(@PathVariable Long storage_id,
                                                   @RequestHeader HttpHeaders headers) {
        try {
//            User user = userService.getUserById(userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0))).toEntity();
            // TODO : 상품명이 db에 있는지 check
            GifticonStorage gifticonStorage = gifticonStorageService.getStorageById(storage_id);

            ProductDto product = productService.getByProductName(gifticonStorage.getProductName());
            if(product == null){
                return ResponseEntity.badRequest().build(); // 관리자에게 문의 전송 how?
            }
            // 가지고 있는것 : 상품dto, storage entity -> 필요한것 gifticonDto
            GifticonDto gifticonDto = gifticonStorage.toGifticonDto(product);


            gifticonService.saveGifticon(gifticonDto);


            ResponseEntity.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.badRequest().build();
    }

}



