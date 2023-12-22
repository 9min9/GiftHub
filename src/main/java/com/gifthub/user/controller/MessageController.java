package com.gifthub.user.controller;

import com.gifthub.user.dto.LocalUserDto;
import lombok.RequiredArgsConstructor;
//import net.nurigo.sdk.NurigoApp;
//import net.nurigo.sdk.message.model.Message;
//import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
//import net.nurigo.sdk.message.response.SingleMessageSentResponse;
//import net.nurigo.sdk.message.service.DefaultMessageService;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/message")
public class MessageController {


    @Value("${coolsms.api.key}")
    private  String apiKey;
    @Value("${coolsms.api.secret}")
    private String apiSecret;
    @Value("${phone.number}")
    private String masterphone;
    final DefaultMessageService messageService;

    public MessageController(){
        this.messageService= NurigoApp.INSTANCE.initialize("NCSYGCMKQ4XE3UCI","NDMX45JDHISWSGRCZIIVV9YJ7FRDBPBU" ,"https://api.coolsms.co.kr");
    }

    @PostMapping( value="/check/sendSMS")
    @ResponseBody
        SingleMessageSentResponse sendSMS(@RequestBody LocalUserDto localUserDto){
        System.out.println("tel23:"+localUserDto.getTel());
        System.out.println("test:");
        String tel=localUserDto.getTel().replace("-","");
        System.out.println("tel2:"+tel);

        Message message = new Message();
        message.setFrom(masterphone);
        message.setTo(tel);
        message.setText("https://myawsimgbucket.s3.ap-northeast-2.amazonaws.com/59c7a3ac-9b48-49de-9bf6-75f8b7022155.jpg");
        System.out.println("정상출력");
        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

        return response;


    }

}
