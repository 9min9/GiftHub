package com.gifthub.payment.enumeration;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum SaleStatus {

    NONE("none"),
    ONSALE("onsale"),
    FINISHED("finished");

    private final String statusCode;

    SaleStatus(String statusCode) {
        this.statusCode = statusCode;
    }

}
