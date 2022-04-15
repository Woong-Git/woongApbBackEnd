package com.woong.apb.woongApbBackEnd.domain;

import lombok.Data;

import java.util.HashMap;

@Data
public class BaseResponse {

    private int code;
    private String msg;
    private HashMap<String, Object> data;

    public BaseResponse(Object data) {
        this.data = new HashMap<>();
        this.data.put("data", data);
    }

    public BaseResponse() {
        this.code = 0;
        this.msg  = "OK";
        this.data = new HashMap<>();
    }
}
