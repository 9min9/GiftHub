package com.gifthub.gifticon.controller;

import com.gifthub.gifticon.dto.storage.GifticonStorageDto;
import com.gifthub.gifticon.entity.GifticonStorage;
import com.gifthub.gifticon.service.GifticonImageService;
import com.gifthub.gifticon.service.GifticonStorageService;
import com.gifthub.user.UserJwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/storage")
public class StorageController {
    private final GifticonStorageService storageService;
    private final GifticonImageService imageService;
    private final UserJwtTokenProvider userJwtTokenProvider;


    @PostMapping("/delete/{id}")
    public ResponseEntity<Object> removeFromStorage(@PathVariable("id") Long storageId,
                                                    @RequestHeader HttpHeaders headers){
        try{
            GifticonStorageDto storage = storageService.getStorageById(storageId);
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

            if (!storage.getUser().getId().equals(userId)) {

                ResponseEntity.badRequest().build();
            }

            imageService.deleteFileByStorage(storage);

        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();

    }


}
