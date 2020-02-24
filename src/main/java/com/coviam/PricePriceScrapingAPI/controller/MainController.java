package com.coviam.PricePriceScrapingAPI.controller;

import com.coviam.PricePriceScrapingAPI.dto.PPSearchListDto;
import com.coviam.PricePriceScrapingAPI.entity.PDPEntity;
import com.coviam.PricePriceScrapingAPI.entity.PPSearchList;
import com.coviam.PricePriceScrapingAPI.response.HandPhoneDataResponse;
import com.coviam.PricePriceScrapingAPI.response.MongoPPResults;
import com.coviam.PricePriceScrapingAPI.service.ServiceClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    ServiceClass serviceClass;

    @GetMapping("/getpdpurls")
    public HttpStatus getAllPdpUrls(){
        serviceClass.scrapePricePricePDPUrls();
        return HttpStatus.OK;
    }

    @PostMapping("/savetomongo")
    public HttpStatus saveFromCsvToMongo(){
        List<PPSearchListDto> allProductListing=new ArrayList<>();
        allProductListing=serviceClass.getAllProductListings();
        for(int i=0;i<allProductListing.size();i++){
            PPSearchListDto ppSearchListDto = allProductListing.get(i);
            PPSearchList ppSearchList = new PPSearchList();
            ppSearchList.setIdentifier(serviceClass.getAlphaNumericString());
            ppSearchList.setProductName(ppSearchListDto.getProductName());
            ppSearchList.setRank(ppSearchListDto.getRank());
            ppSearchList.setProductUrl(ppSearchListDto.getProductUrl());
            serviceClass.addToMongoDb(ppSearchList);
        }
        return HttpStatus.OK;
    }

    @GetMapping("/getfrommongo")
    public ResponseEntity<MongoPPResults> getPhonesFromMongo(){
        List<PPSearchList> allProducts=new ArrayList<>();
        allProducts=serviceClass.getDetailsFromMongo();
        System.out.println(allProducts.size());
        return ResponseEntity.ok(new MongoPPResults(allProducts));
    }

    @GetMapping("/scrapepdp")
    public String scrapePricePricePDP(){
        List<PPSearchList>allProducts=new ArrayList<>();
        //allProducts=serviceClass.getDetailsFromMongo();
        allProducts=serviceClass.getFromCsv();
        serviceClass.scrapeAllPdpUrls(allProducts);
        return "ok";

    }


    @GetMapping("/gethandphonedetail")
    public ResponseEntity<HandPhoneDataResponse> getHandPhoneData(){
        List<PDPEntity> allHandPhoneData=serviceClass.getAllHandPhoneData();
        System.out.println("All handphone data size: "+allHandPhoneData.size());
        return ResponseEntity.ok(new HandPhoneDataResponse(allHandPhoneData));
    }


}
