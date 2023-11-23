package com.gifthub.gifticon.service;

import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.dto.ProductDto;
import com.gifthub.gifticon.repository.ProductRepository;
import com.gifthub.gifticon.repository.ProductRepositoryImpl;
import com.gifthub.gifticon.util.OcrUtil;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OcrService {

    @Value("${ocrSecretKey}")
    private String ocrSecretKey;

    @Value("${ocrAPIURL}")
    private String ocrAPIURL;

    private String imgTestPath = "C:/OcrPractice/";

    private final ProductRepository productRepository;
    private final ProductRepositoryImpl productRepositoryQdsl;

    public GifticonDto readOcrUrlToGifticonDto(String barcodeurl) { // dto분리
        List<String> brandNameList = productRepositoryQdsl.findAllBrandName();
        List<ProductDto> productListByBrand;
        String parsedBarcodeImg = readOcrUrl(barcodeurl);

        String dueDate = null;
        String brandName = null;
        String productName = null;

        for (String s : brandNameList) {
            if (OcrUtil.findMatchString(parsedBarcodeImg, s)) {
                brandName = s;
                productListByBrand = productRepositoryQdsl.findProductByBrand(s);
                for (ProductDto product : productListByBrand) {
                    if (OcrUtil.findMatchString(parsedBarcodeImg, product.getName())) {// 브랜드는 db에 있지만 읽어낸 상품명이 db에 없을 수 도있음!
                        productName = product.getName();
                    }
                }
            }
        }

        LocalDate due = dateFormattingByString(parsedBarcodeImg, dueDate);

        return GifticonDto.builder().due(due).productName(productName).brandName(brandName).build();

    }

    public GifticonDto readOcrMultipartToGifticonDto(String imgFile) { // dto분리
        List<String> brandNameList = productRepositoryQdsl.findAllBrandName();
        List<ProductDto> productListByBrand;
        String parsedBarcodeImg = readOcrMultipart(imgFile);

        String dueDate = null;
        String brandName = null;
        String productName = null;

        for (String s : brandNameList) {
            if (OcrUtil.findMatchString(parsedBarcodeImg, s)) {
                brandName = s;
                productListByBrand = productRepositoryQdsl.findProductByBrand(s);
                for (ProductDto product : productListByBrand) {
                    if (OcrUtil.findMatchString(parsedBarcodeImg, product.getName())) {// 브랜드는 db에 있지만 읽어낸 상품명이 db에 없을 수 도있음!
                        productName = product.getName();
                    }
                }
            }
        }
        LocalDate due = dateFormattingByString(parsedBarcodeImg, dueDate);

        return GifticonDto.builder().due(due).productName(productName).brandName(brandName).build();

    }


    private String readOcrUrl(String imageUrl) {
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

    private String readOcrMultipart(String imageFile) {
        try {
            URL url = new URL(ocrAPIURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setReadTimeout(30000);
            con.setRequestMethod("POST");
            String boundary = "----" + UUID.randomUUID().toString().replaceAll("-", "");
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            con.setRequestProperty("X-OCR-SECRET", ocrSecretKey);

            JSONObject json = new JSONObject();
            json.put("version", "V2");
            json.put("requestId", UUID.randomUUID().toString());
            json.put("timestamp", System.currentTimeMillis());
            JSONObject image = new JSONObject();
            image.put("format", "jpg");
            image.put("name", "demo");
            JSONArray images = new JSONArray();
            images.add(image);
            json.put("images", images);
            String postParams = json.toString();

            con.connect();
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            long start = System.currentTimeMillis();
            File file = new File(imgTestPath + imageFile);
            writeMultiPart(wr, postParams, file, boundary);
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
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    private void writeMultiPart(OutputStream out, String jsonMessage, File file, String boundary) throws
            IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition:form-data; name=\"message\"\r\n\r\n");
        sb.append(jsonMessage);
        sb.append("\r\n");

        out.write(sb.toString().getBytes("UTF-8"));
        out.flush();

        if (file != null && file.isFile()) {
            out.write(("--" + boundary + "\r\n").getBytes("UTF-8"));
            StringBuilder fileString = new StringBuilder();
            fileString
                    .append("Content-Disposition:form-data; name=\"file\"; filename=");
            fileString.append("\"" + file.getName() + "\"\r\n");
            fileString.append("Content-Type: application/octet-stream\r\n\r\n");
            out.write(fileString.toString().getBytes("UTF-8"));
            out.flush();

            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[8192];
                int count;
                while ((count = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, count);
                }
                out.write("\r\n".getBytes());
            }

            out.write(("--" + boundary + "--\r\n").getBytes("UTF-8"));
        }
        out.flush();
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

    private LocalDate dateFormattingByString(String parsedBarcodeImg, String dueDate) {
        if (OcrUtil.dateParserTilde(parsedBarcodeImg) != null) {
            dueDate = OcrUtil.dateParserTilde(parsedBarcodeImg);
        } else {
            dueDate = OcrUtil.dateParserHangul(parsedBarcodeImg);
        }
        dueDate = OcrUtil.dateReplaceFromSpotToHyphen(dueDate);
        LocalDate due = OcrUtil.localDateFormatter(dueDate);
        return due;
    }


}
