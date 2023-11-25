package com.gifthub.gifticon.service;

import com.gifthub.gifticon.dto.GifticonImageDto;
import com.gifthub.gifticon.entity.GifticonImage;
import com.gifthub.gifticon.repository.GifticonImageRepository;
import com.gifthub.gifticon.util.GifticonImageUtil;
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
        String path = "tempStorage/";

        try{
            URL url = new URL(imageUrl);
            String extension = imageUrl.substring(imageUrl.indexOf('.') + 1);
            System.out.println(extension);

            String fileName = GifticonImageUtil.parseImgUrlToFilename(imageUrl);
            System.out.println(fileName);

            BufferedImage image = ImageIO.read(url);
            File file = new File( path + fileName + extension);
            if(!file.exists()){
                file.mkdir();
            }
            ImageIO.write(image, extension, file); // extension : 확장자명
            System.out.println("이미지 업로드 완료");

            GifticonImageDto imageDto = GifticonImageDto.builder().imagePath(path).originalFileName(fileName).build();

            // DB에 저장
            saveImageRepository(imageDto);

            return imageDto;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("url 오류");
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("파일 이름 오류");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("업로드 오류");
        }

        return null;
    }

    public Long saveImageRepository(GifticonImageDto imageDto){
        GifticonImage gifticonImage = gifticonImageRepository.save(imageDto.toEntity());
        return gifticonImage.getId();
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
