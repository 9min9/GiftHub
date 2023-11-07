package com.gifthub.payment.enumeration;

import lombok.Getter;

@Getter
public enum PayStatus {

    PAYING("paying"),
    PAID("paid");

    String status;

    PayStatus(String status) {
        this.status = status;
    }

}
