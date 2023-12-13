package com.gifthub.gifticon.controller;

import com.gifthub.chatbot.util.JsonConverter;
import com.gifthub.config.jwt.JwtContext;
import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.dto.GifticonImageDto;
import com.gifthub.gifticon.dto.GifticonStorageListDto;
import com.gifthub.gifticon.dto.ImageSaveDto;
import com.gifthub.gifticon.dto.storage.GifticonStorageDto;
import com.gifthub.gifticon.entity.GifticonStorage;
import com.gifthub.gifticon.exception.NotExpiredDueException;
import com.gifthub.gifticon.exception.NotValidFileExtensionException;
import com.gifthub.gifticon.service.GifticonImageService;
import com.gifthub.gifticon.service.GifticonService;
import com.gifthub.gifticon.service.GifticonStorageService;
import com.gifthub.gifticon.service.OcrService;
import com.gifthub.gifticon.util.GifticonImageUtil;
import com.gifthub.gifticon.util.OcrUtil;
import com.gifthub.global.exception.ExceptionResponse;
import com.gifthub.product.service.ProductService;
import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.dto.UserDto;
import com.gifthub.user.service.UserService;
import com.google.zxing.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/storage")
@Slf4j
public class StorageController {
    private final GifticonStorageService storageService;
    private final GifticonImageService imageService;
    private final UserJwtTokenProvider userJwtTokenProvider;
    private final UserService userService;
    private final OcrService ocrService;
    private final ProductService productService;
    private final GifticonService gifticonService;
    private final ExceptionResponse exceptionResponse;

    @PostMapping("/kakao/chatbot/add")
    public ResponseEntity<Object> addGificonByKakao(@RequestBody Map<Object, Object> gifticon) {
        String jwtToken = JwtContext.getJwtToken();

        Long userIdFromToken = userJwtTokenProvider.getUserIdFromToken(jwtToken);
        UserDto userById = userService.getUserById(userIdFromToken);

        File file = null;
        try {
            List<String> barcodeUrlList = JsonConverter.kakaoChatbotConverter(gifticon);

            for (String barcodeUrl : barcodeUrlList) {
                String barcode = GifticonService.readBarcode(barcodeUrl);
                GifticonDto gifticonDto = ocrService.readOcrUrlToGifticonDto(barcodeUrl);

                if (gifticonDto.getDue() != null) {
                    OcrUtil.checkDueDate(gifticonDto.getDue());
                }
                file = GifticonImageUtil.convertKakaoUrlToFile(barcodeUrl); // url -> File

                GifticonImageDto imageDto = imageService.saveImage(file); // File -> 서버에 저장
                gifticonDto.setBarcode(barcode);
                gifticonDto.setUser(userById);


//                gifticonDto.setUser(userService.getUserById(userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0))));
                System.out.println(gifticonDto.getUser().getId());

                GifticonStorage storage = storageService.saveStorage(gifticonDto, imageDto);
                System.out.println("sotrage_id : " + storage.getId());


            }

        } catch (NotFoundException e) { // 바코드x
            return ResponseEntity.badRequest().build();

        } catch (NotExpiredDueException e) { // 유효기간 체크
            Map<String, String> exception = exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage());
            return ResponseEntity.badRequest().body(exception);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();

        } finally {
            file.delete();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/file/add") // MultipartType으로 받는다 (1개)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> addGifticonByFile(@RequestPart MultipartFile imageFile,
                                                    @RequestHeader HttpHeaders headers) {

        File file = null;
        try {
            file = GifticonImageUtil.convert(imageFile);

            if(!GifticonImageUtil.checkInvalidFileType(file)){
                throw new NotValidFileExtensionException();
            }
            GifticonDto gifticonDto = ocrService.readOcrMultipartToGifticonDto(file); // 파일

            if (gifticonDto.getDue() != null) {
                OcrUtil.checkDueDate(gifticonDto.getDue());
            }
            GifticonImageDto imageDto = imageService.saveImage(imageFile); // 이미지 서버에 저장 및 db에 경로저장

            String barcode = GifticonService.readBarcode(imageDto.getAccessUrl());
            gifticonDto.setBarcode(barcode);
            // 받은 헤더의 jwt토큰으로부터 유저식별
            gifticonDto.setUser(userService.getUserById(userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0))));
            storageService.saveStorage(gifticonDto, imageDto);

        } catch (NotValidFileExtensionException e){ // 확장자 체크
            log.error("addGifticonByFile | " +e);
            Map<String, String> exception = exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage());
            return ResponseEntity.badRequest().body(exception);

        } catch (NotFoundException e) { // 바코드x
            log.error("addGifticonByFile | " +e);
            Map<String, String> exception = exceptionResponse.getException("barcode", "NotFound.barcode", "바코드가 존재하지 않습니다");
            return ResponseEntity.badRequest().body(exception);

        } catch (NotExpiredDueException e) { // 유효기간 체크
            log.error("addGifticonByFile | " +e);
            Map<String, String> exception = exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage());
            return ResponseEntity.badRequest().body(exception);

        } catch (Exception e) {
            log.error("addGifticonByFile | " +e);
            return ResponseEntity.badRequest().body(exceptionResponse.getException(null, "Exception", e.getMessage()));

        } finally {
            file.delete();
        }
        return ResponseEntity.ok().body(Collections.singletonMap("status", "200"));
    }

    @PostMapping("/file/add/multiple") // MultipartType으로 받는다 (여러개)
    @ResponseStatus(HttpStatus.OK)
    public List<String> addGifticonByFiles(@ModelAttribute ImageSaveDto imageSaveDto) {
//        return gifticonImageService.saveImages(imageSaveDto);
        return null;
    }

    @RequestMapping(value = "/list", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<Object> getStorageList(@RequestHeader HttpHeaders headers, @PageableDefault(size = 6) Pageable pageable) {

        try {
            UserDto userDto = userService.getUserById(userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0)));
            Page<GifticonStorageListDto> storageList = storageService.getStorageList(userDto.getId(), pageable);

            return ResponseEntity.ok(storageList);

        } catch (Exception e) {
            log.error("getStorageList | " + e);
            return ResponseEntity.badRequest().build();
        }
    }


    @PostMapping("/delete/{id}")
    public ResponseEntity<Object> removeFromStorage(@PathVariable("id") Long storageId, @RequestHeader HttpHeaders headers) {
        try {
            GifticonStorageDto storage = storageService.getStorageById(storageId);
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

            if (!storage.getUser().getId().equals(userId)) {
                ResponseEntity.badRequest().build();
            }
            imageService.deleteFileByStorage(storage);
            storageService.deleteStorage(storageId);

        } catch (Exception e) {
            log.error("removeFromStorage | " +e);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}
