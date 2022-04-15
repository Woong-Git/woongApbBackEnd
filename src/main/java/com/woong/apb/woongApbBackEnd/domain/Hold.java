package com.woong.apb.woongApbBackEnd.domain;

import lombok.Data;

@Data
public class Hold {
    String tokenID;
    long   holdTime;

    public Hold(String tokenID, long updateAt) {
        this.tokenID  = tokenID;
        this.holdTime = (System.currentTimeMillis()/1000) - updateAt;
    }
}
