package com.coviam.PricePriceScrapingAPI.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExternalWebsitePrice {
    String price;
    //String merchantName;
    String externalUrl;
}
