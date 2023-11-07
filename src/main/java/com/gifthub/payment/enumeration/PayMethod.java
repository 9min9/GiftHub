package com.gifthub.payment.enumeration;

import lombok.Getter;

@Getter
public enum PayMethod {

    CARD("card"),
    MONEY("money");

    private final String method;

    PayMethod(String method) {
        this.method = method;
    }

}
