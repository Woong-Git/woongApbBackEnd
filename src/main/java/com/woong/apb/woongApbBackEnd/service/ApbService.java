package com.woong.apb.woongApbBackEnd.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woong.apb.woongApbBackEnd.domain.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ApbService {

    public BaseResponse chkIceDeposit(ChkIceDepositReq chkIceDepositReq) {
        BaseResponse res = new BaseResponse();

        try{
            TxRes txRes = new TxRes();

            if((!chkIceDepositReq.getFromEoa().isPresent()) || !chkIceDepositReq.getToEoa().isPresent()) {
                if((chkIceDepositReq.getFromEoa().get().length() != 42) || (chkIceDepositReq.getToEoa().get().length() != 42) ) {
                    res.setCode(400);
                    res.setMsg("지갑 주소가 올바르지 않습니다.");
                    return res;
                }

                if(!chkIceDepositReq.getAmt().isPresent()) {
                    res.setCode(400);
                    res.setMsg("아이스 수량이 올바르지 않습니다.");
                    return res;
                }
            }


            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Basic S0FTS1NaVDhHVkc5QjlQSlVDNjM1TElKOmNEMnVGQkpJTVg0LXhDSWNIdFM1NGgyMGk5cS1SazQ5WFJhd1JUWWI=");
            headers.add("x-chain-id", "8217");
            headers.add("Content-Type", "application/json");

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl("https://th-api.klaytnapi.com/v2/transfer/account/" + chkIceDepositReq.getFromEoa().get())
                    .queryParam("kind", "ft")
                    .queryParam("ca-filter", "0xF030115Fa92BaE821De5AD2E054663064b8b9570")
                    .queryParam("range", "1640972316,1648738800")
                    .queryParam("size","100")
                    .queryParam("exclude-zero-klay","false")
                    .queryParam("from-only", "false")
                    .queryParam("to-only", "false");

            HttpEntity requestHeaders = new HttpEntity(headers);

            RestTemplate       rt    = new RestTemplate();
            ResponseEntity<TxRes> response = rt.exchange(uriBuilder.toUriString(), HttpMethod.GET, requestHeaders, TxRes.class);

            if(!response.getStatusCode().equals(HttpStatus.OK) || response.getBody().getCode() != 0) {
                res.setCode(400);
                res.setMsg("API 호출 응답 에러");
            } else {
                txRes = response.getBody();
            }
            boolean addrChk = false;
            boolean amtChk = false;

            for(Tx tx: txRes.getItems()) {
                if(tx.getTo().equals(chkIceDepositReq.getToEoa().get())) {
                    addrChk = true;
                    if (Integer.parseInt(tx.getFormattedValue()) == chkIceDepositReq.getAmt().get()) {
                        amtChk = true;
                        break;
                    }
                }
            }

            if(!(addrChk && amtChk)) {
                res.setCode(400);
                res.setMsg("확인 실패");
                return res;
            }

            res.getData().put("data", "확인완료!");

        } catch (Exception ex) {
            res.setCode(500);
            res.setMsg(ex.getMessage());
            return res;
        }

        return res;
    }

    public BaseResponse getHolderList() {
        BaseResponse                res     = new BaseResponse();
        HashMap<String, List<Hold>> holders = new HashMap<>();
        String                      cursor  = "";

        try {
            HolderListRes holderListRes;

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Basic S0FTS1NaVDhHVkc5QjlQSlVDNjM1TElKOmNEMnVGQkpJTVg0LXhDSWNIdFM1NGgyMGk5cS1SazQ5WFJhd1JUWWI=");
            headers.add("x-chain-id", "8217");
            headers.add("Content-Type", "application/json");
            HttpEntity requestHeaders = new HttpEntity(headers);

            do {
                UriComponentsBuilder uriBuilder = (cursor.isEmpty()) ?
                        UriComponentsBuilder.fromHttpUrl("https://th-api.klaytnapi.com/v2/contract/nft/0xc984335f3b02382ec7393f9e091d19b84064589b/token").queryParam("size", 30)
                        : UriComponentsBuilder.fromHttpUrl("https://th-api.klaytnapi.com/v2/contract/nft/0xc984335f3b02382ec7393f9e091d19b84064589b/token")
                        .queryParam("size", 30)
                        .queryParam("cursor",cursor);

                RestTemplate rt = new RestTemplate();

                ResponseEntity<HolderListRes> response = rt.exchange(uriBuilder.toUriString(), HttpMethod.GET, requestHeaders, HolderListRes.class);
                holderListRes = response.getBody();

                cursor = holderListRes.getCursor();

                if(holderListRes.getCode() == 0) {
                    for (NFTInfo item : holderListRes.getItems()) {

                        //홀더체크
                        if(!(holders.containsKey(item.getOwner()))) {
                            holders.put(item.getOwner(), new ArrayList<>());
                            holders.get(item.getOwner()).add(new Hold("" + Integer.parseInt(item.getTokenId().substring(2), 16), item.getUpdatedAt()));
                        } else {
                            holders.get(item.getOwner()).add(new Hold("" + Integer.parseInt(item.getTokenId().substring(2), 16), item.getUpdatedAt()));
                        }
                    }
                }
                else {

                }

                res.getData().put("data", holders);

            } while (!cursor.isEmpty());

            return res;

        } catch (Exception ex) {
            res.setCode(500);
            res.setMsg(ex.getMessage());

            return res;
        }
    }
}
