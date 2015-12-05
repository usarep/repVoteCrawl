/**
 * 
 */
package repVote.billinfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import repVote.db.RepVoteDbUtil;
import common.db.BaseDbUtils;
import common.util.ObjUtil;
import common.util.StrUtil;

/**
 * @author am
 *
 */
public class SenateCrsSummaryKeywordCrawler
{
	private static Logger logger = Logger.getLogger(SenateCrsSummaryKeywordCrawler.class);
	
	public static SenateCrsSummaryKeywordCrawler o = new SenateCrsSummaryKeywordCrawler();
	protected SenateCrsSummaryKeywordCrawler() {}
	
	public static void main(String[] args) 
    {
    	
    	try
    	{
    		o.execute();
    		
        
    	} catch (Exception e) {
    		// logger.error(e.getMessage(), e);
    		logger.error(e.getMessage(), e);
    	}
    }
	
	public void execute()
	{
		Connection con = null;
		
		/*
		 * retrieve bill urls from database that have not yet been crawled for crsSummary and crsIndexTerms
		 * 
		 */
		try
		{
			
		
			con = BaseDbUtils.o.getConnectionNonTL(RepVoteDbUtil.o.createDbConfig());
    		con.setAutoCommit(false);
    		
			List<BillRollCall> bills2Crawl = fetchBills2CrawlFromDb(con);
			
			// The Firefox driver supports javascript 
	        WebDriver driver = new FirefoxDriver();
	        
	        o.crawl(driver, bills2Crawl);
	        
	        driver.quit();
			
		} catch (SQLException e) {
			
		} finally {
			BaseDbUtils.o.close(con);
		}
	}
	
	private void crawl (WebDriver driver, List<BillRollCall> bills2Crawl)
	{
		if (ObjUtil.isEmpty(bills2Crawl))
			return;
		
		for (BillRollCall b : bills2Crawl)
		{
			OneBillPageCrawler.o.execute(driver, b);
		}
	}
	

	
	/*
	 * fetch the bills that need to be crawled -- from the db.
	 */
	private List<BillRollCall> fetchBills2CrawlFromDb(Connection con) throws SQLException
	{
		List<BillRollCall> result = new ArrayList<BillRollCall>();
		
		/*
		 * select id, doc_type, name, url from bill where crs_data_crawled=0 and doc_type like 'S%'
		 * 
		 * select id, doc_type, name, url from bill where crs_data_crawled=0 and crs_summary is null and csv_crs_index_terms is null
		 * 
		 * 12/4/15: db has been updated, so simply this is fine:
		 * select id, doc_type, name, url from bill where crs_data_crawled=0
		 * 
		 */
		String sql = "select id, doc_type, name, url from bill where crs_data_crawled=0 and doc_type like 'S%' ";
		sql = "select id, doc_type, name, url from bill where crs_data_crawled=0 and crs_summary is null and csv_crs_index_terms is null ";
		sql = "select id, doc_type, name, url from bill where crs_data_crawled=0  ";
		
		// 12/4/15: crs_data_crawled== 5 was pre primary crs index term. also, some seem to have no crs terms even though the original page does
		// maybe they resemble threads killed? 
		// sql = "select id, doc_type, name, url from bill where crs_data_crawled=5  ";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				String url = rs.getString("url");
				if (StrUtil.isEmpty(url) || url.toUpperCase().contains("_EMPTY"))
					continue;
				
				BillRollCall b = new BillRollCall();
				
				b.billUrl = url;
				b.id = new Integer(rs.getInt("id"));
				b.docType = rs.getString("doc_type");
				b.name = rs.getString("name");
				
				result.add(b);
			}
			
		} finally {
			BaseDbUtils.o.close(rs, pstmt);
		}
		
		return result;
	}

}
