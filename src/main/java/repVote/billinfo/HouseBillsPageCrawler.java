package repVote.billinfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import repVote.db.RepVoteDbUtil;

import common.db.BaseDbUtils;
import common.db.ExecuteWithReturnKey;
import common.db.ExecuteWithReturnKey.ResultWithKey;
import common.util.ObjUtil;
import common.util.StrUtil;

/*
 * https://code.google.com/p/selenium/wiki/GettingStarted
 */
public class HouseBillsPageCrawler 
{
	private static Logger logger = Logger.getLogger(HouseBillsPageCrawler.class);
	public static HouseBillsPageCrawler o = new HouseBillsPageCrawler();
	protected HouseBillsPageCrawler() {}
	
	/*
     * whether or not to crawl indiv bills. if set to true, crawl.
     * on the other hand, since bills repeat across roll calls, more efficient to
     * crawl them as a separate step by pulling them out of db. 
     */
	private static final boolean CRAWL_INDIV_BILLS = false;
	
    public static void main(String[] args) 
    {
    	
    	try
    	{
    		Arguments arguments = Arguments.newInstance(args);
    		
    		// The Firefox driver supports javascript 
	        WebDriver driver = new FirefoxDriver();
	       
	        // select one of the two actions below.
	        // currently using comments. add an arg flag to select TODO
	        
	        o.crawlForYear(driver, arguments.year);
    		
    		// o.crawlRollCallsPage(driver, arguments.url, arguments.year, arguments.startRollCallNum);
    		
    		
    		driver.quit();
    		
        
    	} catch (Exception e) {
    		// logger.error(e.getMessage(), e);
    		logger.error(e.getMessage(), e);
    	}
    }
    
    /*
     * crawl all pages for a given year
     */
    
    public void crawlForYear(WebDriver driver, int year)
    {
    	 
        List<String> pages2Crawl =  o.genPageList(driver, year);
        
        logger.info(pages2Crawl);
        
        for (String url: pages2Crawl)
        {
        	logger.info("start " + url);
        	crawlRollCallsPage(driver, url, year, 0);
        	
        	logger.info("end " + url);
        }
	      
    }
    public void crawlRollCallsPage (WebDriver driver, String url, int year, int startRollCallNum)
    {
    	try
    	{
    		
    		Params params = new Params();
    		
	    	// top level page
	    	// params.url = "http://clerk.house.gov/evs/2014/ROLL_400.asp" ;
	    	// params.year = 2014;
	    	
	    	params.url = url;
	    	params.year = year;
	    	
	    	// even is 2nd, odd is 1st
	    	if (params.year % 2 == 0)
	    		params.session = "2nd";
	    	else
	    		params.session = "1st";
	    	
	    	params.congress =  (params.year - 1787)/2 ; // 113;
	    	
	    	params.startRollCallNum = startRollCallNum;
	    	
	    	crawlRollCallsPage(driver, params);
	        
        
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    	
    	}
    }
    
    public static class Params
    {
    	String url;
    	int year;
    	String session; // 1st or 2nd
    	int chamberId = 1; // US House == 1, US Senate == 2
    	String chamber = "U.S. House of Representatives";
    	int congress; // 113 etc
    	
    	int startRollCallNum; // if > 0, start with this as the min roll call number; earlier values have been processed
    }
    
    /*
     * generate a list of pages to crawl to get meta data on the bills that
     * came up for roll call. each link is of the form
     * 
     * http://clerk.house.gov/evs/2008/ROLL_000.asp
     */
    public List<String> genPageList(WebDriver driver, int year)
    {
    	List<String> result = new ArrayList<String>();
    	
    	// links to capture http://clerk.house.gov/evs/+ year + /ROLL_\d+.asp
    	/*
    	 * top level page (index page) link: http://clerk.house.gov/evs/2008/index.asp
    	 * 
    	 * http://clerk.house.gov/evs/+ year + /index.asp
    	 */
    	
    	// e.g., http://clerk.house.gov/evs/2008/index.asp
    	String indexPageUrl = "http://clerk.house.gov/evs/" + year + "/index.asp" ;
    	driver.get(indexPageUrl);
    	
    	/*
    	 * slow: // WebDriverUtil.findElements(driver, By.xpath("//A" ) ); 
    	 * this may be faster: By.cssSelector("a[href*='long']")
    	 * 
    	 * http://clerk.house.gov/evs/2008/ROLL_000.asp
    	 */
    	
    	// the link, as written on the html page is relative to current page, ie ROLL_500.asp
    	// css selector will use this text
    	String patternStr = "a[href^=\"ROLL_\"]";
    	
    	// the A selector will getAttribute("href") will return the absolute url
    	String patternStrFull = "http://clerk.house.gov/evs/" + year + "/ROLL_\\d+.asp";
    	
    	List<WebElement> allLinks = WebDriverUtil.findElements(driver, By.cssSelector(patternStr ) );
    	
    	if (ObjUtil.isEmpty(allLinks))
    		return result;
    	
    	
    	for (WebElement link : allLinks)
    	{
    		String href = link.getAttribute("href");
    		
    		// check with the full pattern, including the number
    		// see if href matches http://clerk.house.gov/evs/+ year + /ROLL_\d+.asp
    		if ( href.matches(patternStrFull))
    		{
    			result.add(href);
    		}
    		
    	}
    	
    	return result;
    }
    
