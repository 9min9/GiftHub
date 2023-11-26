package com.gifthub.gifticon.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class GifticonImageUtil {

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
        File convFile = new File(multipartFile.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }
}
