package com.coviam.PricePriceScrapingAPI.repository;

import com.coviam.PricePriceScrapingAPI.entity.PDPEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AddPDPDataToMongo extends MongoRepository <PDPEntity,String> {
}
