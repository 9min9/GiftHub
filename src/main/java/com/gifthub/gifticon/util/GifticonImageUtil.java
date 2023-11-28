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
            System.out.println("Iutil / kakaoUrl :"+ kakaoSecretUrl);
            String extension = "jpg";
            String fileName = parseImgUrlToFilename(kakaoSecretUrl);
            System.out.println("Iutil / filename: "+ fileName);

            BufferedImage image = ImageIO.read(imgURL);
            System.out.println("Iutil / bufferImage: "+ image.getPropertyNames());
            File file = new File(tempStorage + fileName + "." + extension); // 파일을 생성
            if (!file.exists()) {
                file.mkdir();
            }
            System.out.println("file: "+ file.getName());
            System.out.println("fileAbsolue: "+ file.getAbsolutePath());

            ImageIO.write(image, extension, file); // image를 file로 업로드
            return file;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MultipartFile convertToMultipart(File file){

        try {
            StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
            System.out.println("-1");
            StandardMultipartHttpServletRequest request = new StandardMultipartHttpServletRequest(null);
            System.out.println("0");

            Path tempFile = Files.createTempFile(null, null);
            Files.copy(file.toPath(), tempFile, StandardCopyOption.REPLACE_EXISTING);

//            MultipartFile multipartFile = resolver.resolveMultipart(null).getFile(file);
            MultipartFile multipartFile = request.getFile(tempFile.toString());
            System.out.println("convertToMultipart / multipartFile: " + multipartFile.getOriginalFilename());
//            InputStream inputStream = new FileInputStream(file);
//            System.out.println("1");

//            byte[] bytes = new byte[inputStream.available()];
//            System.out.println("2");
//            inputStream.read(bytes);
//
//            System.out.println("3");
//            multipartFile.getBytes();
//            System.out.println("convertToMultipart / multipartFile: "+ multipartFile.getOriginalFilename());
            return multipartFile;

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

}
