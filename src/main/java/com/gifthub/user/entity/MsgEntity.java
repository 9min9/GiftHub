package com.gifthub.user.entity;

import lombok.Data;

@Data
public class MsgEntity {
    private String response;
    private Object result;

    public MsgEntity(String response, Object result) {
        this.response = response;
        this.result = result;

    }
}
