package com.gifthub.gifticon.constant;

public enum OcrField {
    DUEDATE("유효기간"),
    brandName("교환처"),
    PRODUCTNAME("상품명");

    private final String field;

OcrField(String field) {
    this.field = field;
}

public String getField() {
    return field;
}
}
