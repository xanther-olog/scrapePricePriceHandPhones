package com.coviam.PricePriceScrapingAPI.response;

import com.coviam.PricePriceScrapingAPI.entity.PPSearchList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class MongoPPResults {
    List<PPSearchList> list=new ArrayList<>();

    public MongoPPResults(List<PPSearchList> list) {
        this.list = list;
    }
}
