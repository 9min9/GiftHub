package com.gifthub.gifticon.service;

import com.gifthub.gifticon.dto.BarcodeImageDto;
import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.dto.GifticonQueryDto;
import com.gifthub.gifticon.entity.Gifticon;
import com.gifthub.gifticon.enumeration.GifticonStatus;
import com.gifthub.gifticon.repository.GifticonRepository;
import com.gifthub.gifticon.repository.image.BarcodeImageRepository;
import com.gifthub.gifticon.util.GifticonImageUtil;
import com.gifthub.user.entity.User;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GifticonService {

    @Value("${static-path-pattern}")
    private static String tempStorage;

    private final GifticonRepository gifticonRepository;
    private final BarcodeImageRepository barcodeRepository;

    public static String readBarcode(String url) throws NotFoundException{
        try {
            BufferedImage image = ImageIO.read(new URL(url));

            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));

            Result decoded = new MultiFormatReader().decode(bitmap);

            String barcodeNumber = decoded.getText();

            return barcodeNumber;
        }
        catch (IOException e) {
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
        return gifticonRepository.findById(gifticonId).orElseThrow().toDtoWithProduct();
    }

    @Transactional
    public Long saveGifticon(GifticonDto gifticonDto){
        gifticonDto.setGifticonStatus(GifticonStatus.NONE);
        Gifticon gifticon = gifticonRepository.save(gifticonDto.toEntity());
        return gifticon.getId();
    }

    @Transactional
    public Long saveGifticonWithKorCategoryName(GifticonDto gifticonDto){
        gifticonDto.setGifticonStatus(GifticonStatus.NONE);
        Gifticon gifticon = gifticonRepository.save(gifticonDto.toEntityWithKorCategoryName());
        return gifticon.getId();
    }

    public List<GifticonDto> getMyGifticon(User user){
        return gifticonRepository.findByUser(user).stream().map(Gifticon::toDto).toList();
    }


    public Page<GifticonQueryDto> getPurchasingGifticon(Pageable pageable, String type) {
        return gifticonRepository.findByGifticonStatusIsOnSale(pageable, type).map(Gifticon::toQueryDto);
    }

    public boolean productNameIsNull(String productName) {
        if (productName.isEmpty() || productName == null) {
            return true;
        }
        return false;
    }

    public boolean brandNameIsNull(String brandName) {
        if (brandName.isEmpty() || brandName == null) {
            return true;
        }
        return false;
    }

    public boolean barcodeIsNull(String barcode) {
        if(barcode.isEmpty() || barcode == null) {
            return true;
        }
        return false;
    }

    public boolean dueIsNull(LocalDate due) {
        if (due == null) {
            return true;
        }
        return false;
    }

    public Page<GifticonDto> getGifticonByUserId(Pageable pageable, Long userId) {
        return gifticonRepository.findByUserId(pageable, userId).map(Gifticon::toDtoWithProduct);
    }

    public void deleteById(Long gifticonId) {
        gifticonRepository.deleteById(gifticonId);
    }

    @Transactional
    public Long setSale(Long gifticonId) throws RuntimeException {
        Long updated = gifticonRepository.updateSaleByGifticonId(gifticonId);

        if (updated == 1) {
            return updated;
        } else if (updated > 1) {
            throw new RuntimeException("한 개 이상의 수정사항이 있습니다.");
        } else {
            throw new RuntimeException("수정사항이 없습니다.");
        }
    }

    @Transactional
    public Long notForSale(Long gifticonId) {
        Long updated = gifticonRepository.updateNoneByGifticonId(gifticonId);

        if(updated == 1){
            return updated;
        } else {
            return (long) -1;
        }
    }
    @Transactional
    public Long setUsed(Long gifticonId) {
        Long updated = gifticonRepository.updateFinishedByGifticonId(gifticonId);
        if(updated == 1){
            return updated;
        } else {
            return (long) -1;
        }
    }

    public Page<GifticonDto> getGifticonByProudctId(Pageable pageable, Long productId) {
        return gifticonRepository.findGifticonByProductIdOrderByProductPrice(pageable, productId).map(Gifticon::toDtoWithProduct);
    }

    public Long changePrice(Long gifticonId) {

        return null;
    }

    public File getBarcodeImage(Long gifticonId, int width, int height){
        GifticonDto gifticonDto = findGifticon(gifticonId);
//        byte[] bacodeArr = GifticonImageUtil.getBarcodeImage(gifticonDto.getBarcode(), width, height);
        String filePath = tempStorage + gifticonId + "." + "png";
        return GifticonImageUtil.generateBarcodeImageFile(gifticonDto.getBarcode(), width, height, filePath);

    }

    public BarcodeImageDto findBarcodeImage(Long gifticonId){

        return barcodeRepository.findBarcodeImageByGifticon_Id(gifticonId).orElse(null).toDto();
    }

}
