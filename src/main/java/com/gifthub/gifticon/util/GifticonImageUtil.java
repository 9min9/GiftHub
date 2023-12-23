package com.gifthub.gifticon.util;

import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.exception.NotValidFileExtensionException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Slf4j
public class GifticonImageUtil {

    @Value("${static-path-pattern}")
    private static String tempStorage;

    private static String[] imageExtensions = {"jpg", "jpeg", "png", "pdf", "tiff"};

    public static String parseImgUrlToFilename(String imgUrl) {
        try {
            Matcher matcher = Pattern.compile("=&signature=(.*?)$").matcher(imgUrl);
            return matcher.find() ? matcher.group(1) : null;

        } catch (PatternSyntaxException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static File convert(MultipartFile multipartFile) throws IOException {
        File convFile = new File(tempStorage + multipartFile.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }

    public static File convertKakaoUrlToFile(String kakaoSecretUrl) {
        try {
            URL imgURL = new URL(kakaoSecretUrl);
            String extension = "jpg";
            String fileName = parseImgUrlToFilename(kakaoSecretUrl);

            BufferedImage image = ImageIO.read(imgURL);
            File file = new File(tempStorage + fileName + "." + extension); // 파일을 생성
            if (!file.exists()) {
                file.mkdir();
            }

            ImageIO.write(image, extension, file); // image를 file로 업로드
            return file;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 확장자가 이미지타입인지 체크
    public static boolean checkInvalidFileType(File file){
        String extension = getFileExtension(file.getName());

        for(String ext : imageExtensions){
            if(extension.equals(ext)){
                return true;
            }
        }
        return false;

    }

    public static String getFileExtension(String originalFileName){
        int index = originalFileName.lastIndexOf('.');

        if(index == -1 || index == originalFileName.length() -1){
            return "";
        }

        return originalFileName.substring(index+1);
    }

    public static File barcodeArrToFile(byte[] barcodeArr, String filePath){
        try {
            File file = new File(filePath);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(barcodeArr);
            fos.close();
            return file;

        } catch (IOException e){
            log.error("barcodeArrToFile" +
                    " | "+ e);
            return null;
        }
    }

    public static byte[] getBarcodeImage(String text, int width, int height){
        try {

            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            Writer writer = new Code128Writer();
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.CODE_128, width, height);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();

        } catch (WriterException | IOException e) {
            log.error("getBarcodeImage | "+e);
            return null;
        }
    }



}
