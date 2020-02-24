package com.coviam.PricePriceScrapingAPI.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class PDPEntity {
    String productName;
    String productStorage;
    String productRam;
    String productColor;
    List<ExternalWebsitePrice> listOfExternalPrices=new ArrayList<>();
}
