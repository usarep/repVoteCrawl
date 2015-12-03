package repVote.billinfo;

import java.sql.Timestamp;
import java.util.Set;

import org.apache.log4j.Logger;

import common.util.ObjUtil;
import common.util.StrUtil;
import repVote.billinfo.HouseBillsPageCrawler.Params;
import repVote.billinfo.Link.Importance;

public class BillRollCall
{
	private static Logger logger = Logger.getLogger(BillRollCall.class);
	public static final String SEPARATOR = ";";
	Integer id; // database primary key
	int rollCallNum;
	String billUrl;
	String billUrlText;
	String docTitle;
	
	
	Params params;
	
	String crsHref;
	String indexTermsHref;
	
	String crsSummary;
	
	Set<Link> indexTerms;
	Link primaryIndexTerm;
	
	// for senate crs data
	String docType;  // S, or S.J.Res or S.Con.Res etc
	String name; // S. 20 or S.J.Res. 7
	
	/**
	 * returns index terms as a csv String. separator is a colon because data may have commas
	 */
	public String getCsvIndexTerms()
	{
		if (ObjUtil.isEmpty(indexTerms))
			return null;
		
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Link link : indexTerms)
		{
			if (link == null || StrUtil.isEmpty(link.text))
				continue;
			
			if (!first)
				sb.append(SEPARATOR);
			sb.append(link.text.trim());
			first = false;
		}
		
		if (sb.length() == 0)
			return null;
		else
			return sb.toString();
		
	}
	
	/*
	 * go through the index terms and find the Link that is marked Importance.HIGH. If found, set it as
	 * the primary index term. 
	 * 
	 * There shouldn't be more than one set to HIGH
	 */
	public void updatePrimaryIndexTerm()
	{
		if (ObjUtil.isEmpty(indexTerms))
			return;
		
		int numFound = 0;
		
		for (Link link : indexTerms)
		{
			if (link == null || StrUtil.isEmpty(link.text))
				continue;
			
			if (Importance.HIGH == link.importance) 
			{
				if (numFound >= 1) 
				{
					logger.error("more than one crs index term was marked Importance.HIGH " + primaryIndexTerm.text + link.text);
				}
				
				primaryIndexTerm = link;
				numFound++;
			}
		}
		
		
	}
	
	/*
	 * H. R. 1234 
	 * 
	 * docType = H R
	 * docNumber = 1234
	 */
	
	
	public Integer getDocNumber()
	{
		if (StrUtil.isEmpty(billUrlText))
			return null;
		
		Integer result = null;
		String s= billUrlText.replaceAll("[^\\d]+", " ").trim();
		if (!StrUtil.isEmpty(s))
		{
			try {
				int docNum = Integer.parseInt(s);
				result = new Integer(docNum);
			} catch (NumberFormatException e) {}
		}
		
		return result;
	}
	
	public String getDocType()
	{
		if (StrUtil.isEmpty(billUrlText))
			return null;
		
		String type = billUrlText.replaceAll("\\W+", " ").replaceAll("\\d", "").trim();
		
		return type;
		
		
	}
	
	
}