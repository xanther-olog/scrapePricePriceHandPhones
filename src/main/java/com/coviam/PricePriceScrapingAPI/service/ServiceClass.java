package com.coviam.PricePriceScrapingAPI.service;

import com.coviam.PricePriceScrapingAPI.dto.PPSearchListDto;
import com.coviam.PricePriceScrapingAPI.entity.PPSearchList;
import com.coviam.PricePriceScrapingAPI.repository.JpaMongoRepository;
import com.coviam.PricePriceScrapingAPI.strings.FinalStrings;
import com.coviam.PricePriceScrapingAPI.threads.ThreadToGetPDPUrls;
import com.coviam.PricePriceScrapingAPI.threads.ThreadToScrapePricePricePdp;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ServiceClass implements ServiceInterface {


    @Autowired
    JpaMongoRepository jpaMongoRepository;

    @SneakyThrows
    @Override
    public List<PPSearchListDto> getAllProductListings() {
        String line;
        BufferedReader bufferedReader=new BufferedReader(new FileReader(FinalStrings.pathToSearchResultsCsv));
        List<PPSearchListDto> res=new ArrayList<>();
        boolean skip=true;
        while ((line=bufferedReader.readLine())!=null){
            if(skip){
                skip=false;
                continue;
            }else {
                String currentRow[] = line.split(",");
                PPSearchListDto ppSearchListDto = new PPSearchListDto();
                ppSearchListDto.setRank(Integer.parseInt(currentRow[0]));
                ppSearchListDto.setProductName(currentRow[1]);
                ppSearchListDto.setProductUrl(currentRow[2]);
                res.add(ppSearchListDto);
            }
        }
        bufferedReader.close();
        return res;
    }

    @Override
    public String getAlphaNumericString() {
        int n=20;
        byte[] array=new byte[256];
        new Random().nextBytes(array);
        String randomString=new String(array, Charset.forName("UTF-8"));
        StringBuffer r = new StringBuffer();
        for (int k=0;k<randomString.length();k++){
            char ch = randomString.charAt(k);
            if (((ch>='a' && ch<='z')
                    || (ch>='A' && ch<='Z')
                    || (ch>='0' && ch<='9'))
                    && (n>0)){
                r.append(ch);
                n--;
            }
        }
        return r.toString();
    }

    @Override
    public void addToMongoDb(PPSearchList ppSearchList) {
        jpaMongoRepository.save(ppSearchList);
    }

    @Override
    public List<PPSearchList> getDetailsFromMongo() {
        List<PPSearchList> allPhonesList = jpaMongoRepository.findAll();
        return allPhonesList;
    }

    @SneakyThrows
    @Override
    public void scrapePricePricePDPUrls() {
        ExecutorService executorService= Executors.newFixedThreadPool(5);
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(
                FinalStrings.disableNotifications
        );
        System.setProperty(
                FinalStrings.chromeWebDriver, FinalStrings.pathToChromeWebDriver
        );
        WebDriver webDriver = new ChromeDriver(chromeOptions);
        webDriver.get(
                FinalStrings.BASE_URL
        );

        String[] pagenationResults=webDriver.findElement(By.xpath(FinalStrings.xPathToNumberOfPages))
                .getText().split(" ");

        int numberOfPages=Integer.parseInt(pagenationResults[pagenationResults.length-1]);
        List<String> pagenationUrls=new ArrayList<>();
        pagenationUrls.add(webDriver.getCurrentUrl());
        for(int i=2;i<=numberOfPages;i++){
            String currentUrl=FinalStrings.BASE_URL+FinalStrings.appendToBaseUrlForEachPage+Integer.toString(i);
            pagenationUrls.add(currentUrl);
        }

        webDriver.close();
        BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(FinalStrings.pathToSearchResultsCsv
                ,true));
        bufferedWriter.write(FinalStrings.searchResultsCsvColumnNames+"\n");
        bufferedWriter.flush();
        bufferedWriter.close();
        for(int i=0;i<pagenationUrls.size();i++){
            String currentUrl=pagenationUrls.get(i);
            Runnable runnable=new ThreadToGetPDPUrls(currentUrl);
            executorService.execute(runnable);
        }
        executorService.shutdown();
    }

    @Override
    public void scrapeAllPdpUrls(List<PPSearchList> allProducts) {
        ExecutorService executorService= Executors.newFixedThreadPool(5);
        for(int i=0;i<5;i++){
            PPSearchList ppSearchList=allProducts.get(i);
            Runnable runnable=new ThreadToScrapePricePricePdp(ppSearchList);
            executorService.execute(runnable);
        }
        executorService.shutdown();
    }

    @SneakyThrows
    @Override
    public List<PPSearchList> getFromCsv() {
        List<PPSearchList> allProductDetails=new ArrayList<>();
        boolean flag=true;
        String line;
        BufferedReader bufferedReader=new BufferedReader(new FileReader(FinalStrings.pathToSearchResultsCsv));
        while((line=bufferedReader.readLine())!=null){
            if(flag){
                flag=false;
                continue;
            }else{
                String currentRow[]=line.split(",");
                PPSearchList ppSearchList=new PPSearchList();
                ppSearchList.setRank(Integer.parseInt(currentRow[0]));
                ppSearchList.setProductName(currentRow[1]);
                ppSearchList.setProductUrl(currentRow[2]);
                ppSearchList.setIdentifier("nubness");
                allProductDetails.add(ppSearchList);
            }
        }
        return allProductDetails;
    }


}
