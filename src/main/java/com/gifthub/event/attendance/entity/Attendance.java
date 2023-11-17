package com.gifthub.event.attendance.entity;

import com.gifthub.event.attendance.dto.AttendanceDto;
import com.gifthub.global.BaseTimeEntity;
import com.gifthub.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Attendance extends BaseTimeEntity {

    @Id
    @SequenceGenerator(name = "seq_payment", sequenceName = "seq_payment", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_payment")
    private Long id;

    @JoinColumn(name = "userId", unique = true)
    @OneToOne
    private User user;
    private Integer attendance;

    public Integer updateAttendance(Integer attendance) {
        this.attendance = attendance;

        return this.attendance;
    }


    public AttendanceDto toDto() {
        return AttendanceDto.builder()
                .id(this.id)
                .userDto(this.user.toDto())
                .attendance(this.attendance)
                .build();
    }

}
