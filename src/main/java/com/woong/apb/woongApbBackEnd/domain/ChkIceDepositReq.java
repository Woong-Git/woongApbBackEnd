package com.woong.apb.woongApbBackEnd.domain;


import lombok.Data;

import java.util.Optional;

@Data
public class ChkIceDepositReq {
    Optional<String> fromEoa;
    Optional<String> toEoa;
    Optional<Double> amt;
}
