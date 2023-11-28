package com.gifthub.gifticon.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class GifticonImageUtil {

    @Value("${static-path-pattern}")
    private static String tempStorage;

    public static String parseImgUrlToFilename(String imgUrl) {
        try {
            Matcher matcher = Pattern.compile("=&signature=(.*?)$").matcher(imgUrl);
            return matcher.find() ? matcher.group(1) : null;

        } catch (PatternSyntaxException e) {
            e.printStackTrace();
            System.out.println("imgUrl 파싱 에러");
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

    // TODO : 확장자가 이미지타입인지 체크
    public static void checkInvalidFileType(String fileName){

    }



}
