package com.gifthub.event.attendance.exception;

import com.gifthub.global.exception.BaseException;
import lombok.Getter;

@Getter
public class DuplicateAttendanceException extends BaseException {

    public DuplicateAttendanceException() {
        super.setField("attendance");
        super.setCode("Duplicate");
        super.setMessage("출석체크는 하루에 한 번만 가능합니다. 내일 다시 시도해주세요");
    }
}
