package com.gifthub.point;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PointBuyRequestDto {
    private Long point;
    private Long[] gifticonIds;
}
