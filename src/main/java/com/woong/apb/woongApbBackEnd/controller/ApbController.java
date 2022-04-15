package com.woong.apb.woongApbBackEnd.controller;

import com.woong.apb.woongApbBackEnd.domain.BaseResponse;
import com.woong.apb.woongApbBackEnd.domain.ChkIceDepositReq;
import com.woong.apb.woongApbBackEnd.service.ApbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ApbController {

    private final ApbService apbService;

    @Autowired
    public ApbController(ApbService apbService) {
        this.apbService = apbService;
    }

    @PostMapping("/api/chkIceDeposit")
    public BaseResponse chkIceDeposit(@RequestBody ChkIceDepositReq chkIceDepositReq) {
        return apbService.chkIceDeposit(chkIceDepositReq);
    }

    @GetMapping("/api/getHolderList")
    public BaseResponse getHolerList() {
        return apbService.getHolderList();
    }

}
