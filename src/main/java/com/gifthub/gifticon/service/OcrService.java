package com.gifthub.gifticon.service;

import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.util.OcrUtil;
import com.gifthub.product.dto.ProductDto;
import com.gifthub.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class OcrService {

    @Value("${ocrSecretKey}")
    private String ocrSecretKey;

    @Value("${ocrAPIURL}")
    private String ocrAPIURL;

    private String giftishow = "giftishow";
    private String giftishowHangul = "기프티쇼";

    private final ProductRepository productRepository;

    public GifticonDto readOcr(String imageUrl) {
        List<String> brandNameList = productRepository.loadBrandNames();
        String ocrResult = ocrGeneralAPI(imageUrl);

        log.info("OcrService | readOcr_imageUrl | 읽은 OCR 결과 : " + ocrResult);

        GifticonDto gifticonDto = findProductFromDbByBrand(ocrResult, brandNameList);
        LocalDate due = dateFormattingByString(ocrResult);

        log.info("OcrService | readOcr_imageUrl | 읽은 유효 기간 : " + due);
        gifticonDto.setDue(due);
        return gifticonDto;

    }

    public GifticonDto readOcr(File file) {
        List<String> brandNameList = productRepository.loadBrandNames();
        String ocrResult = ocrGeneralAPI(file);

        log.info("OcrService | readOcr_file | 읽은 OCR 결과 : " + ocrResult);
        log.info("OcrService | readOcr_file | 브랜드 개수 : " + brandNameList.size());
        GifticonDto gifticonDto = findProductFromDbByBrand(ocrResult, brandNameList);

        LocalDate due = dateFormattingByString(ocrResult);
        log.info("OcrService | readOcr | 읽은 유효 기간 : " + due);

        gifticonDto.setDue(due);
        return gifticonDto;

    }

    private GifticonDto findProductFromDbByBrand(String ocrResult, List<String> brandNameList) {
        String brandName = null;
        String productName = null;
        for (String s : brandNameList) {
            if (OcrUtil.findMatchString(ocrResult, s)) {
                brandName = s;
                List<ProductDto> productListByBrand = productRepository.findProductByBrand(s);
                for (ProductDto product : productListByBrand) {
//                    System.out.println("db의 상품명: "+ product.getName());
                    for (String splitedOcrResult : OcrUtil.parseStringByNewline(ocrResult)) {
//                        System.out.println("개행으로 구분된 ocr결과: "+ splitedOcrResult.strip());
                        if (OcrUtil.findMatchString(splitedOcrResult, giftishow) ||
                                OcrUtil.findMatchString(splitedOcrResult, giftishowHangul)) { // 기프티쇼의 경우
                            if (OcrUtil.parseStringByColon(splitedOcrResult).strip().compareTo(product.getName()) == 0) {
                                productName = product.getName();
                            }
                        }
                        if (splitedOcrResult.strip().compareTo(product.getName().strip()) == 0) { // 카카오톡 선물하기의 경우
                            productName = product.getName();
                        }
                    }
                }
            }
        }
        log.info("OcrService | findProductFromDbByBrand | 읽은 브랜드 이름 : " + brandName);
        log.info("OcrService | findProductFromDbByBrand | 읽은 상품 이름 : " + productName);

        return GifticonDto.builder()
                .brandName(brandName)
                .productName(productName)
                .build();
    }

    private String ocrGeneralAPI(String imageUrl) {
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
            image.put("format", "jpg");
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

            return parseOcrResult(String.valueOf(response));

        } catch (MalformedURLException e) {
            log.error("OcrService | ocrGeneralAPI | " + e);
        } catch (IOException e) {
            log.error("OcrService | ocrGeneralAPI | " + e);
        }
        return null;
    }

    private String ocrGeneralAPI(File file) {
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

            return parseOcrResult(String.valueOf(response));
        } catch (IOException e) {
            log.error("OcrService | ocrGeneralAPI | " + e);
            throw new RuntimeException();
        } catch (NullPointerException e) {
            log.error("OcrService | ocrGeneralAPI | " + e);
            throw new IllegalArgumentException("파일이 존재하지 않습니다");
        }
    }

    private void writeMultiPart(OutputStream out, String jsonMessage, File file, String boundary) throws IOException {
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
            fileString.append("Content-Disposition:form-data; name=\"file\"; filename=");
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


    private String parseOcrResult(String response) { // 개행, 스페이스 기준으로 ocrResult수정
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
            log.error("OcrService | parseOcrResult | " +e);
            throw new RuntimeException(e);
        }

    }

    private LocalDate dateFormattingByString(String ocrResult) {
        String capturedDueDate = null;
        String dueDate = null;
        if (OcrUtil.dateParserTilde(ocrResult) != null) {
            capturedDueDate = OcrUtil.dateParserTilde(ocrResult); // ~2024.02.10 ->
            dueDate = OcrUtil.dateReplaceFromSpotToHyphen(capturedDueDate);
        } else {
            capturedDueDate = OcrUtil.dateParserHangul(ocrResult);
            dueDate = OcrUtil.dateReplaceFromHangulToHyphen(OcrUtil.removeSpaces(capturedDueDate)); // 년월일 공백제거후 하이픈추가
        }
        LocalDate due = OcrUtil.localDateFormatterHyphen(dueDate);
        return due;
    }
}
