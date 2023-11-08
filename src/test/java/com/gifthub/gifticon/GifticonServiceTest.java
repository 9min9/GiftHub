package com.gifthub.gifticon;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GifticonServiceTest {

    @Autowired
    GifticonService service;

    @Test
    public void 바코드를_읽는다() {
        String barcode1 = GifticonService.readBarcode("https://upload.wikimedia.org/wikipedia/commons/5/5d/UPC-A-036000291452.png");

        assertThat(barcode1).isEqualTo("036000291452");
    }

}