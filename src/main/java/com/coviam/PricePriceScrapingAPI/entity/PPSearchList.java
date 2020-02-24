package com.coviam.PricePriceScrapingAPI.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(value = "PricePriceHandphones")
public class PPSearchList {
    @Id
    String identifier;
    int rank;
    String productName;
    String productUrl;
}
