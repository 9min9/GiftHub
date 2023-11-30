package com.gifthub.admin.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductAddRequest {
    private String productName;
    private String brandName;
    private String due;
    private String barcode;
    private String storageId;
    private Boolean isConfirm;
}
