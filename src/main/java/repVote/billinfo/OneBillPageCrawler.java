package repVote.billinfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import repVote.billinfo.Link.Importance;
import repVote.db.RepVoteDbUtil;
import common.db.BaseDbUtils;
import common.db.ExecuteWithReturnKey;
import common.db.ExecuteWithReturnKey.ResultWithKey;
import common.util.ObjUtil;
import common.util.StrUtil;

public class OneBillPageCrawler
{
	private static Logger logger = Logger.getLogger(OneBillPageCrawler.class);
	public static OneBillPageCrawler o = new OneBillPageCrawler();
	
	protected OneBillPageCrawler() {}
	
	public void execute(WebDriver driver, BillRollCall b)
	{
		// fetch the bill url
		driver.get(b.billUrl);
		
		// store the links for crs summary and crs index terms
		b.crsHref = getUrlForText(driver, " CRS Summary");
		b.indexTermsHref = getUrlForText(driver, "Subjects");
		
		logger.info("crsHref=" + b.crsHref);
		logger.info("indexTermsHref=" + b.indexTermsHref);
		
		
		// fetch the crs summary
		fetchCRSSummary(driver, b);
		
		
		// fetch the crs index terms
		fetchCRSIndexTerms(driver, b);
		
		/*
		 * save to db:
		 * 
		 * crs summary, crs href, index terms href -> update bill
		 * 
		 * the crs index terms themselves -> crs_index_term
		 * 
		 * mapping between bill-id and crs-index-term-id -> bill_crs_index_term
		 */
		
		save2Db(b);
	}
	
	public void fetchCRSSummary(WebDriver driver, BillRollCall b)
	{
		if (StrUtil.isEmpty(b.crsHref))
			return;
		
		// fetch the bill url
		driver.get(b.crsHref);
		
		
		
		// fetch the crs summary
		/*
		  <div id='content'>
		  ...
		  </div>
		 */
		b.crsSummary = getTextForDivId(driver, "content");
		logger.info("crsSummary=" + b.crsSummary);
		
		
	}
	
	public void fetchCRSIndexTerms(WebDriver driver, BillRollCall b)
	{
		if (StrUtil.isEmpty(b.indexTermsHref))
			return;
		
		// fetch the page
		driver.get(b.indexTermsHref);
		
		
		
		// fetch the crs index terms
		b.indexTerms = getCRSIndexTerms(driver);
		logger.info("indexTerms=" + b.indexTerms);
		
		b.updatePrimaryIndexTerm();
		
		
	}
	
	protected String getUrlForText(WebDriver driver, String text) 
	{
		/*
		 * xpath is of the form //a[text()='text_i_want_to_find']
		 */
		String href = null;
		
		/*
		  driver.findElements(By.xpath("//a[text()='" + text + "']"));
		 */
		List<WebElement> crsSummaryList = WebDriverUtil.findElements(driver, By
				.xpath("//a[text()='" + text + "']")) ;

		if (!ObjUtil.isEmpty(crsSummaryList))
			href = crsSummaryList.get(0).getAttribute("href");
		
		return href;
	}
	

	// get the text under a div, identified by id
	protected String getTextForDivId(WebDriver driver, String divId) 
	{
		
		String result = null;
		
		/*
		List<WebElement> list = driver.findElements(By
				.xpath("//div[@id='" + divId + "']"));
		*/
		
		List<WebElement> list = WebDriverUtil.findElements(driver, By
				.xpath("//div[@id='" + divId + "']"));

		if (!ObjUtil.isEmpty(list))
			result = list.get(0).getAttribute("innerHTML") ;    //.getText();
		
		
		
		return result;
	}
	
