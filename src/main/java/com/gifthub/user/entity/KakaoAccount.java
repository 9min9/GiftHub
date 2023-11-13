package com.gifthub.user.entity;

import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Entity @Getter @Builder
public class KakaoAccount {
    @Id
    @SequenceGenerator(name = "seq_gen", sequenceName = "seq_diary", allocationSize = 1) 																	// 이름:seq_board2
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_kakaoaccount")
    private int num;

    private String name;
    private String email;
    private String phone_number;



}