    public void crawlRollCallsPage(WebDriver driver, Params params) throws Exception 
    {
        
        // Go to the top level page
        driver.get(params.url);
        
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
        
        List<WebElement> allTRs =  WebDriverUtil.findElements(driver, By.xpath("//TR" ) ); // driver.findElements(By.xpath("//TR"));
        
        List<BillRollCall> billRollCalls = new ArrayList<BillRollCall>();
        
        for (WebElement tr : allTRs) 
        {
        	List<WebElement>  tds = tr.findElements(By.xpath(".//TD"));
        	
        	if (tds != null && tds.size() > 3) 
        	{
        		WebElement firstCol = tds.get(0);
        		String rollCallNumStr = firstCol.getText();
        		
        		// skip if there is no roll call
        		if (rollCallNumStr == null || rollCallNumStr.trim().isEmpty())
        		{
        			continue;
        		}
        		
        		BillRollCall b = new BillRollCall();
        		try {
        			b.rollCallNum = Integer.parseInt(rollCallNumStr);
        		} catch (NumberFormatException e) { b.rollCallNum = 0; }
        		
        		if (b.rollCallNum <= 0)
        			continue;
        		
        		if (params.startRollCallNum > 0 && b.rollCallNum < params.startRollCallNum)
        			continue;
        		
        		b.params = params;
        		
        		WebElement thirdCol = tds.get(2);
        		WebElement link = WebDriverUtil.findElement(thirdCol, By.xpath(".//A") );
        	
        		if (link != null)
        			b.billUrl = link.getAttribute("href");
        		else
        		{
        			// bill url is a unique key in the db, so add a random value that is unique
        			// prepend _EMPTY_ so we know it's not a true url
        			Random r = new Random(System.currentTimeMillis());
        			String randomStr = "_EMPTY_" + r.nextLong();
        			b.billUrl = randomStr;
        		}
        		b.billUrlText = thirdCol.getText();
        		
        		System.out.println("rollCallNum=" + rollCallNumStr 
        				+ " billUrl=" + b.billUrl + ", billUrlText=" + b.billUrlText);
        	
        		
        		billRollCalls.add(b);
        		
        	} // if tds not empty
        	
        	
        }  // loop over trs
        
        save2Db(billRollCalls);
        
        /*
         * whether or not to crawl indiv bills. if set to true, crawl.
         * on the other hand, since bills repeat across roll calls, more efficient to
         * crawl them as a separate step by pulling them out of db. 
         */
        if (CRAWL_INDIV_BILLS)
        	crawl(driver, billRollCalls);
        

        
    }  // execute
    
