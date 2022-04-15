package com.woong.apb.woongApbBackEnd.domain;

import lombok.Data;

@Data
public class Tx {

    String      transferType;
    Transaction transaction;
    Contract    contract;
    String      from;
    String      to;
    String      value;
    String      formattedValue;

    @Data
    public static class Transaction {
        String from;
        String fee;
        long   blockNumber;
        String transactionHash;
        int    typeInt;
        long   timestamp;
        String value;
        String feePayer;
        double feeRatio;
    }

    @Data
    public class Contract {
        String address;
        String name;
        String symbol;
        int    decimals;
    }
}