	/*
	 crs index terms
	 
	 <ul>
<p><li>CRS INDEX TERMS:
<ul><br/><a href="/cgi-bin/bdquery/?&amp;Db=d113&amp;querybd=@FIELD(FLD001+@4(Congress))">Congress</a> 

<br /><a href="/cgi-bin/bdquery/?&amp;Db=d113&amp;querybd=@FIELD(FLD001+@4(House+of+Representatives))">House of Representatives</a> 

<br /><a href="/cgi-bin/bdquery/?&amp;Db=d113&amp;querybd=@FIELD(FLD001+@4(Legislative+rules+and+procedure))">Legislative rules and procedure</a> 
</ul>
</ul>

	find the li which has text matching "CRS INDEX TERMS:"
	get all its children links <a ... >... </a>
	
	the links are returned by the firefox webdriver as absolute urls, even if they are relative in the source html
	 */
	
	protected Set<Link> getCRSIndexTerms(WebDriver driver)
	{
		Set<Link> result = new TreeSet<Link>();
		
		
		// List<WebElement> liList = driver.findElements(By.xpath("//li"));
		List<WebElement> liList = WebDriverUtil.findElements(driver, By.xpath("//li"));
		
		WebElement desiredLi = null;

		if (!ObjUtil.isEmpty(liList))
		{
			for (WebElement e : liList)
			{
				String s = e.getText();
				if (s != null && s.contains("CRS INDEX TERMS:"))
				{
					desiredLi = e;
					break;
				}
			}
		}
		
		if (desiredLi != null)
		{
			// find index terms
			// List<WebElement> aList = desiredLi.findElements(By.xpath(".//a"));
			List<WebElement> aList = WebDriverUtil.findElements(desiredLi, By.xpath(".//a"));
			
			if (!ObjUtil.isEmpty(aList))
			{
				boolean first = true;
				for (WebElement a : aList)
				{
					Link link = new Link();
					link.url = a.getAttribute("href");
					link.text = a.getText();
					
					// for index terms, the first link amongst the list of subjects seems to be the most important.
					if (first)
						link.importance = Importance.HIGH;				
					else
						link.importance = Importance.NORMAL;
					
					first = false;
					result.add(link);
				}
			}
			
		}
		
		return result;
		
		
	}
	
