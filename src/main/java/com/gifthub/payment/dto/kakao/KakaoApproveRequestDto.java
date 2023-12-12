package com.gifthub.payment.dto.kakao;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KakaoApproveRequestDto {

    @NotBlank
    private String pg_token;
    @NotBlank
    private String paymentId;
    @NotBlank
    private Long userId;

}
