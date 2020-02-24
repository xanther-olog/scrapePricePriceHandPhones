package com.coviam.PricePriceScrapingAPI.response;

import com.coviam.PricePriceScrapingAPI.entity.PDPEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class HandPhoneDataResponse {
    List<PDPEntity> allHandPhones=new ArrayList<>();

    public HandPhoneDataResponse(List<PDPEntity> allHandPhones) {
        this.allHandPhones = allHandPhones;
    }
}
