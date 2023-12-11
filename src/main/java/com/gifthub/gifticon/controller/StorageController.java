package com.gifthub.gifticon.controller;

import com.gifthub.chatbot.util.JsonConverter;
import com.gifthub.config.jwt.JwtContext;
import com.gifthub.exception.InvalidDueDate;
import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.dto.GifticonImageDto;
import com.gifthub.gifticon.dto.GifticonStorageListDto;
import com.gifthub.gifticon.dto.ImageSaveDto;
import com.gifthub.gifticon.dto.storage.GifticonStorageDto;
import com.gifthub.gifticon.entity.GifticonStorage;
import com.gifthub.gifticon.exception.NotEmptyBrandnameException;
import com.gifthub.gifticon.exception.NotEmptyDueException;
import com.gifthub.gifticon.exception.NotEmptyPriceException;
import com.gifthub.gifticon.service.GifticonImageService;
import com.gifthub.gifticon.service.GifticonService;
import com.gifthub.gifticon.service.GifticonStorageService;
import com.gifthub.gifticon.service.OcrService;
import com.gifthub.gifticon.util.GifticonImageUtil;
import com.gifthub.gifticon.util.OcrUtil;
import com.gifthub.product.dto.ProductDto;
import com.gifthub.product.enumeration.CategoryName;
import com.gifthub.product.service.ProductService;
import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.dto.UserDto;
import com.gifthub.user.service.UserService;
import com.google.zxing.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/storage")
public class StorageController {
    private final GifticonStorageService storageService;
    private final GifticonImageService imageService;
    private final UserJwtTokenProvider userJwtTokenProvider;
    private final UserService userService;
    private final OcrService ocrService;
    private final ProductService productService;
    private final GifticonService gifticonService;

    @PostMapping("/kakao/chatbot/add")
    public ResponseEntity<Object> addGificonByKakao(@RequestBody Map<Object, Object> gifticon) {
        String jwtToken = JwtContext.getJwtToken();
        System.out.println("@@@@ add KAKa");
        System.out.println(jwtToken);
        System.out.println(gifticon);

        Long userIdFromToken = userJwtTokenProvider.getUserIdFromToken(jwtToken);
        UserDto userById = userService.getUserById(userIdFromToken);
        System.out.println(userById.getId());
        System.out.println(userById.getName());

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

                GifticonImageDto imageDto = imageService.saveImageByFile(file); // File -> 서버에 저장
                gifticonDto.setBarcode(barcode);
                gifticonDto.setUser(userById);


//                gifticonDto.setUser(userService.getUserById(userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0))));
                System.out.println(gifticonDto.getUser().getId());

                GifticonStorage storage = storageService.saveStorage(gifticonDto, imageDto);
                System.out.println("sotrage_id : " + storage.getId());


            }

        } catch (NotFoundException e) { // 바코드x
            return ResponseEntity.badRequest().build();

        } catch (InvalidDueDate e) { // 유효기간 체크
            return ResponseEntity.badRequest().build();

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

            GifticonDto gifticonDto = ocrService.readOcrMultipartToGifticonDto(file); // 파일

            // TODO : 유효기간이 지났는지 check -> 사용자 예외
            if (gifticonDto.getDue() != null) {
                OcrUtil.checkDueDate(gifticonDto.getDue());
            }
            GifticonImageDto imageDto = imageService.saveImage(imageFile); // 이미지 서버에 저장 및 db에 경로저장

            String barcode = GifticonService.readBarcode(imageDto.getAccessUrl());
            gifticonDto.setBarcode(barcode);
            // 받은 헤더의 jwt토큰으로부터 유저식별
            gifticonDto.setUser(userService.getUserById(userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0))));

            storageService.saveStorage(gifticonDto, imageDto);

        } catch (NotFoundException e) { // 바코드x
            return ResponseEntity.badRequest().build();

        } catch (InvalidDueDate e) { // 유효기간 체크
            return ResponseEntity.badRequest().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();

        } finally {
            file.delete();
        }
        return ResponseEntity.ok().body(Collections.singletonMap("status", "ok"));
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
            e.printStackTrace();

            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/gifticon/register") // db에 있는경우
    public ResponseEntity<Object> registerGifticon(@RequestBody Map<String, String> request,
                                                   @RequestHeader HttpHeaders headers) {
        Map<String, String> result = new HashMap<>();
        Map<String, String> errorResult = new HashMap<>();

        long storageId = Long.parseLong(request.get("storageId"));
        String due = request.get("due");

        try {
            /** product namedl 없는 사안은 admin으로 이관됨*/
            if (request.get("due") == null || request.get("due").isEmpty()) {
                throw new NotEmptyDueException();
            }

            if (request.get("brandName") == null || request.get("brandName").isEmpty()) {
                throw new NotEmptyBrandnameException();
            }

            if (request.get("price") == null || request.get("price").isEmpty()) {
                throw new NotEmptyPriceException();
            }

            GifticonStorageDto storage = storageService.getStorageById(storageId);
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));
            UserDto findUser = userService.getUserById(userId);
            storage.setUser(findUser);
            storage.setDue(LocalDate.parse(due));

            ProductDto product = productService.getByProductName(storage.getProductName());
            CategoryName engCategory = CategoryName.ofKor(product.getCategory());
            product.setCategory(engCategory.name());

            GifticonDto gifticonDto = storage.toGifticonDto(product);

            gifticonService.saveGifticon(gifticonDto);
            storageService.deleteStorage(storage.getId());

            result.put("status", "success");

        } catch (NotEmptyDueException e) {
            errorResult.put("status", e.getStatus());
            errorResult.put("code", "not.empty");
            errorResult.put("field", "due");
            errorResult.put("message", "유효기간을 입력해주세요.");
            return ResponseEntity.badRequest().body(errorResult);
        } catch (NotEmptyBrandnameException e) {
            errorResult.put("status", e.getStatus());
            errorResult.put("code", "not.empty");
            errorResult.put("field", "brandName");
            errorResult.put("message", "브랜드 이름을 입력해주세요.");
            return ResponseEntity.badRequest().body(errorResult);
        } catch (NotEmptyPriceException e) {
            errorResult.put("status", e.getStatus());
            errorResult.put("code", "not.empty");
            errorResult.put("field", "price");
            errorResult.put("message", "가격을 입력해주세요.");
            return ResponseEntity.badRequest().body(errorResult);
        } catch (Exception e) {
            errorResult.put("status", "error");
            errorResult.put("message", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(errorResult);
        }

        return ResponseEntity.ok().body(result);
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
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}
