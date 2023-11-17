package com.gifthub.payment.pagination;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Criteria {

    private long page;
    private long size;

    private long totalAmount;

}
