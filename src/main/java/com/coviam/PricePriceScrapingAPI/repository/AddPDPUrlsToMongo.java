package com.coviam.PricePriceScrapingAPI.repository;

import com.coviam.PricePriceScrapingAPI.entity.PPSearchList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddPDPUrlsToMongo extends MongoRepository<PPSearchList,String>{
}
