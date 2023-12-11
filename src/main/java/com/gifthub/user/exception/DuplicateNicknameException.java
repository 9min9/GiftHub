package com.gifthub.user.exception;

import com.gifthub.global.exception.BaseException;
import lombok.Getter;

@Getter
public class DuplicateNicknameException extends BaseException {

    public DuplicateNicknameException() {
        super.setField("nickname");
        super.setCode("Duplicate.nickname");
        super.setMessage("닉네임이 중복되었습니다");
    }
}

