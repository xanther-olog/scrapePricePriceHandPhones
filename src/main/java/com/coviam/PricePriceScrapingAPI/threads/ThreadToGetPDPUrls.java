package com.coviam.PricePriceScrapingAPI.threads;

import com.coviam.PricePriceScrapingAPI.strings.FinalStrings;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

public class ThreadToGetPDPUrls implements Runnable {

    private String searchPageUrl;

    public ThreadToGetPDPUrls(String url) {
        this.searchPageUrl=url;
    }

    @SneakyThrows
    @Override
    public void run() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(
                FinalStrings.disableNotifications
        );
        System.setProperty(
                FinalStrings.chromeWebDriver, FinalStrings.pathToChromeWebDriver
        );
        WebDriver webDriver = new ChromeDriver(chromeOptions);
        webDriver.get(
                searchPageUrl
        );


        List<WebElement> allProductContainers=webDriver.findElements(By.xpath(FinalStrings.xPathToAllProductDivs));
        List<WebElement> productNamesAndUrlsOfProductsInCurrentPage=webDriver.findElements(By
                .xpath(FinalStrings.xPathToProductNameAndUrl));

        BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(FinalStrings.pathToSearchResultsCsv
                ,true));

        for(int i=0;i<allProductContainers.size();i++){
            String[] allProductDetails=allProductContainers.get(i).getText().split("\n");
            int rank=Integer.parseInt(allProductDetails[0]);
            String productName=allProductDetails[1];
            String url=productNamesAndUrlsOfProductsInCurrentPage.get(i).getAttribute("href");
            synchronized (bufferedWriter){
                bufferedWriter.write("\"" + rank + "\"" + ",");
                bufferedWriter.write("\"" + productName + "\""+",");
                bufferedWriter.write("\"" + url + "\""+"\n");
            }
        }

        bufferedWriter.flush();
        bufferedWriter.close();
        webDriver.close();

    }
}


