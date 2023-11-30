package com.gifthub.gifticon.dto;

import com.gifthub.gifticon.enumeration.GifticonStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GifticonSearchCond {
    private Long productId;
    private GifticonStatus gifticonStatus;
    private LocalDate dueDate;

}
