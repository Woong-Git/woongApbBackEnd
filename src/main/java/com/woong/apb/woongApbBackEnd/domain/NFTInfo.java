package com.woong.apb.woongApbBackEnd.domain;

import lombok.Data;

@Data
public class NFTInfo {
    String tokenId;
    String owner;
    String previousOwner;
    String tokenUri;
    String transactionHash;
    long   createdAt;
    long   updatedAt;
}