    protected int save2Db(Collection<BillRollCall> elements)
    {
    	if (ObjUtil.isEmpty(elements))
    		return 0;
    	
    	Connection con = null;
    	PreparedStatement pstmt = null;
    	
    	PreparedStatement pstmt2 = null;
    	
    	/*
    	 url is a unique key
    	 doc_type, doc_number are also unique key
    	 
    	 insert into bill (doc_type, doc_number, name, url, doc_title) values (?,?,?,?,?)
    	 on duplicate key update id = last_insert_id(id), name=values(name), doc_title=values(doc_title);
    	 
    	 12/4/15: add crs_data_crawled to the list of fields saved. set it to 2 for urls matching EMPTY. if there is alreay a value
    	 for it, let it stay
    	 */
    	String sql = "insert into bill (doc_type, doc_number, name, url, doc_title, crs_data_crawled) values (?,?,?,?,?,?) "
    	 + " on duplicate key update id = last_insert_id(id), name=values(name), doc_title=values(doc_title) ; ";
    	
    	/*
    	 insert into vote_meta_small 
    	 	(bill_id, roll_call_num, congress, year, session, chamber)
    	 values (?,?,?,?,?,?) 
    	 on duplicate key update id=last_insert_id(id);
    	 
    	 */
    	String sqlVoteMetaSmall = "insert into vote_meta_small "
    	 	 + " (bill_id, roll_call_num, congress, year, session, chamber_id, chamber) "
    	 	 + " values (?,?,?,?,?,?,?) "
    	 	 + " on duplicate key update id=last_insert_id(id), bill_id=values(bill_id); ";
    	
    	int numSaved = 0;
    	try
    	{
    		con = BaseDbUtils.o.getConnectionNonTL(RepVoteDbUtil.o.createDbConfig());
    		con.setAutoCommit(false);
    		
    		pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    		
    		pstmt2 = con.prepareStatement(sqlVoteMetaSmall);
    		
    		
    		for (BillRollCall e : elements)
    		{
    			pstmt.clearParameters();
    			int i = 1;
    			
    			Integer docNumber = e.getDocNumber();
    			String docType = e.getDocType();
    			
    			/*
    			 * (docNumber, docType) is a unique key if both are non null.
    			 * for docType == JOURNAL or ADJOURN or QUORUM, there is no docNumber. 
    			 * For these, just enter docType. if one field of the key is null, multiple ADJOURNs etc 
    			 * will not violate the unique key constraint.
    			 */
    			if (docNumber == null || StrUtil.isEmpty(docType))
    			{
    				logger.info("docNumber=" + docNumber + ", docType=" + docType + ", this is ok.  saving as is.");
    				// continue;
    				
    				/*
    				 * kludge. 
    				 * if we rerun the crawler, it will generate new records for the bill.
    				 * we don't really want that. in fact, this is not a bill anyway,
    				 * it's just an id that corresponds to the roll call. 
    				 * if two "ADJOURN" records have the same docNumber, it doesn't hurt.
    				 * so we are using rollCallNum for docNumber.
    				 * if (docNumber,docType) collides for this case, it's ok.
    				 */
    				if (docNumber == null)
    					docNumber = new Integer(e.rollCallNum);
    				
    				if (docType == null)
    					docType = "NONE";
    				
    			}
    			pstmt.setString(i++, docType);
    			BaseDbUtils.o.setInt(pstmt, docNumber, i++);
    			BaseDbUtils.o.setString(pstmt, e.billUrlText, i++);
    			pstmt.setString(i++, e.billUrl);
    			BaseDbUtils.o.setString(pstmt, e.docTitle, i++);
    			
    			int crsDataCrawled = 0;
    			if (StrUtil.isEmpty(e.billUrl) || e.billUrl.contains("_EMPTY"))
    				crsDataCrawled=2;
    			
    			pstmt.setInt(i++, crsDataCrawled);
    			
    			ResultWithKey wrappedId = ExecuteWithReturnKey.o.executeUpdate(pstmt);
    			if (wrappedId != null && wrappedId.getGeneratedKey() != null)
    			{
    				// this is not a huge value, will fit as int
    				e.id = new Integer(wrappedId.getGeneratedKey().intValue());
    				numSaved++;
    				
    				pstmt2.clearParameters();
    				int k = 1;
    				pstmt2.setInt(k++, e.id.intValue());
    				
    				pstmt2.setInt(k++, e.rollCallNum);
    				pstmt2.setInt(k++, e.params.congress);
    				pstmt2.setInt(k++, e.params.year);
    				pstmt2.setString(k++, e.params.session);
    				
    				
    				pstmt2.setInt(k++, e.params.chamberId);
    				
    				pstmt2.setString(k++, e.params.chamber);
    				pstmt2.executeUpdate();
    			}
    		}
    		
    		con.commit();
    		
    	} catch (SQLException e) {
    		BaseDbUtils.o.rollback(con);
    		logger.error(e.getMessage(), e);
    		numSaved = -1;
    	} finally {
    		BaseDbUtils.o.close(pstmt2);
    		BaseDbUtils.o.close(pstmt, con);
    	}
    	
    	return numSaved;
    }
    
    protected void testCreateConnection()
    {
    	
    	Connection con = null;
    	
    	try
    	{
    		con = BaseDbUtils.o.getConnectionNonTL(RepVoteDbUtil.o.createDbConfig());
    		con.setAutoCommit(false);
    				
    	} catch (SQLException e) {
    		BaseDbUtils.o.rollback(con);
    		
    	} finally {
    		BaseDbUtils.o.close(con);
    	}
    	
    	
    }
    
    /*
     * many bills repeat -- because roll calls happen on ammendments.
     * 
     * Information on the bill page + crs research summary + index terms are the same.
     * 
     * So can save time by not processing the same information multiple times.
     * 
     * In the future, also read processed bill urls from db. It's small enough to fit in memory.
     */
    
    protected Map<String, Integer> processedBills = new HashMap<String, Integer>();
    protected int crawl(WebDriver driver, Collection<BillRollCall> elements)
    {
    	if (ObjUtil.isEmpty(elements))
    		return 0;
    	
    	int numCrawled = 0;
    	
    	for (BillRollCall e : elements)
		{
    		
    		if (StrUtil.isEmpty(e.billUrl))
    			continue;
    		
    		// if url starts with _EMPTY_
    		if (e.billUrl.startsWith("_EMPTY_"))
    			continue;
    		
    		/*
    		 * has this bill url already been processed ?
    		 */
    		if (processedBills.get(e.billUrl) != null)
    			continue;
    		
    		numCrawled += crawl(driver, e);
    		
    		if (e.id != null)
    		{
    			processedBills.put(e.billUrl, e.id);
    		}
		}
    	
    	
    	return numCrawled;
    }
    
    protected int crawl(WebDriver driver, BillRollCall b)
    {
    	if (b == null)
    		return 0;
    	
    	OneBillPageCrawler.o.execute(driver, b);
    	
    	return 1;
    }
}
