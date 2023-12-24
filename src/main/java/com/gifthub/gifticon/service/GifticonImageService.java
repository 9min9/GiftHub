package com.gifthub.gifticon.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.gifthub.gifticon.dto.BarcodeImageDto;
import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.dto.GifticonImageDto;
import com.gifthub.gifticon.dto.storage.GifticonStorageDto;
import com.gifthub.gifticon.entity.BarcodeImage;
import com.gifthub.gifticon.entity.GifticonImage;
import com.gifthub.gifticon.repository.image.BarcodeImageRepository;
import com.gifthub.gifticon.repository.image.GifticonImageRepository;
import com.gifthub.gifticon.repository.storage.GifticonStorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class GifticonImageService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3Client amazonS3Client;

    private final GifticonImageRepository gifticonImageRepository;
    private final GifticonStorageRepository storageRepository;
    private final BarcodeImageRepository barcodeImageRepository;


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
            log.error("서버에 저장 실패 | "+ e);
        }

        GifticonImageDto gifticonImageDto = gifticonImageRepository.save(image).toGifticonImageDto();

        return gifticonImageDto;
    }

    public GifticonImageDto saveImage(File file) {
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
            log.error("서버에 저장 실패 | "+ e);
        }

        GifticonImageDto gifticonImageDto = gifticonImageRepository.save(image).toGifticonImageDto();

        return gifticonImageDto;
    }


    public void deleteFileByStorage(GifticonStorageDto storageDto){
        GifticonImageDto image = gifticonImageRepository.findGifticonImageByGifticonStorageId(storageDto.getId()).orElse(null);

        if (image != null) {
//            DeleteObjectRequest request = new DeleteObjectRequest(bucketName, image.getStoreFileName());
//            amazonS3Client.deleteObject(request); // 서버에서 삭제
            amazonS3Client.deleteObject(bucketName, image.getStoreFileName());
            storageRepository.delete(storageDto.toStorageEntity());// db에서 GifticonStorage 삭제
        }
    }


    public BarcodeImageDto saveBarcodeImage(File file, GifticonDto gifticonDto) {

        BarcodeImage barcodeImage = BarcodeImage.builder().gifticon(gifticonDto.toEntityWithKorCategoryName()).build();
        String filename = barcodeImage.getId() + ".png";

        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType("image/jpeg");
            objectMetadata.setContentLength(file.length());

            try(InputStream inputStream = new FileInputStream(file)){
                amazonS3Client.putObject(bucketName, filename, inputStream, objectMetadata);
            }

            String accessUrl = amazonS3Client.getUrl(bucketName, filename).toString();
            barcodeImage.setAccessUrl(accessUrl);
        } catch(IOException e) {
            log.error("서버에 저장 실패 | "+ e);
        }

        BarcodeImageDto barcodeImageDto = barcodeImageRepository.save(barcodeImage).toDto();

        return barcodeImageDto;
    }
}
