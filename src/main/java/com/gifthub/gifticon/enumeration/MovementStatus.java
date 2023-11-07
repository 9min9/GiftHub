package com.gifthub.gifticon.enumeration;

import lombok.Getter;

@Getter
public enum MovementStatus {

    FAIL("fail"),
    SUCCESS("success");

    private String status;

    MovementStatus(String status) {
        this.status = status;
    }
}
