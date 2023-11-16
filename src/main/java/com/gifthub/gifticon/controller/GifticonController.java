package com.gifthub.gifticon.controller;

import com.gifthub.chatbot.util.JsonConverter;
import com.gifthub.gifticon.service.OcrService;
import com.gifthub.gifticon.service.GifticonService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class GifticonController {

    private final GifticonService service;
    private final OcrService ocrService;

    @PostMapping("/kakao/chatbot/add")
    public ResponseEntity<Object> addGificonByKakao(@RequestBody Map<Object, Object> gifticon) {

        try {
            List<String> barcodeUrlList = JsonConverter.kakaoChatbotConverter(gifticon);

            for (String barcodeUrl : barcodeUrlList) {
                String barcode = GifticonService.readBarcode(barcodeUrl);
                //todo : ocr
                String resultOcr = ocrService.readOcr(barcodeUrl);

                System.out.println(resultOcr);
                //todo : save DB
            }

        } catch (Exception e) {     //todo : url이 barcode가 아닌 경우 exception 처리하기
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
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
}