	 protected int save2Db(BillRollCall b)
    {
    	if (b == null)
    		return 0;
    	
    	// if crs summary, crsHref, index terms are all empty, return 0
    	if (StrUtil.isEmpty(b.crsSummary)
    			&& StrUtil.isEmpty(b.crsHref)
    			&& StrUtil.isEmpty(b.indexTermsHref)
    			&& ObjUtil.isEmpty(b.indexTerms)
    			)
    	{
    		return 0;
    	}
    	
    	Connection con = null;
    	PreparedStatement pstmt = null;
    	
    	PreparedStatement pstmt2 = null;
    	
    	PreparedStatement pstmt3 = null;
    	
    	/*
    	 * 11/14/14
    	 * added csv_crs_index_terms to bill
    	 * this simplifies loading solr 
    	 * the separator is a colon because data may have commas
    	 * 
    	 * 11/1/15: added primary_crs_index_term to bill
    	 */
    	
    	/*
    	 update bill set crs_summary=?, crs_summary_url=?, index_terms_url=? , csv_crs_index_terms=? 
    	 	, primary_crs_index_term=?,  crs_data_crawled=1, crs_data_crawl_date=now() where id=?
    	 */
    	String sql = "update bill set crs_summary=?, crs_summary_url=?, index_terms_url=?, csv_crs_index_terms=? , primary_crs_index_term=?,  crs_data_crawled=1, crs_data_crawl_date=now() where id=? " ;
    	
    	/*
    	 insert into vote_meta_small 
    	 	(bill_id, roll_call_num, congress, year, session, chamber)
    	 values (?,?,?,?,?,?) 
    	 on duplicate key update id=last_insert_id(id);
    	 
    	 */
    	String sql2 = "insert into crs_index_term (tag, tag_url) values (?,?) "
    	 	 + " on duplicate key update id=last_insert_id(id) ; ";
    	
    	
    	/*
    	 insert ignore into bill_crs_index_term (bill_id, crs_index_term_id) values (?,?)
    	 */
    	
    	String sql3 = "insert ignore into bill_crs_index_term (bill_id, crs_index_term_id) values (?,?)";
    	
    	int numSaved = 0;
    	try
    	{
    		con = BaseDbUtils.o.getConnectionNonTL(RepVoteDbUtil.o.createDbConfig());
    		con.setAutoCommit(false);
    		
    		pstmt = con.prepareStatement(sql);
    	
    		int i = 1;
    		BaseDbUtils.o.setString(pstmt, b.crsSummary, i++);
    		BaseDbUtils.o.setString(pstmt, b.crsHref, i++);
    		BaseDbUtils.o.setString(pstmt, b.indexTermsHref, i++);
    		BaseDbUtils.o.setString(pstmt, b.getCsvIndexTerms(), i++);
    		
    		String primaryIndexTermStr = b.primaryIndexTerm != null? b.primaryIndexTerm.text : null;
    		BaseDbUtils.o.setString(pstmt, primaryIndexTermStr, i++);
    		
    		pstmt.setInt(i++, b.id.intValue());
    		pstmt.executeUpdate();
    		
    		// pstmt2 - index terms
    		if (!ObjUtil.isEmpty(b.indexTerms))
    		{
    			pstmt2 = con.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS);
    			
    			pstmt3 = con.prepareStatement(sql3);
    			
    			for (Link link : b.indexTerms)
    			{
    				pstmt2.clearParameters();
    				int k = 1;
    				pstmt2.setString(k++, link.text);
    				pstmt2.setString(k++, link.url);
    				
    				ResultWithKey rk = ExecuteWithReturnKey.o.executeUpdate(pstmt2);
    				if (rk != null && rk.getGeneratedKey() != null)
    				{
    					link.id = new Integer(rk.getGeneratedKey().intValue());
    					
    					// add (bill-id, link-id) to the many-to-many mapping table bill_crs_index_term
    					int jj=1;
    					pstmt3.clearParameters();
    					pstmt3.setInt(jj++, b.id); // bill id
    					pstmt3.setInt(jj++, link.id.intValue()); // id for this crs index term
    					pstmt3.executeUpdate();
    				}
    			}
    		}
    		
    		numSaved++; // number of bill ids processed - which is 1.
    		con.commit();
    		
    	} catch (SQLException e) {
    		BaseDbUtils.o.rollback(con);
    		logger.error(e.getMessage(), e);
    		numSaved = -1;
    	} finally {
    		BaseDbUtils.o.close(pstmt2);
    		BaseDbUtils.o.close(pstmt3);
    		BaseDbUtils.o.close(pstmt, con);
    	}
    	
    	return numSaved;
    }
	
	public static void main(String[] args) throws Exception
	{
		BillRollCall b = new BillRollCall();
		b.billUrl = "http://thomas.loc.gov/cgi-bin/bdquery/z?d113:h.res.00669:";
		b.id=76;
		
		WebDriver driver = new FirefoxDriver();
		
		o.execute(driver, b);
		
		driver.quit();
	}
	
	private void junk(WebDriver driver, BillRollCall b)
	{
		// fetch the bill url
		driver.get(b.billUrl);
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		
		// check if jquery is on the page
		Boolean flag = (Boolean) js.executeScript("return typeof jQuery == 'undefined'");
		
		if (flag != null && flag.booleanValue())
		{
			// inject jquery onto the page
			 {
			      js.executeScript("var jq = document.createElement('script');jq.src = '//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js';document.getElementsByTagName('head')[0].appendChild(jq);");
			      try {
						Thread.sleep(300);
			      } catch (InterruptedException e)  {	}
			  }
		}
		
		js.executeScript("$(\"a:contains('CRS Summary')\") ");
		// store the links for crs summary and crs index terms
		
		// fetch the crs summary
		
		// fetch the crs index terms
	}
	
	

}
