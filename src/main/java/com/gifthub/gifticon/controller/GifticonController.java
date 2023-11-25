package com.gifthub.gifticon.controller;

import com.gifthub.chatbot.util.JsonConverter;
import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.dto.GifticonImageDto;
import com.gifthub.gifticon.dto.ImageSaveDto;
import com.gifthub.gifticon.service.GifticonImageService;
import com.gifthub.gifticon.service.GifticonService;
import com.gifthub.gifticon.service.GifticonStorageService;
import com.gifthub.gifticon.service.OcrService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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


    @PostMapping("/kakao/chatbot/add")
    public ResponseEntity<Object> addGificonByKakao(@RequestBody Map<Object, Object> gifticon) {

        try {
            List<String> barcodeUrlList = JsonConverter.kakaoChatbotConverter(gifticon);

            for (String barcodeUrl : barcodeUrlList) {
                String barcode = GifticonService.readBarcode(barcodeUrl);
                GifticonDto gifticonDto = ocrService.readOcrUrlToGifticonDto(barcodeUrl);
                // TODO : 이미지 저장
//                GifticonImageDto imageDto = gifticonImageService.saveImage(barcodeUrl);
                //todo : save DB
//                gifticonTempService.saveStorage(gifticonDto, imageDto);


            }

        } catch (Exception e) {     //todo : url이 barcode가 아닌 경우 exception 처리하기
            return ResponseEntity.badRequest().build(); // 400이 날라감 -> ajax에
        }

        return ResponseEntity.ok().build();  // 200이 날라감 0 -> ajax에 success
    }

    @PostMapping("/gifticon/add") // MultipartType으로 받는다 (1개)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> addGifticonByFile(@RequestPart MultipartFile imageFile) {
        try {
//            System.out.println(imageFile.getOriginalFilename());

            GifticonDto gifticonDto = ocrService.readOcrMultipartToGifticonDto(imageFile.getOriginalFilename()); // ? originalName?
            System.out.println("controller단에서 gifticonDto:" + gifticonDto.getProductName());

            GifticonImageDto imageDto = gifticonImageService.saveImage(imageFile);
            System.out.println("Controller / imageDto: " + imageDto.getId());



            // TODO : 임시 저장소에 저장
//            Long tempId = gifticonTempService.saveStorage(gifticonDto, imageDto);
//            System.out.println("controller단에서 tempGifticon: " + tempId);
            //
            Long tempId2 = gifticonTempService.saveTempStorage(gifticonDto, imageDto);
            System.out.println("controller단에서 tempGifticonf: "+ tempId2);

            GifticonDto gifticonDto1 = ocrService.readOcrUrlToGifticonDto(imageDto.getAccessUrl());
            System.out.println("controller / url로 GifticonDto : " + gifticonDto1.getProductName());

            // TODO : 그 다음에는?

        } catch (Exception e){
            return ResponseEntity.badRequest().build(); // 임시저장소에 저장이 안됐을때 에러 분리
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


    // TODO : 서버와 DB에서 삭제
//    @DeleteMapping("/image/delete")
//    @ResponseStatus(HttpStatus.OK)
//    public void deleteImage(@RequestParam("name") String fileName){
//        gifticonImageService
//    }

}
