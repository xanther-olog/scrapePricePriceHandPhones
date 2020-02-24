package com.coviam.PricePriceScrapingAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class PricePriceScrapingApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PricePriceScrapingApiApplication.class, args);
	}

}
