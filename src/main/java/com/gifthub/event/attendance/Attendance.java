package com.gifthub.event.attendance;

import com.gifthub.global.BaseTimeEntity;
import com.gifthub.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 출석 체크 테이블
 */
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

    public void setAttendance(Integer attendance) {
        this.attendance = attendance;
    }
}
