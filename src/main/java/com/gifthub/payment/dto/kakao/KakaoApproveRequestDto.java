package com.gifthub.payment.dto.kakao;

import jakarta.validation.constraints.Min;
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
    @Min(0L)
    private Long userId;

}
