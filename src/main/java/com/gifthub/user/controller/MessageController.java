package com.gifthub.user.controller;

import com.gifthub.gifticon.dto.BarcodeImageDto;
import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.dto.GifticonMessageDto;
import com.gifthub.gifticon.service.GifticonImageService;
import com.gifthub.gifticon.service.GifticonService;
import com.gifthub.global.exception.ExceptionResponse;
import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.dto.LocalUserDto;
import com.gifthub.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.model.StorageType;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
public class MessageController {

    @Value("${coolsms.api.key}")
    private String apiKey;
    @Value("${coolsms.api.secret}")
    private String apiSecret;
    @Value("${phone.number}")
    private String masterphone;
    final DefaultMessageService messageService = NurigoApp.INSTANCE.initialize("NCSYGCMKQ4XE3UCI", "NDMX45JDHISWSGRCZIIVV9YJ7FRDBPBU", "https://api.coolsms.co.kr");


    private final GifticonImageService imageService;
    private final GifticonService gifticonService;
    private final UserJwtTokenProvider userJwtTokenProvider;
    private final UserService userService;
    private final ExceptionResponse exceptionResponse;


    @PostMapping(value = "/check/sendSMS")
    @ResponseBody
    SingleMessageSentResponse sendSMS(@RequestBody LocalUserDto localUserDto) throws IOException {
        String path = "static/images/categorylogo/test.jpg";

        ClassPathResource resource = new ClassPathResource(path);
        File file = resource.getFile();
        String imageId = messageService.uploadFile(file, StorageType.MMS, null);
        String tel = localUserDto.getTel().replace("-", "");

        Message message = new Message();
        message.setSubject("제목");
        message.setFrom(masterphone);
        message.setTo(tel);
        message.setImageId(imageId);
        message.setText("https://myawsimgbucket.s3.ap-northeast-2.amazonaws.com/59c7a3ac-9b48-49de-9bf6-75f8b7022155.jpg");
        log.info("MessageController | sendSMS | 정상 출력");
        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

        return response;


    }


    @PostMapping("/gifticon/use/{gifticonId}")
    public ResponseEntity<Object> useMyGifticon(@PathVariable("gifticonId") Long gifticonId,
                                                @RequestHeader HttpHeaders headers) throws IOException {

        File file = null;

        try {
            GifticonDto gifticon = gifticonService.findGifticon(gifticonId);
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));
            String tel = userService.getUserById(userId).getTel();
            log.error("tel | useMyGifticonTest : " + tel);


            if (!gifticon.getUser().getId().equals(userId)) {
                ResponseEntity.badRequest().build();
            }
            int width = 200;
            int height = 67;

            file = gifticonService.getBarcodeImage(gifticon.getId(), width, height);

            BarcodeImageDto barcodeImage = imageService.saveBarcodeImage(file, gifticon);

            Message message = new Message();
            message.setSubject("GiftHub");
            message.setFrom(masterphone);
            message.setTo(tel);
//          message.setText(barcodeImage.getAccessUrl());

            GifticonMessageDto gifticonMessageDto = gifticonService.findGifticonMessageDtoByGifticonId(gifticonId);

            message.setText("사용신청한 기프티콘 : "
                    + gifticonMessageDto.getProductName()
                    + " 입니다.\n교환처 " + gifticonMessageDto.getBrand()
                    + "\n바코드: " + gifticonMessageDto.getBarcode()
                    + "\n유효기간 : ~" + gifticonMessageDto.getDue());

            String path = "null" + gifticonId + ".jpg";

            File SmsFile = new File(path);
            File dummyFile = new File("null" + gifticonId);

            String imageId = messageService.uploadFile(SmsFile, StorageType.MMS, null);

            message.setImageId(imageId);

            messageService.send(message);


            System.gc();
            SmsFile.delete();
            dummyFile.delete();

        } catch (NurigoMessageNotReceivedException e) {
            log.error("NurigoMessageNotReceivedException | " + e);
            Map<String, String> exception = exceptionResponse.getException("barcode", "Failure.message", e.getMessage());
            return ResponseEntity.badRequest().body(exception);

        } catch (Exception e) {
            log.error("useMyGifticonTest | " + e);
            return ResponseEntity.badRequest().body(exceptionResponse.getException(null, "Exception", e.getMessage()));

        } finally {

            gifticonService.setUsed(gifticonId);
        }
        return ResponseEntity.ok().body(Collections.singletonMap("status", "200"));

    }

    @PostMapping("/gifticon/reUse/{gifticonId}")
    public ResponseEntity<Object> reUseMyGifticon(@PathVariable("gifticonId") Long gifticonId,
                                                  @RequestHeader HttpHeaders headers) {
        File file = null;
        try {
            GifticonDto gifticon = gifticonService.findGifticon(gifticonId);
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));
            String tel = userService.getUserById(userId).getTel();
            log.error("tel | useMyGifticonTest : " + tel);


            if (!gifticon.getUser().getId().equals(userId)) {
                ResponseEntity.badRequest().build();
            }

            BarcodeImageDto barcodeImage = gifticonService.findBarcodeImage(gifticonId);
            log.error("reUseMyGifticon |" + barcodeImage.getId());
            Message message = new Message();
            message.setFrom(masterphone);
            message.setTo(tel);
            message.setText(barcodeImage.getAccessUrl());
            log.error("reUseMyGifticon |" + barcodeImage.getAccessUrl());

            messageService.send(message);

        } catch (NurigoMessageNotReceivedException e) {
            log.error("NurigoMessageNotReceivedException | " + e);
            Map<String, String> exception = exceptionResponse.getException("barcode", "Failure.message", e.getMessage());
            return ResponseEntity.badRequest().body(exception);

        } catch (Exception e) {
            log.error("reUseMyGifticon | " + e);
            return ResponseEntity.badRequest().body(exceptionResponse.getException(null, "Exception", e.getMessage()));

        } finally {
            if (file != null) {
//                file.delete();
            }
        }
        return ResponseEntity.ok().body(Collections.singletonMap("status", "200"));

    }

}
