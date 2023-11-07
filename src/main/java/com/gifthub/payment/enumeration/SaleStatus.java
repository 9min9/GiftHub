package com.gifthub.payment.enumeration;

import lombok.Getter;

@Getter
public enum SaleStatus {

    NONE("none"),
    SALE("sale"),
    FINISHED("finished");

    private final String statusCode;

    SaleStatus(String statusCode) {
        this.statusCode = statusCode;
    }

}
