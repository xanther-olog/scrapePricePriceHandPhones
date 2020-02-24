package com.coviam.PricePriceScrapingAPI.strings;

public class FinalStrings {
    public static final String BASE_URL="https://id.priceprice.com/harga-hp/";
    public static final String disableNotifications="--disable-notifications";
    public static final String chromeWebDriver="webdriver.chrome.driver";
    public static final String pathToChromeWebDriver="/Users/arkadeepbasu/Downloads/chromedriver";
    public static final String pathToSearchResultsCsv="/Users/arkadeepbasu/Desktop/PPsearchResults.csv";


    public static final String xPathToNumberOfPages="//div[@class='pagenation']/p";
    public static final String xPathToAllProductDivs="//div[@class='itemBoxIn']";
    public static final String xPathToProductNameAndUrl="//div[@class='itemBoxIn']/div[@class='itmName']/h3/a";
    public static final String cssSelectorForColor=".itemSumMod .itemSumNav dd.color ul li span";


    public static final String appendToBaseUrlForEachPage="?page=";
    public static final String searchResultsCsvColumnNames="rank,product_name,pdp_url";
}
