package com.gifthub.gifticon.service;

import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.entity.Gifticon;
import com.gifthub.gifticon.repository.GifticonRepository;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import lombok.RequiredArgsConstructor;
import org.hibernate.result.Output;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GifticonService {

    private final GifticonRepository gifticonRepository;

    public static String readBarcode(String url) {
        try {
            BufferedImage image = ImageIO.read(new URL(url));

            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));

            Result decoded = new MultiFormatReader().decode(bitmap);

            String barcodeNumber = decoded.getText();

            return barcodeNumber;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void writeBarcode(String str, OutputStream outputStream) {
        Code39Bean bean = new Code39Bean();

        int dpi = 150;

        bean.setWideFactor(3);
        bean.doQuietZone(true);

        try {
            //Set up the canvas provider for monochrome PNG output
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                    outputStream, "image/x-png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);

            //Generate the barcode
            bean.generateBarcode(canvas, str);

            //Signal end of generation
            canvas.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GifticonDto findGifticon(Long gifticonId) {
        return gifticonRepository.findById(gifticonId).orElseThrow().toDto();
    }

    public Long saveGifticon(GifticonDto gifticonDto){

        Gifticon gifticon = gifticonRepository.save(gifticonDto.toEntity());
        return gifticon.getId();
    }

}
