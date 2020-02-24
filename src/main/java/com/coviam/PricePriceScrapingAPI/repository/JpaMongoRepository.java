package com.coviam.PricePriceScrapingAPI.repository;

import com.coviam.PricePriceScrapingAPI.entity.PPSearchList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMongoRepository extends MongoRepository<PPSearchList,String>{
}
