package com.gifthub.product.exception;

import com.gifthub.global.exception.BaseException;

public class NotFoundCategoryException extends BaseException {

    public NotFoundCategoryException() {
        super.setField("Category");
        super.setCode("NotFound");
        super.setMessage("카테고리를 찾을 수 없습니다.");
    }
}
