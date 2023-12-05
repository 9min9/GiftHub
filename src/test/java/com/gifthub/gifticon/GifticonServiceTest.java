package com.gifthub.gifticon;

import com.gifthub.gifticon.service.GifticonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
class GifticonServiceTest {

    @Autowired
    GifticonService service;

    @Test
    public void 바코드를_읽는다() {
//        String barcode1 = GifticonService.readBarcode("https://upload.wikimedia.org/wikipedia/commons/5/5d/UPC-A-036000291452.png");

//        assertThat(barcode1).isEqualTo("036000291452");
    }

}