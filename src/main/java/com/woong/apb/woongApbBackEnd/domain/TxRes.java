package com.woong.apb.woongApbBackEnd.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class TxRes {
    public TxRes() {
        items = new ArrayList<>();
    }

    List<Tx>   items;
    String     cursor;

    int        code;
    String     message;
    String     requestId;
}
