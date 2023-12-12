package com.gifthub.event.attendance.exception;

import com.gifthub.global.exception.BaseException;
import lombok.Getter;

@Getter
public class FailedAttendanceException extends BaseException {

    public FailedAttendanceException() {
        super.setField("attendance");
        super.setCode("Failed");
        super.setMessage("출석체크에 실패했습니다. 다시 시도해주세요");
    }

}
