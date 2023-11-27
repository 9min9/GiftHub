package com.gifthub.event.attendance.entity;

import com.gifthub.event.attendance.dto.AttendanceDto;
import com.gifthub.global.BaseTimeEntity;
import com.gifthub.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity @Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Attendance extends BaseTimeEntity {

    @Id
    @SequenceGenerator(name = "seq_payment", sequenceName = "seq_payment", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_payment")
    @Column(name = "attendance_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private Integer attendance;

    public AttendanceDto toDto() {
        return AttendanceDto.builder()
                .id(this.id)
                .userDto(this.user.toDto())
                .attendance(this.attendance)
                .createDate(this.getCreateDate())
                .build();
    }

}
