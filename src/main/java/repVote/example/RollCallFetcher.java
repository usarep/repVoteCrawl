package repVote.example;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/*
 * https://code.google.com/p/selenium/wiki/GettingStarted
 */
public class RollCallFetcher {
    public static void main(String[] args) throws Exception {
        // The Firefox driver supports javascript 
        WebDriver driver = new FirefoxDriver();
        
        // roll call url
        String url = "http://clerk.house.gov/cgi-bin/vote.asp?year=2014&rollnumber=473";
        // url = "http://clerk.house.gov/evs/2014/roll473.xml" ;
        driver.get(url);
        
        String pageSource = driver.getPageSource();
        System.out.println(pageSource);

        driver.quit();
        
        /*
         * 2014: 1 to 480
         * 2014 re pulls (done)
         * 28 
         * 56
         * 261
         * 305
         * 363
         * 380
         * 440
         * 
         * --------
         * 
         * 2013: 1 to 641
         * 2013 re pulls (done)
         * 
         * 3
         * 71
         * 95
         * 185
         * 365
         * 503
         * 588
         * 608
         * 617
         * 
         * 
         * 
         */
    }
}
