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

    public GifticonImageDto saveImage(String barcodeUrl){
        try{
            URL url = new URL(barcodeUrl);
            String extension = barcodeUrl.substring(barcodeUrl.indexOf('.') + 1);

            BufferedImage image = ImageIO.read(url);
            File file = new File("test.ico");

            ImageIO.write(image, extension, file);
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
