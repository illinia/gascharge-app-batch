package com.gascharge.taemin.app.api;

import com.gascharge.taemin.app.config.ChargeProperties;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChargeApi {
    private final ChargeProperties chargeProperties;

    public ResponseEntity<String> getChargeResponseEntity() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", chargeProperties.getApi().getAuthorization());

        URI uri = URI.create(this.chargeProperties.getApi().getUrl());

        return restTemplate.exchange(new RequestEntity<>(headers, HttpMethod.GET, uri), String.class);
    }

    public List<ChargeApiDto> getChargeDtoList(ResponseEntity<String> res) throws ParseException {
        List<ChargeApiDto> charges = new ArrayList<>();

        JSONArray array = (JSONArray) new JSONParser().parse(res.getBody());

        for (Object o : array) {
            JSONObject object = (JSONObject) o;

            ChargeApiDto charge = ChargeApiDto.builder()
                    .chargePlaceId((String) object.get("chrstn_mno"))
                    .name((String) object.get("chrstn_nm"))
                    .totalCount((Long) object.get("prfect_elctc_posbl_alge"))
                    .currentCount((Long) object.get("wait_vhcle_alge"))
                    .build();

            charges.add(charge);
        }

        return charges;
    }
}












