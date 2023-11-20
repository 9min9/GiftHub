package com.gifthub.gifticon.service;

import static com.gifthub.gifticon.constant.OcrField.DUEDATE;
import static com.gifthub.gifticon.constant.OcrField.PRODUCTNAME;
import static com.gifthub.gifticon.constant.OcrField.brandName;

import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.dto.ProductDto;
import com.gifthub.gifticon.entity.Product;
import com.gifthub.gifticon.repository.ProductRepository;
import com.gifthub.gifticon.util.OcrUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OcrService {

    @Value("${ocrSecretKey}")
    private String ocrSecretKey;

    @Value("${ocrAPIURL}")
    private String ocrAPIURL;

    private final ProductRepository productRepository;

    public GifticonDto readOcrToGifticonDto(String barcodeurl) {
        List<ProductDto> productList = productRepository.findAllProduct();

        String parsedBarcodeImg = readOcr(barcodeurl);

        String dueDate = null;
        String brandName = null;
        String productName = null;

        for (ProductDto productDto : productList) {
            if (OcrUtil.findMatchString(parsedBarcodeImg, productDto.getName())) {
                productName = productDto.getName();
            }
            if (OcrUtil.findMatchString(parsedBarcodeImg, productDto.getBrandName())) {
                brandName = productDto.getBrandName();
            }
        }

//    OcrUtil.checkBrandInDb(productName);

        if (OcrUtil.dateParserTilde(parsedBarcodeImg) != null) {
            dueDate = OcrUtil.dateParserTilde(parsedBarcodeImg);
        } else {
            dueDate = OcrUtil.dateParserHangul(parsedBarcodeImg);
        }
        LocalDate due = OcrUtil.localDateFormatter(dueDate);

        return GifticonDto.builder().due(due).productName(productName).brandName(brandName).build();

    }


    private String readOcr(String imageUrl) {
        try {
            URL url = new URL(ocrAPIURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            con.setRequestProperty("X-OCR-SECRET", ocrSecretKey);

            JSONObject json = new JSONObject();
            json.put("version", "V2");
            json.put("requestId", UUID.randomUUID().toString());
            json.put("timestamp", System.currentTimeMillis());
            JSONObject image = new JSONObject();
            image.put("format", "jpg");     // jpg가 아닌 다른타입일 경우는 어떻게?
            // imageUrl이 들어가는부분
            image.put("url", imageUrl);

            image.put("name", "demo");
            JSONArray images = new JSONArray();
            images.add(image);
            json.put("images", images);
            String postParams = json.toString();

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            return parseOcr(String.valueOf(response));


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String parseOcr(String response) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonResponse = (JSONObject) parser.parse(response);

            JSONArray images = (JSONArray) jsonResponse.get("images");
            JSONObject objImage1 = (JSONObject) images.get(0);

            StringBuilder sb = new StringBuilder();
            JSONArray fields = (JSONArray) objImage1.get("fields");
            for (int i = 0; i < fields.size(); i++) {
                JSONObject eachResult = (JSONObject) fields.get(i);
                String inferText = (String) eachResult.get("inferText");
                Boolean lineBreak = (Boolean) eachResult.get("lineBreak");
                sb.append(inferText);
                if (lineBreak) {
                    sb.append("\n");
                }
                if (!lineBreak) {
                    sb.append(" ");
                }
            }
            return sb.toString();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }


}
