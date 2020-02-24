package com.coviam.PricePriceScrapingAPI.threads;


import com.coviam.PricePriceScrapingAPI.entity.ExternalWebsitePrice;
import com.coviam.PricePriceScrapingAPI.entity.PDPEntity;
import com.coviam.PricePriceScrapingAPI.entity.PPSearchList;
import com.coviam.PricePriceScrapingAPI.strings.FinalStrings;
import lombok.SneakyThrows;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;
import java.util.List;

public class ThreadToScrapePricePricePdp implements Runnable {

    String pdpUrl;
    int rank;
    String productName;

    public ThreadToScrapePricePricePdp(PPSearchList ppSearchList) {
        this.pdpUrl=ppSearchList.getProductUrl();
        this.rank=ppSearchList.getRank();
        this.productName=ppSearchList.getProductName();
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
                pdpUrl
        );

        Thread.sleep(5000);

        Actions actions = new Actions(webDriver);

        try{
            List<WebElement> colors=webDriver.findElements(By.cssSelector(FinalStrings.cssSelectorForColor));
            int numberOfColors=colors.size();
            String currentColor="";
            String currentStorage="";
            String currentRam="";
            System.out.println(productName);
            Thread.sleep(5000);
            for(int i=0;i<numberOfColors;i++){
                actions.click(colors.get(i)).perform();
                Thread.sleep(3000);
                colors=webDriver.findElements(By.cssSelector(FinalStrings.cssSelectorForColor));
                currentColor=colors.get(i).getAttribute("title");
                List<WebElement> availableVariants=webDriver.findElements(By.xpath("//div[@id='top_div_ft_spec']/dl/dt"));
                int numberOfVariants=availableVariants.size();


                if(numberOfVariants==1){



                    List<WebElement> prices=webDriver.findElements(By.xpath("//div[@class='itmPrice']/p[@class='itmPrice_price']"));
                    List<WebElement> productUrls=webDriver.findElements(By.xpath("//div[@class='itmBtn']//a[1]"));
//                    List<WebElement> seller=webDriver.findElements(By.xpath("//div[@class='itmShop ']/div/a"));

//
                    List<ExternalWebsitePrice> listOfPriceDetails=new ArrayList<>();
                    for(int l=0;l<prices.size();l++){
                        String costOnOnlineWebsite=prices.get(l).getAttribute("innerText").trim();
//                        String onlineSellerName=seller.get(l).getAttribute("href");
                        String externalWebsiteUrl=productUrls.get(l).getAttribute("href");
                        ExternalWebsitePrice externalWebsitePrice=new ExternalWebsitePrice();
                        externalWebsitePrice.setExternalUrl(externalWebsiteUrl);
//                        String arr[]=onlineSellerName.split("/");
//                        externalWebsitePrice.setMerchantName(arr[arr.length-1]);
                        externalWebsitePrice.setPrice(costOnOnlineWebsite);
                        listOfPriceDetails.add(externalWebsitePrice);
                    }

                    PDPEntity pdpEntity=new PDPEntity();
                    pdpEntity.setProductName(productName);
                    pdpEntity.setProductStorage("-");
                    pdpEntity.setProductRam("-");
                    pdpEntity.setProductColor(currentColor);
                    pdpEntity.setListOfExternalPrices(listOfPriceDetails);

                    synchronized (System.out){
                        System.out.println(pdpEntity);
                    }


                }else if(numberOfVariants==2){
                    //color available with either ram/rom
                    String variantAvailable=webDriver.findElement(By.xpath("//div[@id='top_div_ft_spec']/dl/dt[1]")).getText();
                    List<WebElement> variants=webDriver.findElements(By.xpath("//div[@id='top_div_ft_spec']/dl/dd[1]/ul/li"));
                    int numberOfRamOrRomVariants=variants.size();
                    for(int j=1;j<numberOfRamOrRomVariants;j++){
                        List<ExternalWebsitePrice> listOfPriceDetails=new ArrayList<>();
                        actions.click(variants.get(j)).perform();
                        Thread.sleep(3000);
                        variants=webDriver.findElements(By.xpath("//div[@id='top_div_ft_spec']/dl/dd[1]/ul/li"));
                        colors=webDriver.findElements(By.cssSelector(FinalStrings.cssSelectorForColor));

                        //

                        List<WebElement> colorSelectors=webDriver.findElements(By.xpath("//div[@id='top_div_ft_spec']/dl/dd[@class='color']/ul/li"));
                        if(colorSelectors.get(i).getAttribute("class")==null || colorSelectors.get(i).getAttribute("class").equals("")) {
                            actions.click(colors.get(i)).perform();
                            Thread.sleep(3000);
                            variants = webDriver.findElements(By.xpath("//div[@id='top_div_ft_spec']/dl/dd[1]/ul/li"));
                            colors = webDriver.findElements(By.cssSelector(FinalStrings.cssSelectorForColor));
                            colorSelectors=webDriver.findElements(By.xpath("//div[@id='top_div_ft_spec']/dl/dd[@class='color']/ul/li"));
                        }




                        String currentVariant=variants.get(j).getText();

//

                        List<WebElement> prices=webDriver.findElements(By.xpath("//div[@class='itmPrice']/p[@class='itmPrice_price']"));
                        List<WebElement> productUrls=webDriver.findElements(By.xpath("//div[@class='itmBtn']//a[1]"));
//                        List<WebElement> seller=webDriver.findElements(By.xpath("//div[@class='itmShop ']/div/a"));

//


                        System.out.println();


                        for(int l=0;l<prices.size();l++){
                            String costOnOnlineWebsite=prices.get(l).getAttribute("innerText").trim();
//                            String onlineSellerName=seller.get(l).getAttribute("href");
                            String externalWebsiteUrl=productUrls.get(l).getAttribute("href");
                            ExternalWebsitePrice externalWebsitePrice=new ExternalWebsitePrice();
                            externalWebsitePrice.setExternalUrl(externalWebsiteUrl);
//                            String arr[]=onlineSellerName.split("/");
//                            externalWebsitePrice.setMerchantName(arr[arr.length-1]);
                            externalWebsitePrice.setPrice(costOnOnlineWebsite);
                            listOfPriceDetails.add(externalWebsitePrice);
                        }
                        PDPEntity pdpEntity=new PDPEntity();
                        if(variantAvailable.equals("RAM")){
                            pdpEntity.setProductName(productName);
                            pdpEntity.setProductStorage("-");
                            pdpEntity.setProductRam(currentVariant);
                            pdpEntity.setProductColor(currentColor);
                            pdpEntity.setListOfExternalPrices(listOfPriceDetails);

                            synchronized (System.out){
                                System.out.println(pdpEntity);
                            }

                        }else if(variantAvailable.equals("Storage")){
                            pdpEntity.setProductName(productName);
                            pdpEntity.setProductStorage(currentVariant);
                            pdpEntity.setProductRam("-");
                            pdpEntity.setProductColor(currentColor);
                            pdpEntity.setListOfExternalPrices(listOfPriceDetails);
                            synchronized (System.out){
                                System.out.println(pdpEntity);
                            }
                        }
                    }
                }
                else if(numberOfVariants==3){
                    //all 3 available
                    List<WebElement> listOfStorageVariants=webDriver.findElements(By.xpath("//div[@id='top_div_ft_spec']/dl/dd[1]/ul/li"));
                    int numberOfStorageVariants=listOfStorageVariants.size();
                    for(int j=1;j<numberOfStorageVariants;j++) {
                        String storageClassStatus = listOfStorageVariants.get(j).getAttribute("class");
                        actions.click(listOfStorageVariants.get(j)).perform();
                        Thread.sleep(3000);
                        listOfStorageVariants = webDriver.findElements(By.xpath("//div[@id='top_div_ft_spec']/dl/dd[1]/ul/li"));
                        colors = webDriver.findElements(By.cssSelector(FinalStrings.cssSelectorForColor));

                        //

                        List<WebElement> colorSelectors=webDriver.findElements(By.xpath("//div[@id='top_div_ft_spec']/dl/dd[@class='color']/ul/li"));
                        if(colorSelectors.get(i).getAttribute("class")==null || colorSelectors.get(i).getAttribute("class").equals("")) {
                            actions.click(colors.get(i)).perform();
                            Thread.sleep(3000);
                            listOfStorageVariants = webDriver.findElements(By.xpath("//div[@id='top_div_ft_spec']/dl/dd[1]/ul/li"));
                            colors = webDriver.findElements(By.cssSelector(FinalStrings.cssSelectorForColor));
                            colorSelectors=webDriver.findElements(By.xpath("//div[@id='top_div_ft_spec']/dl/dd[@class='color']/ul/li"));
                        }


                        currentStorage = listOfStorageVariants.get(j).getText();
                        List<WebElement> ramVariants = webDriver.findElements(By.xpath("//div[@id='top_div_ft_spec']/dl/dd[2]/ul/li"));
                        int numberOfRamVariants = ramVariants.size();
                        for (int k = 1; k < numberOfRamVariants; k++) {
                            String classStatus = ramVariants.get(k).getAttribute("class");
                            if (classStatus.equals("off")) {
                                continue;
                            }
                            List<ExternalWebsitePrice> listOfPriceDetails = new ArrayList<>();
                            actions.click(ramVariants.get(k)).perform();
                            Thread.sleep(3000);
                            ramVariants = webDriver.findElements(By.xpath("//div[@id='top_div_ft_spec']/dl/dd[2]/ul/li"));
                            colors = webDriver.findElements(By.cssSelector(FinalStrings.cssSelectorForColor));
                            listOfStorageVariants = webDriver.findElements(By.xpath("//div[@id='top_div_ft_spec']/dl/dd[1]/ul/li"));


                            //

                            colorSelectors=webDriver.findElements(By.xpath("//div[@id='top_div_ft_spec']/dl/dd[@class='color']/ul/li"));
                            System.out.println();
                            if(colorSelectors.get(i).getAttribute("class")==null || colorSelectors.get(i).getAttribute("class").equals("")) {
                                actions.click(colors.get(i)).perform();
                                Thread.sleep(3000);
                                ramVariants = webDriver.findElements(By.xpath("//div[@id='top_div_ft_spec']/dl/dd[2]/ul/li"));
                                colors = webDriver.findElements(By.cssSelector(FinalStrings.cssSelectorForColor));
                                listOfStorageVariants = webDriver.findElements(By.xpath("//div[@id='top_div_ft_spec']/dl/dd[1]/ul/li"));
                                colorSelectors=webDriver.findElements(By.xpath("//div[@id='top_div_ft_spec']/dl/dd[@class='color']/ul/li"));
                            }

                            currentRam = ramVariants.get(k).getText();
//


                            Thread.sleep(3000);





                            List<WebElement> prices=webDriver.findElements(By.xpath("//div[@class='itmPrice']/p[@class='itmPrice_price']"));
                            List<WebElement> productUrls=webDriver.findElements(By.xpath("//div[@class='itmBtn']//a[1]"));
//                            List<WebElement> seller=webDriver.findElements(By.xpath("//div[@class='itmShop ']/div/a"));

//

                            System.out.println();

                            for (int l = 0; l < prices.size(); l++) {
                                String costOnOnlineWebsite = prices.get(l).getAttribute("innerText").trim();
//                                String onlineSellerName = seller.get(l).getAttribute("href");
                                String externalWebsiteUrl = productUrls.get(l).getAttribute("href");
                                ExternalWebsitePrice externalWebsitePrice = new ExternalWebsitePrice();
                                externalWebsitePrice.setExternalUrl(externalWebsiteUrl);
//                                String arr[]=onlineSellerName.split("/");
//                                externalWebsitePrice.setMerchantName(arr[arr.length-1]);
                                externalWebsitePrice.setPrice(costOnOnlineWebsite);
                                listOfPriceDetails.add(externalWebsitePrice);
                            }
                            PDPEntity pdpEntity = new PDPEntity();
                            pdpEntity.setProductName(productName);
                            pdpEntity.setProductStorage(currentStorage);
                            pdpEntity.setProductRam(currentRam);
                            pdpEntity.setProductColor(currentColor);
                            pdpEntity.setListOfExternalPrices(listOfPriceDetails);
                            synchronized (System.out) {
                                System.out.println(pdpEntity);

                            }
                        }

                    }
                }


            }
            if(numberOfColors==0){
                List<WebElement> availableVariants=webDriver.findElements(By.xpath("//div[@id='top_div_ft_spec']/dl/dt"));
                if(availableVariants.size()==2){
                    //ram and rom available
                    List<WebElement> listOfStorageVariants=webDriver.findElements(By.xpath("//div[@id='top_div_ft_spec']/dl/dd[1]/ul/li"));
                    int numberOfStorageVariants=listOfStorageVariants.size();
                    for(int i=0;i<numberOfStorageVariants;i++){
                        actions.click(listOfStorageVariants.get(i)).perform();
                        Thread.sleep(3000);
                        listOfStorageVariants = webDriver.findElements(By.xpath("//div[@id='top_div_ft_spec']/dl/dd[1]/ul/li"));
                        currentStorage = listOfStorageVariants.get(i).getText();
                        List<WebElement> ramVariants = webDriver.findElements(By.xpath("//div[@id='top_div_ft_spec']/dl/dd[2]/ul/li"));
                        int numberOfRamVariants = ramVariants.size();
                        for (int j = 1; j < numberOfRamVariants; j++) {
                            String classStatus = ramVariants.get(j).getAttribute("class");
                            if (classStatus.equals("off")) {
                                continue;
                            }
                            List<ExternalWebsitePrice> listOfPriceDetails = new ArrayList<>();
                            actions.click(ramVariants.get(j)).perform();
                            Thread.sleep(3000);
                            ramVariants = webDriver.findElements(By.xpath("//div[@id='top_div_ft_spec']/dl/dd[2]/ul/li"));
                            listOfStorageVariants = webDriver.findElements(By.xpath("//div[@id='top_div_ft_spec']/dl/dd[1]/ul/li"));

                            currentRam = ramVariants.get(j).getText();
//


                            Thread.sleep(3000);





                            List<WebElement> prices=webDriver.findElements(By.xpath("//div[@class='itmPrice']/p[@class='itmPrice_price']"));
                            List<WebElement> productUrls=webDriver.findElements(By.xpath("//div[@class='itmBtn']//a[1]"));
//                            List<WebElement> seller=webDriver.findElements(By.xpath("//div[@class='itmShop ']/div/a"));


                            for (int k = 0; k < prices.size(); k++) {
                                String costOnOnlineWebsite = prices.get(k).getAttribute("innerText").trim();
//                                String onlineSellerName = seller.get(k).getAttribute("href");
                                String externalWebsiteUrl = productUrls.get(k).getAttribute("href");
                                ExternalWebsitePrice externalWebsitePrice = new ExternalWebsitePrice();
                                externalWebsitePrice.setExternalUrl(externalWebsiteUrl);
//                                String arr[]=onlineSellerName.split("/");
//                                externalWebsitePrice.setMerchantName(arr[arr.length-1]);
                                externalWebsitePrice.setPrice(costOnOnlineWebsite);
                                listOfPriceDetails.add(externalWebsitePrice);
                            }

                            PDPEntity pdpEntity = new PDPEntity();
                            pdpEntity.setProductName(productName);
                            pdpEntity.setProductStorage(currentStorage);
                            pdpEntity.setProductRam(currentRam);
                            pdpEntity.setProductColor("-");
                            pdpEntity.setListOfExternalPrices(listOfPriceDetails);
                            synchronized (System.out) {
                                System.out.println(pdpEntity);

                            }

                        }

                    }

                }
                else if(availableVariants.size()==1){
                    //either ram or rom available
                    String variantAvailable=webDriver.findElement(By.xpath("//div[@id='top_div_ft_spec']/dl/dt[1]")).getText();
                    List<WebElement> variants=webDriver.findElements(By.xpath("//div[@id='top_div_ft_spec']/dl/dd[1]/ul/li"));
                    int numberOfRamOrRomVariants=variants.size();
                    for(int j=1;j<numberOfRamOrRomVariants;j++){
                        List<ExternalWebsitePrice> listOfPriceDetails=new ArrayList<>();
                        actions.click(variants.get(j)).perform();
                        Thread.sleep(3000);
                        variants=webDriver.findElements(By.xpath("//div[@id='top_div_ft_spec']/dl/dd[1]/ul/li"));




                        String currentVariant=variants.get(j).getText();

//

                        List<WebElement> prices=webDriver.findElements(By.xpath("//div[@class='itmPrice']/p[@class='itmPrice_price']"));
                        List<WebElement> productUrls=webDriver.findElements(By.xpath("//div[@class='itmBtn']//a[1]"));
//                        List<WebElement> seller=webDriver.findElements(By.xpath("//div[@class='itmShop ']/div/a"));

//


                        System.out.println();


                        for(int l=0;l<prices.size();l++){
                            String costOnOnlineWebsite=prices.get(l).getAttribute("innerText").trim();
//                            String onlineSellerName=seller.get(l).getAttribute("href");
                            String externalWebsiteUrl=productUrls.get(l).getAttribute("href");
                            ExternalWebsitePrice externalWebsitePrice=new ExternalWebsitePrice();
                            externalWebsitePrice.setExternalUrl(externalWebsiteUrl);
//                            String arr[]=onlineSellerName.split("/");
//                            externalWebsitePrice.setMerchantName(arr[arr.length-1]);
                            externalWebsitePrice.setPrice(costOnOnlineWebsite);
                            listOfPriceDetails.add(externalWebsitePrice);
                        }
                        PDPEntity pdpEntity=new PDPEntity();
                        if(variantAvailable.equals("RAM")){
                            pdpEntity.setProductName(productName);
                            pdpEntity.setProductStorage("-");
                            pdpEntity.setProductRam(currentVariant);
                            pdpEntity.setProductColor("-");
                            pdpEntity.setListOfExternalPrices(listOfPriceDetails);

                            synchronized (System.out){
                                System.out.println(pdpEntity);
                            }

                        }else if(variantAvailable.equals("Storage")){
                            pdpEntity.setProductName(productName);
                            pdpEntity.setProductStorage(currentVariant);
                            pdpEntity.setProductRam("-");
                            pdpEntity.setProductColor("-");
                            pdpEntity.setListOfExternalPrices(listOfPriceDetails);
                            synchronized (System.out){
                                System.out.println(pdpEntity);
                            }
                        }
                    }


                }
                else if(availableVariants.size()==0){
                    //none available
                    List<WebElement> prices=webDriver.findElements(By.xpath("//div[@class='itmPrice']/p[@class='itmPrice_price']"));
                    List<WebElement> productUrls=webDriver.findElements(By.xpath("//div[@class='itmBtn']//a[1]"));
                    //List<WebElement> seller=webDriver.findElements(By.xpath("//div[@class='itmShop ']/div/a"));

                    List<ExternalWebsitePrice> listOfPriceDetails=new ArrayList<>();

                    for (int k = 0; k < prices.size(); k++) {
                        String costOnOnlineWebsite = prices.get(k).getAttribute("innerText").trim();
//                        String onlineSellerName = seller.get(k).getAttribute("href");
                        String externalWebsiteUrl = productUrls.get(k).getAttribute("href");
                        ExternalWebsitePrice externalWebsitePrice = new ExternalWebsitePrice();
                        externalWebsitePrice.setExternalUrl(externalWebsiteUrl);
//                        String arr[]=onlineSellerName.split("/");
//                        externalWebsitePrice.setMerchantName(arr[arr.length-1]);
                        externalWebsitePrice.setPrice(costOnOnlineWebsite);
                        listOfPriceDetails.add(externalWebsitePrice);
                    }

                    PDPEntity pdpEntity = new PDPEntity();
                    pdpEntity.setProductName(productName);
                    pdpEntity.setProductStorage("-");
                    pdpEntity.setProductRam("-");
                    pdpEntity.setProductColor("-");
                    pdpEntity.setListOfExternalPrices(listOfPriceDetails);
                    synchronized (System.out) {
                        System.out.println(pdpEntity);

                    }
                }
            }
        }
        catch (NoSuchElementException nsee){
            System.out.println("element mismatch "+productName);

        }


        webDriver.close();
    }
}
