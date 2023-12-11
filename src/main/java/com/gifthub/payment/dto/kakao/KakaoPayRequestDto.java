package com.gifthub.payment.dto.kakao;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class KakaoPayRequestDto {

    @NotEmpty
    private String itemName;
    @NotNull
    @Min(1000)
    private Integer totalAmount;

}
