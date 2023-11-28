package com.gifthub.gifticon.enumeration;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum GifticonStatus {
//    BEFOREINSPECTION("beforeInspection"),
    NONE("notOnSale"),
    ONSALE("onSale"),
    FINISHED("finished"),
    EXPIRED("expired");

    private final String statusCode;

    GifticonStatus(String statusCode) {
        this.statusCode = statusCode;
    }

}
