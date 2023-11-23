package com.gifthub.gifticon.service;

import com.gifthub.gifticon.dto.GifticonImageDto;
import com.gifthub.gifticon.repository.GifticonImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class GifticonImageService {
    private final GifticonImageRepository gifticonImageRepository;

    public GifticonImageDto saveImage(String imageUrl){
        try{
            URL url = new URL(imageUrl);
            String extension = imageUrl.substring(imageUrl.indexOf('.') + 1);

            // TODO : imageUrl 에서 원본파일 이름 추출
            String fileName = "";
            BufferedImage image = ImageIO.read(url);
            File file = new File("tempStorage/" + fileName + extension);
            if(!file.exists()){
                file.mkdir();
            }
            ImageIO.write(image, extension, file); // extension : 확장자명
            System.out.println("이미지 업로드 완료");

            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
//
//    public GifticonImageDto saveImageFile(String imageFileName){
//        try{
//            String extension = imageFileName.substring(imageFileName.indexOf('.') + 1);
//
////            BufferedImage image = ImageIO.read(url);
//            File file = new File("tempStorage/" + imageFileName + extension);
//            if(!file.exists()){
//                file.mkdir();
//            }
////            ImageIO.write(image, extension, file); // extension : 확장자명
//            System.out.println("이미지 업로드 완료");
//
//            return null;
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }


}
