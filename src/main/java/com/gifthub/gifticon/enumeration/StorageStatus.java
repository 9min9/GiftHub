package com.gifthub.gifticon.enumeration;

public enum StorageStatus {

    WAIT_REGISTRATION, // 등록대기
    ADMIN_APPROVAL,  // db(x) -> 관리자에게 문의 :: 검수대기
    COMPLETE_REGISTRATION; // 검수완료상태 -> db에서 GifticonStatus와 image 삭제 및 서버에서 img 삭제



}