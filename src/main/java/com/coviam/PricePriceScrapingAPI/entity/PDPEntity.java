package com.coviam.PricePriceScrapingAPI.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Document(value = "PricePriceHandphoneMappingData")
public class PDPEntity {

    @Id
    String identifier;
    String productName;
    String productStorage;
    String productRam;
    String productColor;
    List<ExternalWebsitePrice> listOfExternalPrices=new ArrayList<>();
}
