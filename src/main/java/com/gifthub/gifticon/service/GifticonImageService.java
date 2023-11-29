package com.gifthub.gifticon.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.gifthub.gifticon.dto.GifticonImageDto;
import com.gifthub.gifticon.dto.storage.GifticonStorageDto;
import com.gifthub.gifticon.entity.GifticonImage;
import com.gifthub.gifticon.repository.GifticonImageRepository;
import com.gifthub.gifticon.repository.storage.GifticonStorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class GifticonImageService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3Client amazonS3Client;

    private final GifticonImageRepository gifticonImageRepository;
    private final GifticonStorageRepository storageRepository;

    // TODO : 파일 여러개 넣는 기능
//    @Transactional
//    public List<String> saveImages(ImageSaveDto saveDto) {
//        List<String> resultList = new ArrayList<>();
//
//        for(MultipartFile multipartFile : saveDto.getGifticonImages()) {
//            String value = saveImage(multipartFile);
//            resultList.add(value);
//        }
//
//        return resultList;
//    }

    @Transactional
    public GifticonImageDto saveImage(MultipartFile multipartFile) {
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

        GifticonImageDto gifticonImageDto = gifticonImageRepository.save(image).toGifticonImageDto();

        return gifticonImageDto;
    }
    @Transactional
    public GifticonImageDto saveImageByFile(File file) {
        String originalName = file.getName();
        GifticonImage image = new GifticonImage(originalName);
        String filename = image.getStoreFileName();

        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType("image/jpeg");
            objectMetadata.setContentLength(file.length());

            try(InputStream inputStream = new FileInputStream(file)){
                amazonS3Client.putObject(bucketName, filename, inputStream, objectMetadata);
            }

            String accessUrl = amazonS3Client.getUrl(bucketName, filename).toString();
            image.setAccessUrl(accessUrl);
        } catch(IOException e) {
            System.out.println("서버에 이미지 저장 실패");
        }

        GifticonImageDto gifticonImageDto = gifticonImageRepository.save(image).toGifticonImageDto();

        return gifticonImageDto;
    }

    // 연쇄 삭제( Storage 삭제 -> Image 서버에서 삭제 -> db Image entity 삭제)
    public void deleteFileByStorage(GifticonStorageDto storageDto){
        GifticonImage image = gifticonImageRepository.findGifticonImageByGifticonStorage(storageDto.toStorageEntity()).orElse(null);
        if (image != null) {
            DeleteObjectRequest request = new DeleteObjectRequest(bucketName, image.getStoreFileName());
            amazonS3Client.deleteObject(request); // 서버에서 삭제
            gifticonImageRepository.delete(image); // db에서 Gifticon_image삭제
            storageRepository.delete(storageDto.toStorageEntity());// db에서 GifticonStorage 삭제
        }
    }


}
