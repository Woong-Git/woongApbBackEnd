package com.woong.apb.woongApbBackEnd.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HolderListRes {
    @JsonAnyGetter
    List<NFTInfo> items;
    String        cursor;
    int           code;
    String        message;
    String        requestId;

    public HolderListRes() {
        this.items = new ArrayList<>();
    }
}
