package com.gifthub.event.attendance.dto;

import com.gifthub.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class AttendanceDto {

    private Long id;
    private UserDto userDto;
    private Integer attendance;

    private LocalDateTime createDate;

}
