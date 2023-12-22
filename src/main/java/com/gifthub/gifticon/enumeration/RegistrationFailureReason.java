package com.gifthub.gifticon.enumeration;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum RegistrationFailureReason {

    /**
     * code     error           message
     * 0        NOT_CANCELLED
     * 1.       REQUIRED        존재하지 않음
     * 2.       MISMATCHED      일치하지 않음
     * 3.       INAPPROPRIATE   적합하지 않음
     *
     * */

    NOT_CANCELLED("", 0, ""),

    PRODUCT_NAME_REQUIRED("productName", 1, "상품 이름이 존재하지 않습니다"),
    PRODUCT_NAME_MISMATCHED("productName", 2, "상품 이름이 일치하지 않습니다"),
    PRODUCT_NAME_INAPPROPRIATE("productName", 3, "상품 이름이 적합하지 않습니다"),

    BRAND_REQUIRED("brand", 1, "브랜드가 존재하지 않습니다"),
    BRAND_MISMATCHED("brand", 2, "브랜드가 일치하지 않습니다"),
    BRAND_INAPPROPRIATE("brand", 3, "브랜드가 적합하지 않습니다"),

    BARCODE_Required("barcode", 1, "바코드 값이 존재하지 않습니다"),
    BARCODE_MISMATCHED("barcode", 2, "바코드 값이 일치하지 않습니다"),
    BARCODE_INAPPROPRIATE("barcode", 3, "바코드 값이 적합하지 않습니다"),

    PRICE_Required("price", 1, "구매 가격이 존재하지 않습니다"),
    PRICE_MISMATCHED("price", 2, "구매 가격이 일치하지 않습니다"),
    PRICE_INAPPROPRIATE("price", 3, "구매 가격이 적합하지 않습니다");

    private String field;
    private Integer errorCode;
    private String errorMessage;
}
