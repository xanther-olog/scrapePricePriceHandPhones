package com.coviam.PricePriceScrapingAPI.service;

import com.coviam.PricePriceScrapingAPI.dto.PPSearchListDto;
import com.coviam.PricePriceScrapingAPI.entity.PDPEntity;
import com.coviam.PricePriceScrapingAPI.entity.PPSearchList;

import java.util.List;

public interface ServiceInterface {
    List<PPSearchListDto> getAllProductListings();

    String getAlphaNumericString();

    void addToMongoDb(PPSearchList ppSearchList);

    List<PPSearchList> getDetailsFromMongo();

    void scrapePricePricePDPUrls();

    void scrapeAllPdpUrls(List<PPSearchList> allProducts);

    List<PPSearchList> getFromCsv();

    String getLargeAlphaNumericString();

    void addPDPToMongo(PDPEntity pdpEntity);

    List<PDPEntity> getAllHandPhoneData();
}
