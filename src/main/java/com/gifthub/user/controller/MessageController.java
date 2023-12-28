package com.gifthub.user.controller;

import com.gifthub.user.dto.LocalUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.model.StorageType;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/message")
@Slf4j
public class MessageController {

    @Value("${coolsms.api.key}")
    private String apiKey;
    @Value("${coolsms.api.secret}")
    private String apiSecret;
    @Value("${phone.number}")
    private String masterphone;
    final DefaultMessageService messageService;

    public MessageController() {
        this.messageService = NurigoApp.INSTANCE.initialize("NCSYGCMKQ4XE3UCI", "NDMX45JDHISWSGRCZIIVV9YJ7FRDBPBU", "https://api.coolsms.co.kr");
    }

    @PostMapping(value = "/check/sendSMS")
    @ResponseBody
    SingleMessageSentResponse sendSMS(@RequestBody LocalUserDto localUserDto) throws IOException {
        String path ="static/images/categorylogo/test.jpg";

        //path 값에 이미지 path 작성하면 됩니다. 저장되있는 이미지만 보내는게 가능하고, 인터넷 주소에서 복사한 이미지로는 안됩니다.
        ClassPathResource resource = new ClassPathResource(path);
        File file = resource.getFile();
        String imageId = messageService.uploadFile(file, StorageType.MMS, null);
        String tel = localUserDto.getTel().replace("-", "");

        Message message = new Message();
        message.setSubject("제목");
        message.setFrom(masterphone);
        message.setTo(tel);
        message.setImageId(imageId);
        message.setText("텍스트넣어주세요");
        log.info("MessageController | sendSMS | 정상 출력");
        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

        return response;


    }

}
