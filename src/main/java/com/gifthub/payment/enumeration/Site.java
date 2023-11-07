package com.gifthub.payment.enumeration;

import lombok.Getter;

@Getter
public enum Site {

    KAKAO("kakao");

    private String site;

    Site(String site) {
        this.site = site;
    }

}
