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
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.http.entity.ContentType;
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
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
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

                GifticonImageDto imageDto = gifticonImageService.saveImageByFile(file); // File -> 서버에 저장
                gifticonDto.setBarcode(barcode);
                gifticonDto.setUser(userById);


//                gifticonDto.setUser(userService.getUserById(userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0))));
                System.out.println(gifticonDto.getUser().getId());

                GifticonStorage storage = gifticonStorageService.saveStorage(gifticonDto, imageDto);
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

    @PostMapping("/gifticon/file/add") // MultipartType으로 받는다 (1개)
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
            GifticonImageDto imageDto = gifticonImageService.saveImage(imageFile); // 이미지 서버에 저장 및 db에 경로저장

            String barcode = GifticonService.readBarcode(imageDto.getAccessUrl());
            gifticonDto.setBarcode(barcode);
            // 받은 헤더의 jwt토큰으로부터 유저식별
            gifticonDto.setUser(userService.getUserById(userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0))));

            gifticonStorageService.saveStorage(gifticonDto, imageDto);

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

    @PostMapping("/gifticon/file/add/multiple") // MultipartType으로 받는다 (여러개)
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

            response.setContentType(ContentType.IMAGE_PNG.getMimeType());
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
            UserDto userDto = userService.getUserById(userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0)));
            Page<GifticonStorageListDto> storageList = gifticonStorageService.getStorageList(userDto.getId(), pageable);

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

            GifticonStorageDto storage = gifticonStorageService.getStorageById(storageId);
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));
            UserDto findUser = userService.getUserById(userId);
            storage.setUser(findUser);
            storage.setDue(LocalDate.parse(due));

            ProductDto product = productService.getByProductName(storage.getProductName());
            CategoryName engCategory = CategoryName.ofKor(product.getCategory());
            product.setCategory(engCategory.name());

            GifticonDto gifticonDto = storage.toGifticonDto(product);

            gifticonService.saveGifticon(gifticonDto);
            gifticonStorageService.deleteStorage(storage.getId());

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

    @GetMapping("/gifticons")
    public ResponseEntity<Object> gifticonDtos(Pageable pageable, @RequestHeader HttpHeaders headers) {
        try {
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

            Page<GifticonDto> gifticons = gifticonService.getGifticonByUserId(pageable, userId);

            return ResponseEntity.ok(gifticons);
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/gifticon/delete/{gifticonId}")
    public ResponseEntity<Object> deleteGifticon(@PathVariable("gifticonId") Long gifticonId,
                                                 @RequestHeader HttpHeaders headers) {
        Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

        GifticonDto gifticonDto = gifticonService.findGifticon(gifticonId);

        gifticonService.deleteById(gifticonId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/gifticon/forSale/{gifticonId}")
    public ResponseEntity<Object> setSale(@PathVariable("gifticonId") Long gifticonId,
                                          @RequestHeader HttpHeaders headers) {
        Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

        GifticonDto gifticonDto = gifticonService.findGifticon(gifticonId);

        try {
            gifticonService.setSale(gifticonId);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("/gifticon/products/{productId}")
    public ResponseEntity<Object> findGifticonByProudctId(Pageable pageable,
                                                     @PathVariable("productId") Long productId) {
        try {
            Page<GifticonDto> gifticons = gifticonService.getGifticonByProudctId(pageable, productId);

            return ResponseEntity.ok(gifticons);
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.badRequest().build();
        }
    }

}



