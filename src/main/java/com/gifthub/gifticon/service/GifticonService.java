package com.gifthub.gifticon.service;

import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.dto.GifticonQueryDto;
import com.gifthub.gifticon.entity.Gifticon;
import com.gifthub.gifticon.enumeration.GifticonStatus;
import com.gifthub.gifticon.repository.GifticonRepository;
import com.gifthub.user.entity.User;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import lombok.RequiredArgsConstructor;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GifticonService {

    private final GifticonRepository gifticonRepository;

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

    // TODO : 특정 product_id를 인자로 받아 GifticonList를 return
    public Page<GifticonDto> getGifticonByProduct(Pageable pageable, Long productId){

        return gifticonRepository.findByProduct(pageable, productId).map(Gifticon::toDto);
    }

    public Page<GifticonQueryDto> getPurchasingGifticon(Pageable pageable, String type) {
        return gifticonRepository.findByGifticonStatusIsOnSale(pageable, type).map(Gifticon::toQueryDto);
    }

//    public List<String> getGifticonBrandName(ProductName productName) {
//        return gifticonRepository.findBrandNameByCategory(category);
//    }


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

    public Page<GifticonDto> getGifticonByProudctId(Pageable pageable, Long productId) {
        return gifticonRepository.findGifticonByProductIdOrderByProductPrice(pageable, productId).map(Gifticon::toDtoWithProduct);
    }

}
