package com.gifthub.gifticon.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.gifthub.gifticon.dto.GifticonImageDto;
import com.gifthub.gifticon.dto.ImageSaveDto;
import com.gifthub.gifticon.entity.GifticonImage;
import com.gifthub.gifticon.repository.GifticonImageRepository;
import com.gifthub.gifticon.util.GifticonImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GifticonImageService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3Client amazonS3Client;

    private final GifticonImageRepository gifticonImageRepository;

//    public GifticonImageDto saveImage(String imageUrl){
//        String path = "tempStorage/";
//
//        try{
//            URL url = new URL(imageUrl);
//            String extension = imageUrl.substring(imageUrl.indexOf('.') + 1);
//            System.out.println(extension);
//
//            String fileName = GifticonImageUtil.parseImgUrlToFilename(imageUrl);
//            System.out.println(fileName);
//
//            BufferedImage image = ImageIO.read(url);
//            File file = new File( path + fileName + extension);
//            if(!file.exists()){
//                file.mkdir();
//            }
//            ImageIO.write(image, extension, file); // extension : 확장자명
//            System.out.println("이미지 업로드 완료");
//
//            GifticonImageDto imageDto = GifticonImageDto.builder().accessUrl(path).originalFileName(fileName).build();//
//
//            // DB에 저장
//            saveImageRepository(imageDto);
//
//            return imageDto;
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            System.out.println("url 오류");
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//            System.out.println("파일 이름 오류");
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("업로드 오류");
//        }
//
//        return null;
//    }
//
//    public Long saveImageRepository(GifticonImageDto imageDto){
//        GifticonImage gifticonImage = gifticonImageRepository.save(imageDto.toEntity());
//        return gifticonImage.getId();
//    }


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

    @Transactional
    public List<String> saveImages(ImageSaveDto saveDto) {
        List<String> resultList = new ArrayList<>();

        for(MultipartFile multipartFile : saveDto.getGifticonImages()) {
            String value = saveImage(multipartFile);
            resultList.add(value);
        }

        return resultList;
    }

    @Transactional
    public String saveImage(MultipartFile multipartFile) {
        String originalName = multipartFile.getOriginalFilename();
        GifticonImage image = new GifticonImage(originalName);
        String filename = image.getStoreFileName();

        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());
            objectMetadata.setContentLength(multipartFile.getInputStream().available());

            amazonS3Client.putObject(bucketName, filename, multipartFile.getInputStream(), objectMetadata);

            String accessUrl = amazonS3Client.getUrl(bucketName, filename).toString();
            image.setAccessUrl(accessUrl);
        } catch(IOException e) {
            System.out.println("서버에 이미지 저장 실패");
        }

        gifticonImageRepository.save(image);

        return image.getAccessUrl();
    }


}
