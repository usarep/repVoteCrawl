package repVote.billinfo;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/*
 * https://code.google.com/p/selenium/wiki/GettingStarted
 */
public class HouseBillInfo 
{
	
    public static void main(String[] args) throws Exception 
    {
    	// top level page
    	String url = "http://clerk.house.gov/evs/2014/ROLL_400.asp" ;
    	
        // The Firefox driver supports javascript 
        WebDriver driver = new FirefoxDriver();
        
        // Go to the top level page
        driver.get(url);
        
        /*
         *
<TABLE BORDER="1" WIDTH="100%"><TR>
<TH><FONT FACE="Arial" SIZE="-1">Roll</FONT></TH>
<TH><FONT FACE="Arial" SIZE="-1">Date</FONT></TH>
<TH><FONT FACE="Arial" SIZE="-1">Issue</FONT></TH>
<TH><FONT FACE="Arial" SIZE="-1">Question</FONT></TH>
<TH><FONT FACE="Arial" SIZE="-1">Result</FONT></TH>
<TH><FONT FACE="Arial" SIZE="-1">Title/Description</FONT></TH></TR>
<TR><TD><A HREF="http://clerk.house.gov/cgi-bin/vote.asp?year=2014&rollnumber=480">480</A></TD>
<TD><FONT FACE="Arial" SIZE="-1">1-Aug</FONT></TD>
<TD><FONT FACE="Arial" SIZE="-1">
<A HREF="http://thomas.loc.gov/cgi-bin/bdquery/z?d113:h.j.res.00076:">H J RES 76</A>
</FONT></TD>
<TD><FONT FACE="Arial" SIZE="-1">On Motion to Concur in the Senate Amendments</FONT></TD>
<TD ALIGN="CENTER"><FONT FACE="Arial" SIZE="-1">P</FONT></TD>
<TD><FONT FACE="Arial" SIZE="-1">Making continuing appropriations for the National Security Administration for fiscal year 2014, and for other purposes.</FONT></TD></TR>
<TR><TD><A HREF="http://clerk.house.gov/cgi-bin/vote.asp?year=2014&rollnumber=479">479</A></TD>
<TD><FONT FACE="Arial" SIZE="-1">1-Aug</FONT></TD>
<TD><FONT FACE="Arial" SIZE="-1">
<A HREF="http://thomas.loc.gov/cgi-bin/bdquery/z?d113:h.r.05272:">H R 5272</A>
</FONT></TD>
<TD><FONT FACE="Arial" SIZE="-1">On Passage</FONT></TD>
<TD ALIGN="CENTER"><FONT FACE="Arial" SIZE="-1">P</FONT></TD>
<TD><FONT FACE="Arial" SIZE="-1">To prohibit certain actions with respect to deferred action for aliens not lawfully present in the United States, and for other purposes.</FONT></TD></TR>


         */
        
        // Enter the query string "Cheese"
       //  WebElement query = driver.findElement(By.name("q"));
       //  query.sendKeys("Cheese");
        
        List<WebElement> allTRs = driver.findElements(By.xpath("//TR"));
        
        for (WebElement tr : allTRs) {
        	List<WebElement>  tds = tr.findElements(By.xpath(".//TD"));
        	
        	if (tds != null && tds.size() > 3) 
        	{
        		WebElement firstCol = tds.get(0);
        		String rollCallNumStr = firstCol.getText();
        		
        		WebElement thirdCol = tds.get(2);
        		WebElement link = thirdCol.findElement(By.xpath(".//A"));
        		String billUrl = null;
        		if (link != null)
        			billUrl = link.getAttribute("href");
        		String billUrlText = thirdCol.getText();
        		
        		System.out.println("rollCallNum=" + rollCallNumStr 
        				+ " billUrl=" + billUrl + ", billUrlText=" + billUrlText);
        	}
        }
        

        driver.quit();
    }
}
