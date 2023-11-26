package com.gifthub.gifticon.controller;

import com.gifthub.chatbot.util.JsonConverter;
import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.dto.GifticonImageDto;
import com.gifthub.gifticon.dto.ImageSaveDto;
import com.gifthub.gifticon.entity.GifticonStorage;

import com.gifthub.gifticon.service.GifticonImageService;
import com.gifthub.gifticon.service.GifticonService;
import com.gifthub.gifticon.service.GifticonStorageService;
import com.gifthub.gifticon.service.OcrService;
import com.gifthub.gifticon.util.GifticonImageUtil;
import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.service.UserService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    private final GifticonStorageService gifticonTempService;
    private final GifticonImageService gifticonImageService;

    private final GifticonService gifticonService;
    private final OcrService ocrService;
    private final UserService userService;

    private final UserJwtTokenProvider userJwtTokenProvider;


    @PostMapping("/kakao/chatbot/add")
    public ResponseEntity<Object> addGificonByKakao(@RequestBody Map<Object, Object> gifticon) {

        try {
            List<String> barcodeUrlList = JsonConverter.kakaoChatbotConverter(gifticon);

            for (String barcodeUrl : barcodeUrlList) {
                String barcode = GifticonService.readBarcode(barcodeUrl);
                GifticonDto gifticonDto = ocrService.readOcrUrlToGifticonDto(barcodeUrl);
                // TODO : 이미지 저장
                GifticonImageDto imageDto = gifticonImageService.saveImageByUrl(barcodeUrl);
                // TODO : save DB
                GifticonStorage gifticonStorage = gifticonTempService.saveTempStorage(gifticonDto, imageDto);
                gifticonStorage.setBarcode(barcode);

                // TODO : setUser는 현재 로그인한 사람

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
//            System.out.println(imageFile.getOriginalFilename());
            file = GifticonImageUtil.convert(imageFile);

            GifticonDto gifticonDto = ocrService.readOcrMultipartToGifticonDto(file); // 파일
            GifticonImageDto imageDto = gifticonImageService.saveImage(imageFile); // 이미지 서버에 저장 및 db에 경로저장

            GifticonStorage storage = gifticonTempService.saveTempStorage(gifticonDto, imageDto);
//            System.out.println("controller단에서 tempGifticonf: "+ tempId2);

            storage.setBarcode(GifticonService.readBarcode(imageDto.getAccessUrl()));

            // TODO : setUser 현재 로그인한 사람
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

            storage.setUser(userService.getUserById(userId).toEntity());

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

}
